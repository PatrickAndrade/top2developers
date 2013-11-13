package epfl.lsr.bachelor.project.serverNIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import epfl.lsr.bachelor.project.server.ServerInterface;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class Server implements ServerInterface {

	private InetSocketAddress mInetSocketAddress;

	private Selector mSelector;
	
	private ByteBuffer mReadByteBuffer;
	private Map<Channel, ArrayList<ByteBuffer>> mDataToSend;

	public Server(String hostname, int port) throws IOException {
		mInetSocketAddress = new InetSocketAddress(hostname, port);

		mReadByteBuffer = ByteBuffer.allocate(Constants.READ_BUFFER_NIO);
		mDataToSend = new HashMap<>();

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

		System.out.println("  -> Started connection with "
				+ mInetSocketAddress.getAddress());
	}

	private void read(SelectionKey key) throws ClosedChannelException {
		SocketChannel socketChannel = (SocketChannel) key.channel();

		mReadByteBuffer.clear();

		int byteRead = 0;

		try {
			byteRead = socketChannel.read(mReadByteBuffer);
		} catch (IOException e) {
			// There is a problem with the connection
			key.cancel();
			
			try {
				socketChannel.close();
			} catch (IOException e1) {
			}
			
			return;
		}

		if (byteRead < 0) {
			
			System.err.println(" -> Connection with "
					+ mInetSocketAddress.getAddress() + " aborted !");
			
			try {
				key.channel().close();
			} catch (IOException e) {
			}
			
			key.cancel();
			
			return;
		}
		
		//TODO. add the parser
	}

	private void write(SelectionKey key) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		
//		for (ByteBuffer toSend : mDataToSend.get(socketChannel));
		
	}

	public void start() {

		System.out.println(Constants.WELCOME_NIO);

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
						} else if (key.isWritable()) {
							write(key);
						}
					}
				}
			} catch (IOException e) {
			}
		}
	}
}
