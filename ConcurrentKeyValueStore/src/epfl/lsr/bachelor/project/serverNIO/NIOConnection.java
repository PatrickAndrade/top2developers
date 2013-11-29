package epfl.lsr.bachelor.project.serverNIO;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.PriorityBlockingQueue;

import epfl.lsr.bachelor.project.server.RequestBuffer;
import epfl.lsr.bachelor.project.server.request.ErrRequest;
import epfl.lsr.bachelor.project.server.request.Request;
import epfl.lsr.bachelor.project.server.request.RequestsComparator;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * This class is a encapsulate a connection when we use NIO
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class NIOConnection {

	private SocketChannel mSocketChannel;
	private PriorityBlockingQueue<Request> mToSend;
	private NIOWriterWorker mWriterWorker;
	
	private RequestBuffer mRequestBuffer;

	private long mNextRequestID = 0;
	private long mNextRequestToSend = 0;

	/**
	 * Default constructor
	 * 
	 * @param socketChannel
	 *            the socket with which we want to communicate
	 * @param channelID
	 *            the id of the NIOConnection
	 * @param worker
	 *            the worker that send and perform the request
	 */
	public NIOConnection(SocketChannel socketChannel, RequestBuffer requestBuffer) {
		mSocketChannel = socketChannel;
		
		mToSend = new PriorityBlockingQueue<>(
				Constants.NUMBER_OF_PIPELINED_REQUESTS,
				new RequestsComparator());
		mRequestBuffer = requestBuffer;
	}

	/**
	 * Add a request to the request buffer if we can perform it, otherwise
	 * notify the worker that he can send the answer of the request
	 * 
	 * @param request
	 *            the request to be performed
	 * @param requestBuffer
	 *            the request buffer in which we add the request to be performed
	 * @param mAnswerBuffer 
	 */
	public void addRequestToPerform(Request request) {

		// If the request can be performed, we put it in the
		// buffer
		if (request.canBePerformed()) {
			request.setID(mNextRequestID);
			request.setChannel(mSocketChannel);
			request.setWorker(mWriterWorker);
			mRequestBuffer.add(request);
		} else {

			// The only cases in which we achieve this part is
			// when the request is
			// either an empty request or an error request
			if (request.isMessageEmpty()) {
				request = new ErrRequest(Constants.EMPTY_STRING);
			}
			
			request.setID(mNextRequestID);
			request.setChannel(mSocketChannel);
			request.setWorker(mWriterWorker);

			mWriterWorker.notifyThatRequestIsPerformed(request);
		}

		mNextRequestID++;
	}

	/**
	 * Add a request to be send
	 * 
	 * @param request
	 *            the request to be send
	 */
	public synchronized void addRequestToSend(Request request) {
		mToSend.add(request);
	}

	/**
	 * Send the answers already performed in the order of when they are received
	 */
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

	/**
	 * Enable to know if we can send an answer
	 * 
	 * @return <code>true</code> if we can send an answer
	 */
	public synchronized boolean isReady() {
		Request toSend = mToSend.peek();
		return (toSend != null)
				&& (mNextRequestToSend == toSend.getID());
	}
	
	/**
	 * Set the writer that send answer
	 * 
	 * @param writerWorker the writer
	 */
	public void setWriterWorker(NIOWriterWorker writerWorker) {
		mWriterWorker = writerWorker;
	}
}
