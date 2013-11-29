package epfl.lsr.bachelor.project.serverNIO;

import java.nio.channels.Channel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import epfl.lsr.bachelor.project.server.RequestBuffer;
import epfl.lsr.bachelor.project.server.request.Request;
import epfl.lsr.bachelor.project.util.CommandParser;

/**
 * This class parse the data already read
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class NIOReaderWorker implements Runnable {

	private LinkedList<DataRead> mDataToPerformList;
	private CommandParser mCommandParser;

	private Map<Channel, NIOConnection> mChannelConnectionMap;

	private AtomicBoolean mClosed;

	/**
	 * Default constructor
	 * 
	 * @param requestBuffer
	 */
	public NIOReaderWorker(RequestBuffer requestBuffer) {
		mDataToPerformList = new LinkedList<DataRead>();
		mCommandParser = new CommandParser();
		mChannelConnectionMap = new ConcurrentHashMap<Channel, NIOConnection>();
		mClosed = new AtomicBoolean();
	}

	/**
	 * Add a new NIOConnection
	 * 
	 * @param socketChannel
	 *            the socket with which we want to communicate
	 * @param channelID
	 *            the id of the NIOConnection
	 */
	public void addConnection(SocketChannel socketChannel,
			NIOConnection connection) {
		mChannelConnectionMap.put(socketChannel, connection);
	}

	/**
	 * Add a request to be parsed
	 * 
	 * @param dataRead the request and the channel
	 */
	public synchronized void addRequestToPerform(DataRead dataRead) {
		mDataToPerformList.add(dataRead);
		notify();
	}

	private synchronized DataRead take() {
		while (!mClosed.get() && mDataToPerformList.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

		return !mClosed.get() ? mDataToPerformList.removeFirst() : null;
	}

	/**
	 * Remove a channel
	 * 
	 * @param socketChannel the channel
	 */
	public void closeChannel(SocketChannel socketChannel) {
		mChannelConnectionMap.remove(socketChannel);
	}

	/**
	 * Stop this worker
	 */
	public synchronized void stop() {
		mClosed.set(true);
		notify();
	}

	@Override
	public void run() {
		while (!mClosed.get()) {
			DataRead dataRead = take();
			NIOConnection connection = mChannelConnectionMap.get(dataRead
					.getChannel());

			if ((dataRead != null) && (connection != null)) {
				Request request = mCommandParser.parse(dataRead.getRequest());
				connection.addRequestToPerform(request);
			}
		}
	}
}
