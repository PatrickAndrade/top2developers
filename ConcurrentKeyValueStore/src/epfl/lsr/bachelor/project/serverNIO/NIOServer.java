package epfl.lsr.bachelor.project.serverNIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import epfl.lsr.bachelor.project.pipe.SingleThreadPipe;
import epfl.lsr.bachelor.project.server.RequestBuffer;
import epfl.lsr.bachelor.project.server.ServerInterface;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * This class implement the NIO server
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class NIOServer implements ServerInterface {

	private static RequestBuffer mRequestBuffer = new RequestBuffer();

	private InetSocketAddress mInetSocketAddress;

	private Map<Channel, Integer> mChannelIDsMap;
	private Map<Channel, String> mChannelReadMap;

	private int mNextConnection;

	private Worker mWorker;

	private Selector mSelector;

	private ByteBuffer mReadByteBuffer;

	public NIOServer(String hostname, int port) throws IOException {
		mInetSocketAddress = new InetSocketAddress(hostname, port);

		mChannelIDsMap = new HashMap<Channel, Integer>();
		mChannelReadMap = new HashMap<Channel, String>();

		mNextConnection = 0;

		mReadByteBuffer = ByteBuffer.allocate(Constants.READ_BUFFER_NIO);
		mWorker = new Worker(mRequestBuffer);

		new Thread(mWorker).start();

		mSelector = initializeSelector();
	}

	private Selector initializeSelector() throws IOException {
		Selector selector = SelectorProvider.provider().openSelector();

		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);

		// Bind the server socket with the IP address and the port number
		serverSocketChannel.bind(mInetSocketAddress);
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		return selector;
	}

	private void accept(SelectionKey key) throws IOException {

		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key
				.channel();

		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);

		socketChannel.register(mSelector, SelectionKey.OP_READ);

		mWorker.addConnection(socketChannel, mNextConnection);

		// To identify the channel
		mChannelIDsMap.put(socketChannel, mNextConnection);
		mChannelReadMap.put(socketChannel, "");
		mNextConnection++;

		System.out.println("  -> Started connection with "
				+ mInetSocketAddress.getAddress());
	}

	private void read(SelectionKey key) throws IOException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		mReadByteBuffer.clear();

		int byteRead = 0;

		try {
			byteRead = socketChannel.read(mReadByteBuffer);
		} catch (IOException e) {
			// There is a problem with the connection
			closeChannel(key);

			return;
		}

		if (byteRead < 0) {

			closeChannel(key);
			return;
		}

		String command = new String(mReadByteBuffer.array()).substring(0, mReadByteBuffer.position());

		mChannelReadMap.put(socketChannel, mChannelReadMap.get(socketChannel)
				+ command);
		while ((mChannelReadMap.get(socketChannel) != null)
				&& (mChannelReadMap.get(socketChannel).contains("\n"))) {
			String[] commandArray = mChannelReadMap.get(socketChannel).split(
					"\n");
			
			String commandToPerform = commandArray.length == 0 ? "" : commandArray[0];
			mChannelReadMap.put(
					socketChannel,
					mChannelReadMap.get(socketChannel).replaceFirst(
							"^" + commandToPerform + "\n", ""));

			if (commandToPerform.equals(Constants.QUIT_COMMAND)) {
				closeChannel(key);
			} else {
				mWorker.addRequestToPerform(commandToPerform,
						mChannelIDsMap.get(socketChannel));
			}
		}
	}

	public void closeChannel(SelectionKey key) throws IOException {
		System.err.println(" -> Connection with "
				+ mInetSocketAddress.getAddress() + " aborted !");

		SocketChannel socketChannel = (SocketChannel) key.channel();
		mWorker.closeChannel(mChannelIDsMap.get(socketChannel));
		mChannelIDsMap.remove(socketChannel);
		mChannelReadMap.remove(socketChannel);
		socketChannel.close();
		key.cancel();
	}

	public void start() {

		System.out.println(Constants.WELCOME_NIO);

		// We launch the thread that handles the requests
		new Thread(SingleThreadPipe.getInstance(mRequestBuffer)).start();

		while (true) {

			try {

				mSelector.select();

				Iterator<SelectionKey> keyIterator = mSelector.selectedKeys()
						.iterator();

				while (keyIterator.hasNext()) {
					SelectionKey key = keyIterator.next();
					keyIterator.remove();

					if (key.isValid()) {
						if (key.isAcceptable()) {
							accept(key);
						} else if (key.isReadable()) {
							read(key);
						}
					}
				}
			} catch (IOException e) {
			}
		}
	}
}
