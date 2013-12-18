package epfl.lsr.bachelor.project.benchmarks;

import java.net.InetSocketAddress;

import epfl.lsr.bachelor.project.client.PipelinedClient;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * General Benchmark enables to send several requests with several clients
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class GeneralBenchmarkPipelined {

	private String[] mRequest;
	private int[] mNumberOfSend;
	private Thread[] mClients;
	private String mAddress;
	private static final long TEN_POWER_SIX = 1000000;
	private static final long TEN_POWER_THREE = 1000;

	/**
	 * Default constructor
	 * 
	 * @param request the array of request
	 * @param numberOfSend the array of number of time a request is send
	 * @param numberClient the number of client (must be greater or equals than 1)
	 */
	public GeneralBenchmarkPipelined(String[] request, int[] numberOfSend,
			int numberClient) {
		
		if (numberClient < 1) {
			throw new IllegalArgumentException("numberClient < 1");
		}
		
		mRequest = request;
		mNumberOfSend = numberOfSend;
		mAddress = "127.0.0.1";

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
		System.out.flush();
	}

	private class ClientWorker implements Runnable {

		@Override
		public void run() {
			PipelinedClient client = new PipelinedClient(new InetSocketAddress(
					mAddress, Constants.PORT));
			client.connect();

			long totalTime = 0;
			long initTime = 0;
			long finishedTime = 0;

			// Send pipelined requests
			for (int i = 0; i < mRequest.length; i++) {
				initTime = System.nanoTime();
				for (int j = 0; j < mNumberOfSend[i]; j++) {
					client.customCommand(mRequest[i]);
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
