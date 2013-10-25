package epfl.lsr.bachelor.project.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import epfl.lsr.bachelor.project.server.RequestBuffer;
import epfl.lsr.bachelor.project.server.request.ErrRequest;
import epfl.lsr.bachelor.project.server.request.Request;
import epfl.lsr.bachelor.project.server.request.RequestsComparator;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * Encapsulates a pipelined-connection that allow pipelined
 * requests performing
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class PipelinedConnection extends Connection {

	private PriorityBlockingQueue<Request> mRequestToSend;
	private AtomicBoolean mClosed;
	private AtomicLong mNextRequestToBeSend;
	private AtomicLong mNextRequestID;

    /**
     * Default constructor
     * 
     * @param socket the socket related to the connection
     * @param requestBuffer the buffer of requests
     * @throws IOException
     */
	public PipelinedConnection(Socket socket, RequestBuffer requestBuffer) throws IOException {
		super(socket, requestBuffer);
		mRequestToSend = new PriorityBlockingQueue<>(
				Constants.NUMBER_OF_PIPELINED_REQUESTS,
				new RequestsComparator());
		mNextRequestToBeSend = new AtomicLong();
		mNextRequestID = new AtomicLong();
		mClosed = new AtomicBoolean();
	}

	@Override
	public void run() {
		
		// Create the reader
		new Thread(new Reader()).start();
		
		try {
			while (!mClosed.get()) {
				
				// We first check if the queue has some element and if so,
				// we take it and ensure that it's the next request to be answered
				while ((mRequestToSend.peek() != null)
						&& (mNextRequestToBeSend.get() == mRequestToSend.peek()
								.getID())) {
					mRequestToSend.poll().respond();
					mNextRequestToBeSend.incrementAndGet();
				}
			}
		} catch (IOException e) {
		}

		try {
			closeConnection();
		} catch (IOException e) {
		}
	}
	
	@Override
	public synchronized void closeConnection() throws IOException {
		super.closeConnection();
		mClosed.set(true);
	}

	@Override
	public void notifyThatRequestIsPerformed(Request request) {
		mRequestToSend.add(request);
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
						request.setID(mNextRequestID.get());

						// If the request can be performed, we put it in the
						// buffer
						if (request.canBePerformed()) {
							getRequestBuffer().add(request);
						} else {
							if (request.isMessageEmpty()) {
								request = new ErrRequest(Constants.EMPTY_STRING);
								request.setConnection(PipelinedConnection.this);
								request.setID(mNextRequestID.get());
							}
							mRequestToSend.add(request);
						}

						mNextRequestID.incrementAndGet();
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