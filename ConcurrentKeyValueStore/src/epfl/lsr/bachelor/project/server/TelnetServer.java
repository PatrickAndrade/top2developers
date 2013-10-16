package epfl.lsr.bachelor.project.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * First implementation of the Telnet server accepting the connections on port 22122
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class TelnetServer {
	public final static int PORT = 22122;
	private static final int NUMBER_CURRENT_CONNECTION = 100;
	
	private static ServerSocket serverSocket;
	
	private static ExecutorService threadPool = Executors.newFixedThreadPool(NUMBER_CURRENT_CONNECTION);

	public static void main(String[] args) {
		try {
			serverSocket = new ServerSocket(PORT);

			init();

			while (true) {

				Socket socket = serverSocket.accept();
				
				Connection connection = new Connection(socket);
				threadPool.execute(connection);		//TODO:si le client ce deco avant qu'un thread n execute connection,
													//géré la deconnection!!!
//				new Thread(new Connection(socket)).start();

			}
		} catch (IOException e) {
			System.out.println("Telnet server encoured some unexpected error ! Please restart !");
		}
		threadPool.shutdown();
		while (!threadPool.isTerminated()) { }; //attendre que l'on ait satisfait les derniers clients
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
