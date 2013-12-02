package epfl.lsr.bachelor.project.serverNIO;

import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import epfl.lsr.bachelor.project.connection.ConnectionInterface;
import epfl.lsr.bachelor.project.server.request.Request;
import epfl.lsr.bachelor.project.serverNIO.NIOServer.NIOWriter;

/**
 * The worker class that perform the request and send the answer to the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class NIOWriterWorker implements Runnable, ConnectionInterface {

	// Enable to retrieve the NIOConnection when we want to answer a request
	private Map<Channel, NIOConnection> mChannelConnectionMap;

	// Enable to know when a NIOConnection can send an answer
	private LinkedList<Channel> mReadyChannelQueue;

	private AtomicBoolean mClosed;

	private NIOAnswerBuffer mAnswerBuffer;

	private NIOWriter mWriter;

	/**
	 * Default constructor
	 * 
	 * @param answerBuffer the answer buffer
	 * @param writer the nio writer
	 */
	public NIOWriterWorker(NIOAnswerBuffer answerBuffer, NIOWriter writer) {
		mChannelConnectionMap = new ConcurrentHashMap<Channel, NIOConnection>();
		mAnswerBuffer = answerBuffer;
		mReadyChannelQueue = new LinkedList<Channel>();
		mClosed = new AtomicBoolean();
		mWriter = writer;
	}

	/**
	 * Add a new NIOConnection
	 * 
	 * @param socketChannel
	 *            the socket with which we want to communicate
	 * @param connection
	 *            the nio connection
	 */
	public void addConnection(SocketChannel socketChannel, NIOConnection connection) {
		connection.setWriterWorker(this);
		mChannelConnectionMap.put(socketChannel, connection);
	}

	/**
	 * Close a connection with a client
	 * 
	 * @param channel
	 *            the channel that used in a NIOConnection
	 */
	public void closeChannel(Channel channel) {
		mChannelConnectionMap.remove(channel);
	}

	/**
	 * Notify the worker that he can now send an answer
	 * 
	 * @param request
	 *            the request that he can answer
	 */
	@Override
	public void notifyThatRequestIsPerformed(Request request) {
		request.setNIOAnswerBuffer(mAnswerBuffer);
		NIOConnection nioConnection = mChannelConnectionMap.get(request
				.getChannel());

		// Disconnected
		if (nioConnection == null) {
			return;
		}

		nioConnection.addRequestToSend(request);

		if (nioConnection.isReady()) {
			addNextConnection(request.getChannel());
		}
	}

	/**
	 * Stop this worker
	 */
	public void stopWorker() {
		mClosed.set(true);
		notifyToSendAnAnswer();
	}
	
	private synchronized void addNextConnection(Channel channel) {
		mReadyChannelQueue.add(channel);
		notifyToSendAnAnswer();
	}

	private synchronized NIOConnection takeNextConnection() {
		while (!mClosed.get() && mReadyChannelQueue.isEmpty()) {
			waitToSendAnAnswer();
		}

		return !mReadyChannelQueue.isEmpty() ? mChannelConnectionMap
				.get(mReadyChannelQueue.poll()) : null;
	}

	@Override
	public void run() {
		while (!mClosed.get()) {

			NIOConnection connection = takeNextConnection();

			// If the client disconnect when the request is performed
			if (connection != null) {
				connection.sendAnswers();
				mWriter.send(connection.getChannel());
			}
		}
	}

	private synchronized void waitToSendAnAnswer() {
		try {
			wait();
		} catch (InterruptedException e) {
		}
	}

	private synchronized void notifyToSendAnAnswer() {
		notify();
	}
}
