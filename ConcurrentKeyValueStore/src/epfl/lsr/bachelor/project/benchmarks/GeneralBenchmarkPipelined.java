package epfl.lsr.bachelor.project.benchmarks;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

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
	private int mNumberOfSend;
	private Thread[] mClients;
	private String mAddress;
	private static final long TEN_POWER_THREE = 1000;
	private AtomicInteger counter;
	private ConcurrentLinkedQueue<Double> averageTime;

	/**
	 * Default constructor
	 * 
	 * @param request
	 *            the array of request
	 * @param numberOfSend
	 *            the array of number of time a request is send
	 * @param numberClient
	 *            the number of client (must be greater or equals than 1)
	 */
	public GeneralBenchmarkPipelined(String[] request, int numberOfSend,
			int numberClient) {

		if (numberClient < 1) {
			throw new IllegalArgumentException("numberClient < 1");
		}

		mRequest = request;
		mNumberOfSend = numberOfSend;
		mAddress = "192.168.0.1";//InetAddress.getLoopbackAddress().getHostAddress();

		if (mNumberOfSend < 1) {
			throw new IllegalArgumentException("NumberOfRequest < 1");
		}

		mClients = new Thread[numberClient];
		for (int i = 0; i < numberClient; i++) {
			mClients[i] = new Thread(new ClientWorker());
		}
	}

	/**
	 * Start the benchmark
	 */
	public synchronized double start() {
		counter = new AtomicInteger(0);
		averageTime = new ConcurrentLinkedQueue<Double>();
		for (Thread thread : mClients) {
			thread.start();
		}
		double average = 0;
		try {
			wait();
			for (double time : averageTime) {
				average += time;
			}
			average = average/averageTime.size();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return average;
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
			final int rangeOfKeyValue = 100;
			String command = "";

			// Send pipelined requests
			initTime = System.nanoTime();
			for (int j = 0; j < mNumberOfSend; j++) {
				command = mRequest[(int) (Math.random() * mRequest.length)]
						+ " " + (int) (Math.random() * rangeOfKeyValue) + " "
						+ (int) (Math.random() * rangeOfKeyValue);
				client.customCommand(command);
			}

			for (int j = 0; j < mNumberOfSend; j++) {
				client.getNextAnswerFromServer();
			}
			finishedTime = System.nanoTime();
			totalTime = finishedTime - initTime;

//			print(Arrays.toString(mRequest), (double) totalTime / (double) TEN_POWER_SIX,
//					(double) totalTime
//							/ (double) (mNumberOfSend * TEN_POWER_THREE));

			client.disconnect();
			
			averageTime.add((double) totalTime
					/ (double) (mNumberOfSend * TEN_POWER_THREE));
			
			counter.incrementAndGet();
			if (counter.get() >= mClients.length) {
				synchronized (GeneralBenchmarkPipelined.this) {
					GeneralBenchmarkPipelined.this.notify();
				}
			}
		}
	}
}
