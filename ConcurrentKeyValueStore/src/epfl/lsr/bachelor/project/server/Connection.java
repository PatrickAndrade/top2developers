package epfl.lsr.bachelor.project.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;





import epfl.lsr.bachelor.project.server.request.Request;
import epfl.lsr.bachelor.project.util.CommandParser;

/**
 * Encapsulates a connection opened
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class Connection implements Runnable {
	private Socket mSocket;
	private BufferedReader mBufferedReader;
	private DataOutputStream mDataOutputStream;
	private CommandParser mCommandParser;
	private RequestBuffer mRequestBuffer;

	private final static String QUIT_COMMAND = "quit";

	public Connection(Socket socket, RequestBuffer requestBuffer) throws IOException {
		mSocket = socket;
		mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
		mCommandParser = new CommandParser();
		mRequestBuffer = requestBuffer;
		
		System.out.println("  -> Started connection with " + mSocket.getInetAddress());
	}

	public void run() {
		String command = "";

		while (!command.equals(QUIT_COMMAND)) {
			try {
				mDataOutputStream.writeChars("ConcurrentKeyValueStore > ");
				command = mBufferedReader.readLine();
				Request request = mCommandParser.parse(command);
				
				if (request != null) {
					if (!command.equals(QUIT_COMMAND)) {
						request.setConnection(this);
						mRequestBuffer.add(request);
						waitUntilRequestIsPerformed();
						request.respond();
					}
				} else {
					mDataOutputStream.writeChars("Error\n");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			closeConnection(mSocket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void waitUntilRequestIsPerformed() {
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void notifyThatRequestIsPerformed() {
		notify();
	}
	
	public DataOutputStream getDataOutputStream() {
		return mDataOutputStream;
	}

	private void closeConnection(Socket socket) throws IOException {
		socket.close();
		System.err.println("   -> Connection with " + mSocket.getInetAddress() + " aborted !");
	}
}