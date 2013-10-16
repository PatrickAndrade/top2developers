package epfl.lsr.bachelor.project.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import epfl.lsr.bachelor.project.pipe.SingleThreadPipe;

/**
 * First implementation of the Telnet server accepting the connections on port 22122
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class TelnetServer {
	public final static int PORT = 22122;
	private static final int NUMBER_CURRENT_CONNECTION = 100;
	
	private static ServerSocket mServerSocket;
	
	private static ExecutorService mThreadPool = Executors.newFixedThreadPool(NUMBER_CURRENT_CONNECTION);
	
	private static RequestBuffer mRequestBuffer = new RequestBuffer();

	public static void main(String[] args) {
		try {
			mServerSocket = new ServerSocket(PORT);
			
			new Thread(SingleThreadPipe.create(mRequestBuffer)).start();

			init();

			while (true) {

				Socket socket = mServerSocket.accept();
				
				Connection connection = new Connection(socket, mRequestBuffer);
				mThreadPool.execute(connection);		//TODO:si le client se déco avant qu'un thread n execute connection,
													//géré la deconnection!!!
//				new Thread(new Connection(socket)).start();

			}
		} catch (IOException e) {
			System.out.println("Telnet server encoured some unexpected error ! Please restart !");
		}
		mThreadPool.shutdown();
		while (!mThreadPool.isTerminated()) { }; //attendre que l'on ait satisfait les derniers clients
		
		try {
			mServerSocket.close();
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
