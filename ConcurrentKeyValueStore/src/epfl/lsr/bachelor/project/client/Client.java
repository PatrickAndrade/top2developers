package epfl.lsr.bachelor.project.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * The client that send the request to the server
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class Client {

	private Socket mSocket;
	private InetSocketAddress mIpPort;
	private BufferedReader mBufferedReader;
	private DataOutputStream mDataOutputStream;

	public Client(InetAddress inetaddress, int port) {
		mIpPort = new InetSocketAddress(inetaddress, port);
	}

	public boolean connect() {
		if (isConnected()) {
			return true;
		}
		
		try {
			mSocket = new Socket(mIpPort.getAddress(), mIpPort.getPort());
			mBufferedReader = new BufferedReader(new InputStreamReader(
					mSocket.getInputStream()));
			mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
	
	public boolean isConnected() {
		return (mSocket != null) && mSocket.isConnected();
	}

	public void disconnect() {
		try {
			mBufferedReader.close();
			mDataOutputStream.close();
			mSocket.close();
		} catch (IOException e) {
		}
	}

	public String get(String key) {
		if (key == null) {
			return null;
		}

		try {
			mDataOutputStream.writeChars("get " + key + "\0");
			return mBufferedReader.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	public String set(String key, String value) {
		if ((key == null) || (value == null)) {
			return null;
		}

		try {
			mDataOutputStream.writeChars("set " + key + " " + value);
			return mBufferedReader.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	public String delete(String key) {
		if (key == null) {
			return null;
		}

		try {
			mDataOutputStream.writeChars("del " + key);
			return mBufferedReader.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	public String increment(String key) {
		if (key == null) {
			return null;
		}

		try {
			mDataOutputStream.writeChars("incr " + key);
			return mBufferedReader.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	public String increment(String key, int value) {
		if (key == null) {
			return null;
		}

		try {
			mDataOutputStream.writeChars("hincr " + key + value);
			return mBufferedReader.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	public String decrement(String key) {
		if (key == null) {
			return null;
		}

		try {
			mDataOutputStream.writeChars("decr " + key);
			return mBufferedReader.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	public String decrement(String key, int value) {
		if (key == null) {
			return null;
		}

		try {
			mDataOutputStream.writeChars("hdecr " + key + value);
			return mBufferedReader.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	public void emptyInput() {
		try {
			while (mBufferedReader.readLine() != null) {	}
		} catch (IOException e) {
		}
	}
}
