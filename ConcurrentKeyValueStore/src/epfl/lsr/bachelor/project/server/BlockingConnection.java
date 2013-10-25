package epfl.lsr.bachelor.project.server;

import java.io.IOException;
import java.net.Socket;

import epfl.lsr.bachelor.project.server.request.Request;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * Encapsulates a connection opened
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class BlockingConnection extends Connection {

	public BlockingConnection(Socket socket, RequestBuffer requestBuffer) throws IOException {
		super(socket, requestBuffer);
	}

	public void run() {
		String command = Constants.EMPTY_STRING;

		try {
			while (command != null && !command.equals(Constants.QUIT_COMMAND)) {
			    getDataOutputStream().writeBytes(Constants.PROGRAMM_NAME);

				// It gets the command asked by the client
				command = getBufferedReader().readLine();

				if (command != null && !command.equals(Constants.QUIT_COMMAND)) {
					// We parse the command to encapsulate it in a more specific
					// request
					Request request = getCommandParser().parse(command);
					request.setConnection(this);

					// If the request can be performed, we put it in the buffer
					if (request.canBePerformed()) {
						addRequestAndWait(request);
					}

					// If the answer to the client is not empty we respond him
					if (!request.isMessageEmpty()) {
						request.respond();
					} else {
					    getDataOutputStream().writeBytes(Constants.EMPTY_STRING + "\n");
					    getDataOutputStream().flush();
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
	public synchronized void addRequestAndWait(Request request) {
		try {
		    getRequestBuffer().add(request);
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

}