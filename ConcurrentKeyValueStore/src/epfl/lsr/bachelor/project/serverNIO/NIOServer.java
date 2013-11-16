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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

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

	private RequestBuffer mRequestBuffer = new RequestBuffer();

	private InetSocketAddress mInetSocketAddress;

	// Enable to know the id of a channel to identify in the worker
	// when we receive a request
	private Map<Channel, Integer> mChannelIDsMap;
	
	// Store the data read in the corresponding buffer
	private Map<Channel, String> mChannelReadMap;

	// Enable to identify the next connection
	private int mNextConnection;

	private NIOConnectionWorker mWorker;

	private Selector mSelector;
	
	private ServerSocketChannel mServerSocketChannel;

	private ByteBuffer mReadByteBuffer;
	
	private AtomicBoolean mClosed;

	/**
	 * Default constructor
	 * 
	 * @param hostname the ip address of the server
	 * @param port the port number of the server
	 * @throws IOException if we can't create the server
	 */
	public NIOServer(String hostname, int port) throws IOException {
		mInetSocketAddress = new InetSocketAddress(hostname, port);

		mChannelIDsMap = new HashMap<Channel, Integer>();
		mChannelReadMap = new HashMap<Channel, String>();

		mNextConnection = 0;
		
		mClosed = new AtomicBoolean();

		mReadByteBuffer = ByteBuffer.allocate(Constants.READ_BUFFER_NIO);
		
		mWorker = new NIOConnectionWorker(mRequestBuffer);
		new Thread(mWorker).start();

		mSelector = initializeSelector();
	}

	/**
	 * Initialize the selector, that is : open the non blocking server socket,
	 * initialize it and register it to accept connection
	 * 
	 * @return the selector
	 * @throws IOException if we can't open the server socket
	 */
	private Selector initializeSelector() throws IOException {
		Selector selector = SelectorProvider.provider().openSelector();

		mServerSocketChannel = ServerSocketChannel.open();
		mServerSocketChannel.configureBlocking(false);

		// Bind the server socket with the IP address and the port number
		mServerSocketChannel.bind(mInetSocketAddress);
		mServerSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		return selector;
	}

	/**
	 * Accept a connection from the client, that is : create a socket when we accept a connection,
	 * initialize it to be non blocking, store it to the worker and register it to read data
	 * 
	 * @param key the server socket
	 * @throws IOException if we can't accept the connection
	 */
	private void accept(SelectionKey key) throws IOException {

		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key
				.channel();

		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);

		mWorker.addConnection(socketChannel, mNextConnection);

		// To identify the channel
		mChannelIDsMap.put(socketChannel, mNextConnection);
		mChannelReadMap.put(socketChannel, "");
		mNextConnection++;

		socketChannel.register(mSelector, SelectionKey.OP_READ);
		
		System.out.println("  -> Started connection with "
				+ mInetSocketAddress.getAddress());
	}

	/**
	 * Read the data and store it until all the command is receive
	 * 
	 * @param key the socket that we want to read
	 * @throws IOException if we can't read
	 */
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

		// The client want to disconnect
		if (byteRead < 0) {
			closeChannel(key);
			return;
		}

		String readData = new String(mReadByteBuffer.array()).substring(0,
				mReadByteBuffer.position());
		
		// Get the datas that are already stored, add the data that we just read
		// and update the data that we have already read
		String dataAlreadyRead = mChannelReadMap.get(socketChannel);
		mChannelReadMap.put(socketChannel, dataAlreadyRead + readData);
		dataAlreadyRead = mChannelReadMap.get(socketChannel);

		// if there are '\n' char, we can perform the request
		while ((dataAlreadyRead != null) && (dataAlreadyRead.contains("\n"))) {
			String[] dataAlreadyReadArray = dataAlreadyRead.split("\n");
			String commandToPerform = dataAlreadyReadArray.length == 0 ? ""
					: dataAlreadyReadArray[0];
			
			// remove the data that we want to perform
			dataAlreadyRead = dataAlreadyRead.replaceFirst("^"
					+ commandToPerform + "\n", "");
			mChannelReadMap.put(socketChannel, dataAlreadyRead);

			if (commandToPerform.equals(Constants.QUIT_COMMAND)) {
				closeChannel(key);
			} else {
				mWorker.addRequestToPerform(commandToPerform,
						mChannelIDsMap.get(socketChannel));
			}
		}
	}

	/**
	 * Close the connection with a client
	 * 
	 * @param key the socket that we want to close
	 * @throws IOException if we can't close
	 */
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

	/**
	 * Start the nio server
	 */
	public void start() {

		System.out.println(Constants.WELCOME_NIO);

		// We launch the thread that handles the requests
		new Thread(SingleThreadPipe.getInstance(mRequestBuffer)).start();

		while (!mClosed.get()) {

			try {
				
				// Wait for an event
				mSelector.select();
				
				if (mClosed.get()) {
					return;
				}

				Iterator<SelectionKey> keyIterator = mSelector.selectedKeys()
						.iterator();

				while (keyIterator.hasNext()) {
					SelectionKey key = keyIterator.next();
					keyIterator.remove();

					// When we cancel, it's possible that the key isn't already removed
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
	
	/**
	 * Stop the NIO server and the worker
	 */
	public void stop() {
		mClosed.set(true);
		
		SingleThreadPipe.getInstance(mRequestBuffer).close();
		mWorker.stopWorker();
		try {
			mSelector.close();
		} catch (IOException e) {
			
		}
		
		for (Channel socketChannel : mChannelIDsMap.keySet()) {
			try {
				socketChannel.close();
			} catch (IOException e) {
			}
		}
		
		try {
			mServerSocketChannel.close();
		} catch (IOException e) {
		}
	}
}
