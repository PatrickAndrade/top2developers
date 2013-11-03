package com.example.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

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
