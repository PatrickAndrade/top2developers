package epfl.lsr.bachelor.project.serverNIO;

import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This class encapsulate a list of answer to be send to the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class NIOAnswerBuffer {

	private Map<Channel, LinkedList<ByteBuffer>> mChannelAnswerMap;

	/**
	 * Default constructor
	 * 
	 */
	public NIOAnswerBuffer() {
		mChannelAnswerMap = new HashMap<Channel, LinkedList<ByteBuffer>>();
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

		return ((byteBufferList != null) && (!byteBufferList.isEmpty())) ? byteBufferList
				.getFirst() : null;
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

	/**
	 * Remove an answer that has been send
	 * 
	 * @param channel
	 *            the channel to identify the list on which we want to remove
	 *            the first request
	 */
	public synchronized void removeAnswer(Channel channel) {
		LinkedList<ByteBuffer> byteBufferList = mChannelAnswerMap.get(channel);

		if ((byteBufferList != null) && !byteBufferList.isEmpty()) {
			byteBufferList.removeFirst();
		}
	}

	/**
	 * Check if the list of answer is empty for a channel
	 * 
	 * @param socketChannel
	 *            the channel
	 * @return <code>true</code> if the list is empty or if it's
	 *         <code>null</code>
	 */
	public synchronized boolean isEmpty(SocketChannel socketChannel) {
		LinkedList<ByteBuffer> byteBufferList = mChannelAnswerMap
				.get(socketChannel);
		return (byteBufferList == null) || byteBufferList.isEmpty();
	}
}
