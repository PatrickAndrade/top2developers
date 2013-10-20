package epfl.lsr.bachelor.project.benchmarks;

import java.net.InetSocketAddress;

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
	private static final int SIZE = 10;

	public static void main(String[] args) {
	//	initServer();
		
		Thread[] myThreads = new Thread[SIZE];
		for (int i = 0; i < myThreads.length; i++) {
			myThreads[i] = new Thread(new TestingCode());
		}
		
		for (Thread thread : myThreads) {
			thread.start();
		}

		//Server.stop();
	}

	private static void initServer() {
		new Thread(new Runnable() {

			public void run() {
				Server.start();
			}
		}).start();
	}

}

class TestingCode implements Runnable {

	private static final long TEN_POWER_NINE = (long) Math.pow(10, 9);
	private static final long TEN_POWER_SIX = (long) Math.pow(10, 6);
	//private static final long TEN_POWER_THREE = (long) Math.pow(10, 3);
	private static final long ITERATION = (long) Math.pow(10, 2);

	@Override
	public void run() {
		Client client = new Client(new InetSocketAddress("192.168.1.42", Constants.PORT));
		client.connect();

		client.set("b", "10");

		long totalTime = 0;
		long initTime = System.nanoTime();
		long forLoopInitTime = initTime;
		long finishedLoopTime = 0;
		for (long i = 0; i < ITERATION; i++) {

			client.get("a");

			finishedLoopTime = System.nanoTime();
			totalTime += finishedLoopTime - forLoopInitTime;

			forLoopInitTime = finishedLoopTime;

		}
		long finishedTime = System.nanoTime();

		System.out
				.println(" -> Elapsed total time: "
						+ ((finishedTime - initTime) / TEN_POWER_NINE)
						+ " seconds");
		System.out.println(" -> Average total time: " + totalTime
				/ TEN_POWER_SIX / ITERATION + " milliseconds");

		client.disconnect();

	}

}
