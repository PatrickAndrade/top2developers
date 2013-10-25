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
 * Encapsulates a non-blocking connection that avoid blocking over a
 * readLine()-call
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class PipelinedConnection extends Connection {

	private PriorityBlockingQueue<Request> mRequestToSend;
	private AtomicBoolean mClosed;
	private AtomicLong mNextRequestToBeSend;
	private AtomicLong mNextRequestID;

	public PipelinedConnection(Socket socket, RequestBuffer requestBuffer) throws IOException {
		super(socket, requestBuffer);
		mRequestToSend = new PriorityBlockingQueue<>(
				Constants.NUMBER_OF_PIPELINED_REQUESTS,
				new RequestsComparator());
		mNextRequestToBeSend = new AtomicLong();
		mNextRequestID = new AtomicLong();
		mClosed = new AtomicBoolean();
	}

	public void run() {
		
		new Thread(new Reader()).start();
		
		try {
			while (!mClosed.get()) {
				
				while ((mRequestToSend.peek() != null)
						&& (mNextRequestToBeSend.get() == mRequestToSend.peek()
								.getID())) {
					mRequestToSend.poll().respond();
					mNextRequestToBeSend.incrementAndGet();
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
	
	public synchronized void closeConnection() throws IOException {
		super.closeConnection();
		mClosed.set(false);
	}

	public void notifyThatRequestIsPerformed(Request request) {
		mRequestToSend.add(request);
	}

	private class Reader implements Runnable {
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
				// System.err.println("  -> Error of connection with " +
				// mSocket.getInetAddress());
				try {
					closeConnection();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
}