package epfl.lsr.bachelor.project.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import epfl.lsr.bachelor.project.pipe.SingleThreadPipe;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * The Telnet server accepting the connections on port 22122
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class Server {

	private static ServerSocket mServerSocket;
	private static ExecutorService mThreadPool = 
			Executors.newFixedThreadPool(Constants.NUMBER_OF_PIPELINED_CONNECTIONS);
	private static RequestBuffer mRequestBuffer = new RequestBuffer();
	
	public static void start() {
		try {
			// We launch the server
			mServerSocket = new ServerSocket(Constants.PORT);

			// We launch the thread that handles the requests
			new Thread(SingleThreadPipe.getInstance(mRequestBuffer)).start();

			System.out.println(Constants.WELCOME);

			// We accept connections and give it to the next stage
			while (true) {

				Socket socket = mServerSocket.accept();

				Connection connection = new Connection(socket, mRequestBuffer);
				mThreadPool.execute(connection);

			}
		} catch (IOException e) {
			System.out.println(Constants.SERVER_CLOSED);
		}

		stop();
	}
	
	public static void stop() {
		mThreadPool.shutdown();

		SingleThreadPipe.getInstance(mRequestBuffer).close();
		
		// We wait until all the clients have got their answers
		while (!mThreadPool.isTerminated()) {
		}

		if (mServerSocket != null) {
			try {
				mServerSocket.close();
			} catch (IOException e) {
			}
		}
	}
}
