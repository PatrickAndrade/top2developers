package epfl.concurency.bachelorProject;

/*
 *	Author:      Patrick Oliveira Andrade
 *	Date:        14 oct. 2013
 */

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.ServerSocket;
import java.net.Socket;

public class TelnetServer {
	private final static int PORT = 11211;
	
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(PORT);
			
			System.out.println("Telnet server on port " + PORT + " started sucessfully !");

			while (true) {

				Socket socket = serverSocket.accept();

				OutputStream os = socket.getOutputStream();
				DataOutputStream dout = new DataOutputStream(os);

				dout.writeChars("Welcome to the Telnet channel " + PORT + " ! ");
				dout.writeChars("Please start communicating:");

				new Thread(new Connection(socket)).start();

			}
		} catch (IOException e) {
			System.out.println("Impossible to start the Telnet server !");
		}

	}
}

class Connection implements Runnable {
	private Socket mSocket;
	private BufferedReader bfr;
	private DataOutputStream dout;
	private CommandParser parser;
	
	private final static String QUIT_COMMAND = "quit";

	public Connection(Socket socket) throws IOException {
		mSocket = socket;
		bfr = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		dout = new DataOutputStream(mSocket.getOutputStream());
		System.out.println("	Started connection with " + mSocket.getInetAddress());
		
		parser = new CommandParser();
	}

	public void run() {
		String command = "";
		String answer = "";
		
		while(!command.equals(QUIT_COMMAND)){
			try {
				command = bfr.readLine();
				
				answer = parser.parse(command);
				System.out.println("I read the command: " + command);
				dout.writeChars("Thanks for requesting the command " + command + "\n");
				
				if (!answer.equals("")) {
					dout.writeChars("Answer : " + answer);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			mSocket.close();
			System.err.println("	-> Connection with " + mSocket.getInetAddress() + " aborted!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
