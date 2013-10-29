package epfl.lsr.bachelor.project.benchmarks;

import java.net.InetSocketAddress;
import java.util.NoSuchElementException;

import epfl.lsr.bachelor.project.client.Client;
import epfl.lsr.bachelor.project.client.PipelinedClient;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * This is the first benchmark of you KeyValueStore
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class KeyValueStoreBenchmarkNotPipelined {
	public static final int SIZE = 5;

	public static void main(String[] args) {

		Thread[] myThreads = new Thread[SIZE];
		for (int i = 0; i < myThreads.length; i++) {
			myThreads[i] = new Thread(new TestingCodeNotPipelined());
		}

		for (Thread thread : myThreads) {
			thread.start();
		}

	}

} 

class TestingCodeNotPipelined implements Runnable {

	//private static final long TEN_POWER_NINE = (long) Math.pow(10, 9);
	private static final long TEN_POWER_SIX = (long) Math.pow(10, 6);
	private static final long TEN_POWER_THREE = (long) Math.pow(10, 3);
	private static final long ITERATION = (long) Math.pow(10, 1);

	@Override
	public void run() {
		Client client = new Client(new InetSocketAddress(
				"128.179.146.219", Constants.PORT));
		client.connect();

		// Set a first value
		client.set("a", "2");
		//System.out.println(client.getNextAnswerFromServer());

		long totalTime = 0;
		long initTime = System.nanoTime();

		// Send pipelined requests
		for (long i = 0; i < ITERATION; i++) {
			System.out.println(client.ping());
//			client.get("a");
		}

		// Read answers
		/*for (long i = 0; i < ITERATION; i++) {
			try {
//				System.out.println(client.getNextAnswerFromServer());
				client.getNextAnswerFromServer();
			} catch (NoSuchElementException e) {
				e.printStackTrace();
			}
		}*/

		long finishedTime = System.nanoTime();
		totalTime = finishedTime - initTime;

		System.out.println(" -> Elapsed total time: " + (double) totalTime / (double) TEN_POWER_SIX + " milliseconds");
		System.out.println(" -> Average total time: " + (double) totalTime /
				(double) (KeyValueStoreBenchmark.SIZE * ITERATION * TEN_POWER_THREE) + " microseconds");

		client.disconnect();

	}

}
