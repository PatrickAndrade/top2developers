package epfl.lsr.bachelor.project.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import epfl.lsr.bachelor.project.util.CommandParser;

/**
 * Encapsulates a connection opened
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
final class Connection implements Runnable {
	private Socket mSocket;
	private BufferedReader mBufferedReader;
	private DataOutputStream mDataOutputStream;
	private CommandParser mCommandParser;

	private final static String QUIT_COMMAND = "quit";

	public Connection(Socket socket) throws IOException {
		mSocket = socket;
		mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
		mCommandParser = new CommandParser();
		
		System.out.println("  -> Started connection with " + mSocket.getInetAddress());
	}

	public void run() {
		String command = "";

		while (!command.equals(QUIT_COMMAND)) {
			try {
				mDataOutputStream.writeChars("ConcurrentKeyValueStore > ");
				command = mBufferedReader.readLine();
				String answer = mCommandParser.parse(command);

				if (!answer.equals("")) {
					mDataOutputStream.writeChars(answer + "\n");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			closeConnection(mSocket);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void closeConnection(Socket socket) throws IOException {
		socket.close();
		System.err.println("   -> Connection with " + mSocket.getInetAddress() + " aborted !");
	}
}