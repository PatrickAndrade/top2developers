package epfl.lsr.bachelor.project.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

import epfl.lsr.bachelor.project.connection.IOConnection;
import epfl.lsr.bachelor.project.connection.PipelinedConnection;
import epfl.lsr.bachelor.project.pipe.SingleThreadPipe;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * The server accepting the connections on port 22122
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class Server implements ServerInterface {

	private ServerSocket mServerSocket;
	private ExecutorService mThreadPool = 
			Executors.newFixedThreadPool(Constants.NUMBER_OF_PIPELINED_CONNECTIONS);
	private RequestBuffer mRequestBuffer = new RequestBuffer();
	
	private InetSocketAddress mInetSocketAddress;
	
	public Server(int port) {
		mInetSocketAddress = new InetSocketAddress(port);
	}

	public void start() {
		try {
			// We launch the server
			mServerSocket = new ServerSocket(mInetSocketAddress.getPort());

			// We launch the thread that handles the requests
			new Thread(SingleThreadPipe.getInstance(mRequestBuffer)).start();

			System.out.println(Constants.WELCOME_STANDARD);

			// We accept connections and give it to the next stage
			while (true) {

				Socket socket = mServerSocket.accept();

				IOConnection connection = new PipelinedConnection(socket, mRequestBuffer);
				
				try {
					mThreadPool.execute(connection);
				} catch (RejectedExecutionException e) {
					connection.closeConnection();
				}

			}
		} catch (IOException e) {
			System.out.println(Constants.SERVER_CLOSED);
		}

		stop();
	}
	
	/**
	 * Enables to stop the server
	 */
	public void stop() {
		mThreadPool.shutdown();
		
		// We wait until all the clients have got their answers
		while (!mThreadPool.isTerminated()) {
		}
		
		SingleThreadPipe.getInstance(mRequestBuffer).close();

		if (mServerSocket != null) {
			try {
				mServerSocket.close();
			} catch (IOException e) {
			}
		}
	}
}
