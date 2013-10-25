package epfl.lsr.bachelor.project.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.PriorityBlockingQueue;

import epfl.lsr.bachelor.project.server.RequestBuffer;
import epfl.lsr.bachelor.project.server.request.ErrRequest;
import epfl.lsr.bachelor.project.server.request.Request;
import epfl.lsr.bachelor.project.server.request.RequestsComparator;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * Encapsulates a non-blocking connection that avoid blocking over a
 * readLine()-call
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class NonBlockingConnection extends Connection {

	private PriorityBlockingQueue<Request> mRequestToSend;
	private long mNextRequestToBeSend;
	private long mNextRequestID;

	public NonBlockingConnection(Socket socket, RequestBuffer requestBuffer)
		throws IOException {
		super(socket, requestBuffer);
		mRequestToSend = new PriorityBlockingQueue<>(
				Constants.NUMBER_OF_PIPELINED_REQUESTS,
				new RequestsComparator());
		mNextRequestToBeSend = 0;
		mNextRequestID = 0;
	}

	public void run() {
		String command = Constants.EMPTY_STRING;
		
		try {
			while (command != null && !command.equals(Constants.QUIT_COMMAND)) {

				if (getBufferedReader().ready()) {

					// It gets the command asked by the client
					command = getBufferedReader().readLine();

					if (command != null
							&& !command.equals(Constants.QUIT_COMMAND)) {
						// We parse the command to encapsulate it in a more
						// specific
						// request
						Request request = getCommandParser().parse(command);
						request.setConnection(this);
						request.setID(mNextRequestID);

						// If the request can be performed, we put it in the
						// buffer
						if (request.canBePerformed()) {
							getRequestBuffer().add(request);
						} else {
							if (request.isMessageEmpty()) {
								request = new ErrRequest(Constants.EMPTY_STRING);
								request.setConnection(this);
								request.setID(mNextRequestID);
							}
							mRequestToSend.add(request);
						}

						mNextRequestID++;
					}
				}

				while (!mRequestToSend.isEmpty()
						&& (mNextRequestToBeSend == mRequestToSend.peek()
								.getID())) {
					mRequestToSend.poll().respond();
					mNextRequestToBeSend++;
				}
			}
		} catch (IOException e) {
			// System.err.println("  -> Error of connection with " +
			// mSocket.getInetAddress());
		}

		try {
			closeConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void notifyThatRequestIsPerformed(Request request) {
		mRequestToSend.add(request);
	}
}