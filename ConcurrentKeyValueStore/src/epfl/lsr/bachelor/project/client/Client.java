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
 * The client that send normal requests to the server
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class Client {

	private Socket mSocket;
	private InetSocketAddress mIpPort;
	private BufferedReader mBufferedReader;
	private DataOutputStream mDataOutputStream;

	/**
	 * Default constructor
	 * 
	 * @param inetaddress
	 *            the {@link InetAddress} of the server
	 * @param port
	 *            the port of the server
	 */
	public Client(InetAddress inetaddress, int port) {
		mIpPort = new InetSocketAddress(inetaddress, port);
	}

	/**
	 * Default constructor
	 * 
	 * @param inetSocketAddress
	 *            the {@link InetSocketAddress} of the server
	 */
	public Client(InetSocketAddress inetSocketAddress) {
		mIpPort = inetSocketAddress;
	}

	/**
	 * Enables to connect to the server. This MUST be called before executing
	 * any request
	 * 
	 * @return if we are connected to the server
	 */
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

	/**
	 * Enables to know if we are connected to the server
	 * 
	 * @return if we are connected
	 */
	public boolean isConnected() {
		return (mSocket != null) && mSocket.isConnected()
				&& !mSocket.isClosed();
	}

	/**
	 * Enables to disconnect the client, should always be called when we are
	 * done with the server
	 */
	public void disconnect() {
		try {
			mBufferedReader.close();
			mDataOutputStream.close();
			mSocket.close();
		} catch (IOException e) {
		}
	}

	/**
	 * Enables to execute a fake command
	 * 
	 */
	public String fakeCommand(String command) {
		return isConnected() ? sendAndGetAnswer(command) : null;
	}

    /**
     * Enables to execute a get command over the specified key
     * 
     * @param key
     */
	public String get(String key) {
		return (!isConnected() || key == null) ? null : sendAndGetAnswer(Constants.GET_COMMAND
				+ " " + key);
	}

    /**
     * Enables to execute a set command over the specified key with the
     * specified value
     * 
     * @param key
     * @param value
     */
	public String set(String key, String value) {
		return (!isConnected() || (key == null) || (value == null)) ? null
				: sendAndGetAnswer(Constants.SET_COMMAND + " " + key + " "
						+ value);
	}

    /**
     * Enables to execute a delete command over the specified key
     * 
     * @param key
     */
	public String delete(String key) {
		return (!isConnected() || key == null) ? null : sendAndGetAnswer(Constants.DEL_COMMAND
				+ " " + key);
	}

    /**
     * Enables to execute an increment command over the specified key
     * 
     * @param key
     */
	public String increment(String key) {
		return (!isConnected() || key == null) ? null : sendAndGetAnswer(Constants.INCR_COMMAND
				+ " " + key);
	}

    /**
     * Enables to execute an hincrement command over the specified key with the
     * specified increment
     * 
     * @param key
     * @param value
     */
	public String increment(String key, int value) {
		return (!isConnected() || key == null) ? null : sendAndGetAnswer(Constants.HINCR_COMMAND
				+ " " + key + " " + value);
	}

    /**
     * Enables to execute a decrement command over the specified key
     * 
     * @param key
     */
	public String decrement(String key) {
		return (!isConnected() || key == null) ? null : sendAndGetAnswer(Constants.DECR_COMMAND
				+ " " + key);
	}

    /**
     * Enables to execute a decrement command over the specified key with the
     * specified decrement
     * 
     * @param key
     * @param value
     */
	public String decrement(String key, int value) {
		return (!isConnected() || key == null) ? null : sendAndGetAnswer(Constants.HDECR_COMMAND
				+ " " + key + " " + value);
	}

    /**
     * Enables to execute an append command over the specified key with the
     * specified value to append
     * 
     * @param key
     * @param value
     */
	public String append(String key, String value) {
		return (!isConnected() || key == null) ? null : sendAndGetAnswer(Constants.APPEND_COMMAND
				+ " " + key + " " + value);
	}
	
    /**
     * Enables to execute a ping command to the server
     */
	public String ping() {
		return isConnected() ? sendAndGetAnswer(Constants.PING_COMMAND) : null;
	}

    /**
     * Enables to execute a quit command should be executed before a call to the
     * method disconnect
     * 
     */
	public String quit() {
		return isConnected() ? sendAndGetAnswer("quit") : null;
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
