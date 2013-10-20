package epfl.lsr.bachelor.project.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import epfl.lsr.bachelor.project.server.request.Request;
import epfl.lsr.bachelor.project.util.CommandParser;
import epfl.lsr.bachelor.project.util.Constants;

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

	public Connection(Socket socket, RequestBuffer requestBuffer) throws IOException {
		mSocket = socket;
		mBufferedReader = new BufferedReader(new InputStreamReader(
				mSocket.getInputStream()));
		mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
		mCommandParser = new CommandParser();
		mRequestBuffer = requestBuffer;

		System.out.println("  -> Started connection with "
				+ mSocket.getInetAddress());
	}

	public void run() {
		String command = Constants.EMPTY_STRING;

		try {
			while (command != null && !command.equals(Constants.QUIT_COMMAND)) {
				mDataOutputStream.writeBytes(Constants.PROGRAMM_NAME);

				// It gets the command asked by the client
				command = mBufferedReader.readLine();

				if (command != null && !command.equals(Constants.QUIT_COMMAND)) {
					// We parse the command to encapsulate it in a more specific
					// request
					Request request = mCommandParser.parse(command);
					request.setConnection(this);

					// If the request can be performed, we put it in the buffer
					if (request.canBePerformed()) {
						mRequestBuffer.add(request);
						waitUntilRequestIsPerformed();
					}

					// If the answer to the client is not empty we respond him
					if (!request.isMessageEmpty()) {
						request.respond();
					} else {
						mDataOutputStream.writeBytes(Constants.EMPTY_STRING);
						mDataOutputStream.flush();
					}
				}
			}
		} catch (IOException e) {
			//System.err.println("  -> Error of connection with " + mSocket.getInetAddress());
		}
		
		try {
			closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Enables to call a wait
	 */
	public synchronized void waitUntilRequestIsPerformed() {
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Enables to notify a previous waitUntilRequestIsPerformed()-call
	 */
	public synchronized void notifyThatRequestIsPerformed() {
		notify();
	}

	/**
	 * Enables to get the {@link DataOutputStream} of this connection
	 * 
	 * @return the {@link DataOutputStream} of this connection
	 */
	public DataOutputStream getDataOutputStream() {
		return mDataOutputStream;
	}

	/**
	 * Enables to close properly the connection (ie it closes the socket)
	 * 
	 * @param socket
	 * @throws IOException
	 */
	public void closeConnection() throws IOException {
		if (mSocket != null) {
			mSocket.close();
			System.err.println(" -> Connection with " + mSocket.getInetAddress()
					+ " aborted !");
		}
	}
}