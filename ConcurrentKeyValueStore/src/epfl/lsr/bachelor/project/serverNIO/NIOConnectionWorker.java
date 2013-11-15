package epfl.lsr.bachelor.project.serverNIO;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import epfl.lsr.bachelor.project.server.RequestBuffer;
import epfl.lsr.bachelor.project.server.request.Request;
import epfl.lsr.bachelor.project.util.CommandParser;
import epfl.lsr.bachelor.project.connection.ConnectionInterface;

/**
 * The worker class that perform the request and send the answer to the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class NIOConnectionWorker implements Runnable, ConnectionInterface {

	// Enable to retrieve the NIOConnection when we want to answer a request
	private Map<Integer, NIOConnection> mIDConnectionMap;
	
	// Enable to know when a NIOConnection can send an answer
	private ConcurrentLinkedQueue<Integer> mReadyChannelQueue;
	
	private CommandParser mCommandParser;
	private RequestBuffer mRequestBuffer;

	/**
	 * Default constructor
	 * 
	 * @param requestBuffer the shared request buffer
	 */
	public NIOConnectionWorker(RequestBuffer requestBuffer) {
		mIDConnectionMap = new HashMap<Integer, NIOConnection>();
		mCommandParser = new CommandParser();
		mRequestBuffer = requestBuffer;
		mReadyChannelQueue = new ConcurrentLinkedQueue<Integer>();
	}

	/**
	 * Add a request, to the good NIOConnection, to be parsed and perform
	 * 
	 * @param command the string request
	 * @param channelID the id of the NIOConnection
	 */
	public void addRequestToPerform(String command, Integer channelID) {
		Request request = mCommandParser.parse(command);
		request.setChannelID(channelID);
		request.setWorker(this);
		mIDConnectionMap.get(channelID).addRequestToPerform(request, mRequestBuffer);
	}

	/**
	 * Add a new NIOConnection
	 * 
	 * @param socketChannel the socket with which we want to communicate
	 * @param channelID the id of the NIOConnection
	 */
	public void addConnection(SocketChannel socketChannel, int channelID) {
		mIDConnectionMap.put(channelID, new NIOConnection(socketChannel, channelID, this));
	}

	/**
	 * Close a connection with a client
	 * 
	 * @param channelID the id of the NIOConnection
	 */
	public void closeChannel(int channelID) {
		mIDConnectionMap.remove(channelID);
	}

	/**
	 * Notify the worker that he can now send an answer
	 * 
	 * @param request the request that he can answer
	 */
	@Override
	public void notifyThatRequestIsPerformed(Request request) {
		int channelID = request.getChannelID();
		NIOConnection nioConnection = mIDConnectionMap.get(channelID);

		nioConnection.addRequestToSend(request);

		if (nioConnection.isReady()) {
			mReadyChannelQueue.add(channelID);
			notifyToSendAnAnswer();
		}
	}

	@Override
	public void run() {
		while (true) {
			while (mReadyChannelQueue.isEmpty()) {
				waitToSendAnAnswer();
			}

			mIDConnectionMap.get(mReadyChannelQueue.poll()).sendAnswers();
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
