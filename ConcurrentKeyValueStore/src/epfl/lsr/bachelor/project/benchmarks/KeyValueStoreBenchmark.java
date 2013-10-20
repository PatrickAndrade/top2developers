package epfl.lsr.bachelor.project.benchmarks;

import java.net.InetAddress;
import java.net.UnknownHostException;

import epfl.lsr.bachelor.project.client.Client;
import epfl.lsr.bachelor.project.server.Server;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * This is the first benchmark of you KeyValueStore
 * 
 * @author Patrick Andrade
 * 
 */
public class KeyValueStoreBenchmark {
	private static final long TEN_POWER_SIX = (long) Math.pow(10, 6);
	private static final long ITERATION = (long) Math.pow(10, 2);

	public static void main(String[] args) {
		initServer();
		
		try {
			Client client = new Client(InetAddress.getLocalHost(), Constants.PORT);
			client.connect();
			
			client.set("a", "10");

			long initTime = System.nanoTime();
			long forLoopInitTime = initTime;
			for (long i = 0; i < ITERATION; i++) {

				client.get("a");
				long finishedLoopTime = System.nanoTime();

				System.out.println("Elapsed time: "
						+ ((finishedLoopTime - forLoopInitTime) / TEN_POWER_SIX)
						+ " milliseconds");
				forLoopInitTime = finishedLoopTime;
			}
			long finishedTime = System.nanoTime();

			System.out.println(" -> Elapsed total time: " +
					((finishedTime - initTime) / TEN_POWER_SIX) + " milliseconds");
			
			client.disconnect();
			Server.stop();
			
		} catch (UnknownHostException e) { 
			System.err.println("Unable to solve the host ! Please restart !");
		}
	}

	private static void initServer() {
		new Thread(new Runnable() {

			public void run() {
				Server.start();
			}
		}).start();
	}

}
