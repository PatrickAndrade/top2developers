package epfl.lsr.bachelor.project.serverNIO;

import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import epfl.lsr.bachelor.project.serverNIO.NIOServer.NIOWriter;

/**
 * This class encapsulate a list of answer to be send to the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class NIOAnswerBuffer {

	private Map<Channel, LinkedList<ByteBuffer>> mChannelAnswerMap;
	private AtomicBoolean mClosed;

	private NIOWriter mWriter;

	/**
	 * Default constructor
	 * 
	 * @param nioWriter
	 */
	public NIOAnswerBuffer(NIOWriter nioWriter) {
		mChannelAnswerMap = new HashMap<Channel, LinkedList<ByteBuffer>>();
		mClosed = new AtomicBoolean();
		mWriter = nioWriter;
	}

	/**
	 * Add an answer to be send when we can
	 * 
	 * @param channel
	 *            the channel of the socket that we want to answer
	 * @param answer
	 *            the answer to be send
	 */
	public synchronized void add(Channel channel, String answer) {
		LinkedList<ByteBuffer> byteBufferList = mChannelAnswerMap.get(channel);
		if (mChannelAnswerMap.get(channel) == null) {
			byteBufferList = new LinkedList<ByteBuffer>();
			mChannelAnswerMap.put(channel, byteBufferList);
		}

		byteBufferList.add(ByteBuffer.wrap(answer.getBytes()));
		mWriter.send(channel);
		notifyAll();
	}

	/**
	 * Take the next answer to be send
	 * 
	 * @param channel
	 *            the channel of the socket that we want to answer
	 * 
	 * @return the answer to be send or <code>null</code> if we close
	 */
	public synchronized ByteBuffer get(Channel channel) {

		LinkedList<ByteBuffer> byteBufferList = mChannelAnswerMap.get(channel);

		return (!mClosed.get() && (byteBufferList != null) && (!byteBufferList
				.isEmpty())) ? byteBufferList.getFirst() : null;
		// return mRequestList.removeFirst();
	}

	/**
	 * Remove the channel of the buffer (used when the client disconnect)
	 * 
	 * @param channel
	 *            the channel that we want to remove
	 */
	public synchronized void remove(Channel channel) {
		mChannelAnswerMap.remove(channel);
	}

	public synchronized void removeAnswer(Channel channel) {
		LinkedList<ByteBuffer> byteBufferList = mChannelAnswerMap.get(channel);

		if ((byteBufferList != null) && !byteBufferList.isEmpty()) {
			byteBufferList.removeFirst();
		}
	}

	/**
	 * Enables to notify all the threads that are currently waiting for answers.
	 * This method also make that all waiting-take() calls will return
	 * <code>null</code>
	 */
	public synchronized void notifyAllThreadsToStopWaiting() {
		mClosed.set(true);
		notifyAll();
	}
}
