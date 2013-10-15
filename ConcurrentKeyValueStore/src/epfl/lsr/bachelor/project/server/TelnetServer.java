package epfl.lsr.bachelor.project.server;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * First implementation of the Telnet server accepting the connections on port 22122
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class TelnetServer {
	public final static int PORT = 22122;
	private static ServerSocket serverSocket;

	public static void main(String[] args) {
		try {
			serverSocket = new ServerSocket(PORT);

			init();

			while (true) {

				Socket socket = serverSocket.accept();

				new Thread(new Connection(socket)).start();

			}
		} catch (IOException e) {
			System.out.println("Telnet server encoured some unexpected error ! Please restart !");
		}
	}
	
	private static void init() {
		System.out.println("TelnetServer 1.0.1 (developed in JAVA)");
		System.out.println();
		System.out.println("Running in standard mode");
		System.out.println("Port: " + PORT);
		System.out.println();
	}
}
