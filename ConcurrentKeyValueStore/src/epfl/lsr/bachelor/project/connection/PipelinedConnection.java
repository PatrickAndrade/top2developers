package epfl.lsr.bachelor.project.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

import epfl.lsr.bachelor.project.server.RequestBuffer;
import epfl.lsr.bachelor.project.server.request.ErrRequest;
import epfl.lsr.bachelor.project.server.request.Request;
import epfl.lsr.bachelor.project.server.request.RequestsComparator;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * Encapsulates a pipelined-connection that allow pipelined requests performing
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class PipelinedConnection extends IOConnection {

	private PriorityWaitingRequestQueue<Request> mPriorityBlockingGenericQueue;
	private AtomicBoolean mClosed;
	private long mNextRequestID;

	/**
	 * Default constructor
	 * 
	 * @param socket
	 *            the socket related to the connection
	 * @param requestBuffer
	 *            the buffer of requests
	 * @throws IOException
	 */
	public PipelinedConnection(Socket socket, RequestBuffer requestBuffer) throws IOException {
		super(socket, requestBuffer);

		mPriorityBlockingGenericQueue = new PriorityWaitingRequestQueue<>(
				Constants.NUMBER_OF_PIPELINED_REQUESTS,
				new RequestsComparator());
		mNextRequestID = 0;
		mClosed = new AtomicBoolean();
	}

	@Override
	public void run() {

		// Create the reader
		new Thread(new Reader()).start();

		// The writer code
		try {
			while (!mClosed.get()) {
				Request requestToSend = mPriorityBlockingGenericQueue.poll();
				
				if (requestToSend != null) {
					requestToSend.respond();
				}
			}
		} catch (IOException e) {
		}
	}

	@Override
	public synchronized void closeConnection() throws IOException {
		super.closeConnection();
		mClosed.set(true);
		mPriorityBlockingGenericQueue.close();
	}

	@Override
	public void notifyThatRequestIsPerformed(Request request) {
		mPriorityBlockingGenericQueue.add(request);
	}

	private class Reader implements Runnable {

		@Override
		public void run() {
			try {
				String command = Constants.EMPTY_STRING;
				while (command != null
						&& !command.equals(Constants.QUIT_COMMAND)) {
					// It gets the command asked by the client
					command = getBufferedReader().readLine();

					if (command != null
							&& !command.equals(Constants.QUIT_COMMAND)) {
						// We parse the command to encapsulate it in a more
						// specific
						// request
						Request request = getCommandParser().parse(command);
						request.setConnection(PipelinedConnection.this);
						request.setID(mNextRequestID);

						// If the request can be performed, we put it in the
						// buffer
						if (request.canBePerformed()) {
							getRequestBuffer().add(request);
						} else {

							// The only cases in which we achieve this part is
							// when the request is
							// either an empty request or an error request
							if (request.isMessageEmpty()) {
								request = new ErrRequest(Constants.EMPTY_STRING);
								request.setConnection(PipelinedConnection.this);
								request.setID(mNextRequestID);
							}

							mPriorityBlockingGenericQueue.add(request);
						}

						mNextRequestID++;
					}
				}
			} catch (IOException e) {
			}

			try {
				closeConnection();
			} catch (IOException e1) {
			}
		}
	}
}