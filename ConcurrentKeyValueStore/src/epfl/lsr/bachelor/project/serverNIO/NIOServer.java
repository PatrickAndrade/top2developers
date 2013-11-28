package epfl.lsr.bachelor.project.serverNIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import epfl.lsr.bachelor.project.pipe.WorkerPipeInterface;
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

	private NIOAnswerBuffer mAnswerBuffer;

	private WorkerPipeInterface mWorkers;

	private InetSocketAddress mInetSocketAddress;

	// Enable to know the id of a channel to identify in the worker
	// when we receive a request
	private Map<Channel, Integer> mChannelIDsMap;

	// Store the data read in the corresponding buffer
	private Map<Channel, String> mChannelReadMap;

	// Enable to identify the next connection
	private int mNextConnection;

	private NIOConnectionWorker mNIOConnectionWorker;

	private Selector mSelector;

	private ServerSocketChannel mServerSocketChannel;

	private ByteBuffer mReadByteBuffer;

	private AtomicBoolean mClosed;

	private NIOReader mReader;

	private NIOWriter mWriter;

	/**
	 * Default constructor
	 * 
	 * @param hostname
	 *            the ip address of the server
	 * @param port
	 *            the port number of the server
	 * @param worker
	 * @param requestBuffer
	 * @throws IOException
	 *             if we can't create the server
	 */
	public NIOServer(String hostname, int port, RequestBuffer requestBuffer,
			WorkerPipeInterface worker) throws IOException {
		mInetSocketAddress = new InetSocketAddress(hostname, port);
		mRequestBuffer = requestBuffer;
		mWorkers = worker;

		mChannelIDsMap = new ConcurrentHashMap<Channel, Integer>();
		mChannelReadMap = new ConcurrentHashMap<Channel, String>();

		mNextConnection = 0;

		mClosed = new AtomicBoolean();

		mReadByteBuffer = ByteBuffer.allocate(Constants.READ_BUFFER_NIO);

		mReader = new NIOReader();
		new Thread(mReader).start();

		mWriter = new NIOWriter();
		new Thread(mWriter).start();

		mNIOConnectionWorker = new NIOConnectionWorker(mRequestBuffer,
				mAnswerBuffer, mWriter);
		new Thread(mNIOConnectionWorker).start();

		mSelector = initializeSelector();
	}

	/**
	 * Initialize the selector, that is : open the non blocking server socket,
	 * initialize it and register it to accept connection
	 * 
	 * @return the selector
	 * @throws IOException
	 *             if we can't open the server socket
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
	 * Accept a connection from the client, that is : create a socket when we
	 * accept a connection, initialize it to be non blocking, store it to the
	 * worker and register it to read data
	 * 
	 * @param key
	 *            the server socket
	 * @throws IOException
	 *             if we can't accept the connection
	 */
	private void accept(SelectionKey key) throws IOException {

		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key
				.channel();

		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);

		mNIOConnectionWorker.addConnection(socketChannel, mNextConnection);

		// To identify the channel
		mChannelIDsMap.put(socketChannel, mNextConnection);
		mChannelReadMap.put(socketChannel, "");
		mNextConnection++;

		mReader.register(socketChannel);
		mWriter.register(socketChannel);

		System.out.println("  -> Started connection with "
				+ mInetSocketAddress.getAddress());
	}

	/**
	 * Close the connection with a client
	 * 
	 * @param key
	 *            the socket that we want to close
	 * @throws IOException
	 *             if we can't close
	 */
	public void closeChannel(SelectionKey key) throws IOException {
		
		// We can cancel the key when we write, so we finish to write
		synchronized (key) {

			System.err.println(" -> Connection with "
					+ mInetSocketAddress.getAddress() + " aborted !");

			SocketChannel socketChannel = (SocketChannel) key.channel();
			mNIOConnectionWorker
					.closeChannel(mChannelIDsMap.get(socketChannel));
			mChannelIDsMap.remove(socketChannel);
			mChannelReadMap.remove(socketChannel);
			mAnswerBuffer.remove(socketChannel);
			socketChannel.close();
			key.cancel();
		}
	}

	/**
	 * Start the nio server
	 */
	public void start() {

		System.out.println(Constants.WELCOME_NIO);

		// We launch the thread that handles the requests
		mWorkers.start();

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

					// When we cancel, it's possible that the key isn't already
					// removed
					if (key.isValid()) {
						if (key.isAcceptable()) {
							accept(key);
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

		mWorkers.close();
		mNIOConnectionWorker.stopWorker();
		try {
			mSelector.close();
		} catch (IOException e) {

		}

		try {
			mReader.stop();
		} catch (IOException e1) {

		}

		try {
			mWriter.stop();
		} catch (IOException e1) {

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

	/**
	 * Class that read the data from the sockets
	 * 
	 * @author Gregory Maitre
	 * 
	 */
	private class NIOReader implements Runnable {

		private Selector mReaderSelector;

		/**
		 * Default constructor
		 * 
		 * @throws IOException
		 */
		public NIOReader() throws IOException {
			mReaderSelector = SelectorProvider.provider().openSelector();
		}

		/**
		 * Read the data and store it until all the command is receive
		 * 
		 * @param key
		 *            the socket that we want to read
		 * @throws IOException
		 *             if we can't read
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
					byteRead);
						
			// Get the datas that are already stored, add the data that we just
			// read
			// and update the data that we have already read
			String dataAlreadyRead = mChannelReadMap.get(socketChannel);
			mChannelReadMap.put(socketChannel, dataAlreadyRead.concat(readData));
			dataAlreadyRead = mChannelReadMap.get(socketChannel);
			
			// if there are '\n' char, we can perform the request
			if ((dataAlreadyRead != null) && (dataAlreadyRead.contains("\n"))) {
				String[] dataAlreadyReadArray = dataAlreadyRead.split("\n");

				if (dataAlreadyReadArray.length == 0) {
					mNIOConnectionWorker.addRequestToPerform("",
							mChannelIDsMap.get(socketChannel));
				} else {

					int commandToPerformInArraySize = (dataAlreadyRead
							.charAt(dataAlreadyRead.length() - 1) == '\n') ? dataAlreadyReadArray.length
							: (dataAlreadyReadArray.length - 1);

					for (int i = 0; i < commandToPerformInArraySize; i++) {
						if (dataAlreadyReadArray[i]
								.equals(Constants.QUIT_COMMAND)) {
							closeChannel(key);
							return;
						} else {
							// If we use telnet, we must remove the last
							// caracter
							if (dataAlreadyReadArray[i]
									.charAt(dataAlreadyReadArray[i].length() - 1) == Constants.NIO_TELNET_LAST_CHAR) {
								dataAlreadyReadArray[i] = dataAlreadyReadArray[i]
										.substring(0, dataAlreadyReadArray[i]
												.length() - 1);
							}

							mNIOConnectionWorker.addRequestToPerform(
									dataAlreadyReadArray[i],
									mChannelIDsMap.get(socketChannel));
						}
					}

					if (dataAlreadyReadArray.length == commandToPerformInArraySize) {
						mChannelReadMap.put(socketChannel, "");
					} else {
						mChannelReadMap
								.put(socketChannel,
										dataAlreadyReadArray[commandToPerformInArraySize]);
					}
				}
			}
		}

		/**
		 * Register the channel with the selector
		 * 
		 * @param socketChannel
		 * @throws ClosedChannelException
		 */
		public synchronized void register(SocketChannel socketChannel) throws ClosedChannelException {
			mReaderSelector.wakeup();
			socketChannel.register(mReaderSelector, SelectionKey.OP_READ);
		}

		/**
		 * Close the selector
		 * 
		 * @throws IOException
		 */
		public synchronized void stop() throws IOException {
			mReaderSelector.close();
		}

		@Override
		public void run() {
			while (!mClosed.get()) {
				try {

					// Wait for an event
					mReaderSelector.select();

					// Needed to avoid a call of select if we register a channel
					synchronized (this) {
					}

					if (mClosed.get()) {
						return;
					}

					Iterator<SelectionKey> keyIterator = mReaderSelector
							.selectedKeys().iterator();

					while (keyIterator.hasNext()) {
						SelectionKey key = keyIterator.next();
						keyIterator.remove();

						// When we cancel, it's possible that the key isn't
						// already
						// removed
						if (key.isValid()) {
							if (key.isReadable()) {
								read(key);
							}
						}
					}
				} catch (IOException e) {
				}
			}
		}
	}

	public class NIOWriter implements Runnable {

		private Selector mWriterSelector;

		/**
		 * Default constructor
		 * 
		 * @throws IOException
		 */
		public NIOWriter() throws IOException {
			mWriterSelector = SelectorProvider.provider().openSelector();
			mAnswerBuffer = new NIOAnswerBuffer(this);
		}

		/**
		 * Write the data
		 * 
		 * @param key
		 *            the socket that we want to write
		 * @throws IOException
		 *             if we can't write
		 */
		private void write(SelectionKey key) throws IOException {
			SocketChannel socketChannel = (SocketChannel) key.channel();
			ByteBuffer dataToSend = mAnswerBuffer.get(socketChannel);

			if (dataToSend == null) {
				return;
			}

			socketChannel.write(dataToSend);

			// We successfull send the answer
			if (dataToSend.remaining() == 0) {
				mAnswerBuffer.removeAnswer(socketChannel);
			}
		}

		/**
		 * Register the channel with the selector
		 * 
		 * @param socketChannel
		 * @throws ClosedChannelException
		 */
		public synchronized void register(SocketChannel socketChannel) throws ClosedChannelException {
			mWriterSelector.wakeup();
			socketChannel.register(mWriterSelector, SelectionKey.OP_WRITE);
		}

		/**
		 * Close the selector
		 * 
		 * @throws IOException
		 */
		public synchronized void stop() throws IOException {
			mWriterSelector.close();
		}

		@Override
		public void run() {
			while (!mClosed.get()) {
				try {

					// Wait for an event
					mWriterSelector.select();

					// Needed to avoid a call of select if we register a channel
					synchronized (this) {
					}

					if (mClosed.get()) {
						return;
					}

					Iterator<SelectionKey> keyIterator = mWriterSelector
							.selectedKeys().iterator();

					while (keyIterator.hasNext()) {
						SelectionKey key = keyIterator.next();
						keyIterator.remove();

						// When we cancel, it's possible that the key isn't
						// already
						// removed
						synchronized (key) {
							if (key.isValid()) {
								if (key.isWritable()) {
									write(key);
								}
							}
						}
					}
				} catch (IOException e) {
				}
			}
		}

		public synchronized void send() {
			mWriterSelector.wakeup();
		}
	}
}
