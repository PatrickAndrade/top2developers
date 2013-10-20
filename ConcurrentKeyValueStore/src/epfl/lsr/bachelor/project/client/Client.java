package epfl.lsr.bachelor.project.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import epfl.lsr.bachelor.project.util.Constants;

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

	public String fakeCommand(String command) {
		return sendAndGetAnswer(command);
	}
	
	public String get(String key) {
		return (key == null) ? null : sendAndGetAnswer(Constants.GET_COMMAND + " " + key);
	}

	public String set(String key, String value) {
		return ((key == null) || (value == null)) ? null : 
			sendAndGetAnswer(Constants.SET_COMMAND + " " + key + " " + value);
	}

	public String delete(String key) {
		return (key == null) ? null : sendAndGetAnswer(Constants.DEL_COMMAND + " " + key);
	}

	public String increment(String key) {
		return (key == null) ? null : sendAndGetAnswer(Constants.INCR_COMMAND + " " + key);
	}

	public String increment(String key, int value) {
		return (key == null) ? null : sendAndGetAnswer(Constants.HINCR_COMMAND + " " + key + " " + value);
	}

	public String decrement(String key) {
		return (key == null) ? null : sendAndGetAnswer(Constants.DECR_COMMAND + " " + key);
	}

	public String decrement(String key, int value) {
		return (key == null) ? null : sendAndGetAnswer(Constants.HDECR_COMMAND + " " + key + " " + value);
	}
	
	public String quit() {
		return sendAndGetAnswer("quit");
	}

	private String sendAndGetAnswer(String toBeWritten) {
		try {
			mDataOutputStream.writeBytes(toBeWritten + "\n");
			mDataOutputStream.flush();
			return mBufferedReader.readLine();
		} catch (IOException e) {
			return null;
		}
	}
}
