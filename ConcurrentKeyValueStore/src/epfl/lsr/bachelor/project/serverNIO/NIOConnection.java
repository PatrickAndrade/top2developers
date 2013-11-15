package epfl.lsr.bachelor.project.serverNIO;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.PriorityBlockingQueue;

import epfl.lsr.bachelor.project.connection.PipelinedConnection;
import epfl.lsr.bachelor.project.connection.PriorityWaitingRequestQueue;
import epfl.lsr.bachelor.project.server.RequestBuffer;
import epfl.lsr.bachelor.project.server.request.ErrRequest;
import epfl.lsr.bachelor.project.server.request.Request;
import epfl.lsr.bachelor.project.server.request.RequestsComparator;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class NIOConnection {
	private SocketChannel mSocketChannel;
	private PriorityBlockingQueue<Request> mToSend;
	private int mId;
	private long mNextRequestID = 0;
	private long mNextRequestToSend = 0;
	private Worker mWorker;

	public NIOConnection(SocketChannel socketChannel, int id, Worker worker) {
		mSocketChannel = socketChannel;
		mWorker = worker;
		mToSend = new PriorityBlockingQueue<>(
				Constants.NUMBER_OF_PIPELINED_REQUESTS,
				new RequestsComparator());
		mId = id;
	}

	public void addRequestToPerform(Request request, RequestBuffer requestBuffer) {

		request.setID(mNextRequestID);
		request.setChannel(mSocketChannel);

		// If the request can be performed, we put it in the
		// buffer
		if (request.canBePerformed()) {
			requestBuffer.add(request);
		} else {

			// The only cases in which we achieve this part is
			// when the request is
			// either an empty request or an error request
			if (request.isMessageEmpty()) {
				request = new ErrRequest(Constants.EMPTY_STRING);
				request.setID(mNextRequestID);
				request.setChannelID(mId);
				request.setChannel(mSocketChannel);
				request.setWorker(mWorker);
			}

			mWorker.notifyThatRequestIsPerformed(request);
		}

		mNextRequestID++;
	}

	public synchronized void addRequestToSend(Request request) {
		mToSend.add(request);
	}

	public synchronized void sendAnswers() {
		try {
			while (isReady()) {
				mToSend.poll().respond();
				mNextRequestToSend++;
			}
		} catch (IOException e) {
			// Disconnected
		}
	}

	public synchronized boolean isReady() {
		return (mToSend.peek() != null) && (mNextRequestToSend == mToSend.peek().getID());
	}
}
