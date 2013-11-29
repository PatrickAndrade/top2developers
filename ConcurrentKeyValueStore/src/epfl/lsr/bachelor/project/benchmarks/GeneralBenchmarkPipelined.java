package epfl.lsr.bachelor.project.benchmarks;

import java.net.InetSocketAddress;

import epfl.lsr.bachelor.project.client.PipelinedClient;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * General Benchmark
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class GeneralBenchmarkPipelined {

	private String[] mRequest;
	private int[] mNumberOfSend;
	private Thread[] mClients;
	private static final long TEN_POWER_SIX = (long) Math.pow(10, 6);
	private static final long TEN_POWER_THREE = (long) Math.pow(10, 3);

	/**
	 * Default constructor
	 * 
	 * @param request
	 * @param numberOfSend
	 * @param numberClient
	 */
	public GeneralBenchmarkPipelined(String[] request, int[] numberOfSend,
			int numberClient) {
		mRequest = request;
		mNumberOfSend = numberOfSend;

		if (mNumberOfSend.length != mRequest.length) {
			throw new IllegalArgumentException(
					"Resquet.length != NumberOfRequest.length");
		}

		mClients = new Thread[numberClient];
		for (int i = 0; i < numberClient; i++) {
			mClients[i] = new Thread(new ClientWorker());
		}
	}

	/**
	 * Start the benchmark
	 */
	public void start() {
		for (Thread thread : mClients) {
			thread.start();
		}
	}

	private synchronized void print(String command, double elapsed,
			double average) {
		System.out.println(" -> " + command);
		System.out.println(" -> Elapsed total time: " + elapsed
				+ " milliseconds");
		System.out.println(" -> Average total time: " + average
				+ " microseconds");

		System.out.println("#################################################");
	}

	private class ClientWorker implements Runnable {

		@Override
		public void run() {
			PipelinedClient client = new PipelinedClient(new InetSocketAddress(
					"127.0.0.1", Constants.PORT));
			client.connect();

			long totalTime = 0;
			long initTime = 0;
			long finishedTime = 0;

			// Send pipelined requests
			for (int i = 0; i < mRequest.length; i++) {
				initTime = System.nanoTime();
				for (int j = 0; j < mNumberOfSend[i]; j++) {
					client.fakeCommand(mRequest[i]);
				}

				for (int j = 0; j < mNumberOfSend[i]; j++) {
					client.getNextAnswerFromServer();
				}
				finishedTime = System.nanoTime();
				totalTime = finishedTime - initTime;

				print(mRequest[i], (double) totalTime / (double) TEN_POWER_SIX,
						(double) totalTime
								/ (double) (mNumberOfSend[i] * TEN_POWER_THREE));
			}

			client.disconnect();
		}
	}
}
