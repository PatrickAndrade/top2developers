package epfl.lsr.bachelor.project.serverNIO;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import epfl.lsr.bachelor.project.server.RequestBuffer;
import epfl.lsr.bachelor.project.server.request.Request;
import epfl.lsr.bachelor.project.util.CommandParser;
import epfl.lsr.bachelor.project.connection.Connection;

/**
 * The worker class that perform the request and send the answer to the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class Worker implements Runnable, Connection {

	private Map<Integer, NIOConnection> mIDConnectionMap;
	private ConcurrentLinkedQueue<Integer> mReadyChannelQueue;
	private CommandParser mCommandParser;
	private RequestBuffer mRequestBuffer;

	public Worker(RequestBuffer requestBuffer) {
		mIDConnectionMap = new HashMap<Integer, NIOConnection>();
		mCommandParser = new CommandParser();
		mRequestBuffer = requestBuffer;
		mReadyChannelQueue = new ConcurrentLinkedQueue<Integer>();
	}

	public void addRequestToPerform(String command, Integer id) {
		Request request = mCommandParser.parse(command);
		request.setChannelID(id);
		request.setWorker(this);
		mIDConnectionMap.get(id).addRequestToPerform(request, mRequestBuffer);
	}

	public void addConnection(SocketChannel socketChannel, int id) {
		mIDConnectionMap.put(id, new NIOConnection(socketChannel, id, this));
	}

	public void closeChannel(int channelID) {
		mIDConnectionMap.remove(channelID);
	}

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
