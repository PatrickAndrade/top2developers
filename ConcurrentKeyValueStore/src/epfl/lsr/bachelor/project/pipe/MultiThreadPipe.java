package epfl.lsr.bachelor.project.pipe;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import epfl.lsr.bachelor.project.server.RequestBuffer;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * Set of thread that performs the requests (it's like a pipe between
 * RequestBuffer and KeyValueStore)
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
final public class MultiThreadPipe implements WorkerPipeInterface {

	private static MultiThreadPipe mInstance;
	private ExecutorService mWorkerThread;
	private ArrayList<ThreadPipe> mWorkerList;
	private RequestBuffer mRequestBuffer;

	private MultiThreadPipe(RequestBuffer requestBuffer) {
		mWorkerThread = Executors
				.newFixedThreadPool(Constants.NUMBER_OF_PIPELINED_WORKER);
		mRequestBuffer = requestBuffer;
		mWorkerList = new ArrayList<ThreadPipe>();
	}

	/**
	 * Create or return the single instance of MultiThreadPipe
	 * 
	 * @param requestBuffer the request buffer
	 * @return the single instance of this class
	 */
	public static MultiThreadPipe getInstance(RequestBuffer requestBuffer) {
		if (mInstance == null) {
			mInstance = new MultiThreadPipe(requestBuffer);
		}

		return mInstance;
	}

	@Override
	public void start() {
		for (int i = 0; i < Constants.NUMBER_OF_PIPELINED_WORKER; i++) {
			ThreadPipe worker = new ThreadPipe(mRequestBuffer);
			mWorkerList.add(worker);
			mWorkerThread.execute(worker);
		}
	}

	@Override
	public void close() {
		for (ThreadPipe worker : mWorkerList) {
			worker.close();
		}
		
		mWorkerThread.shutdown();

		while (!mWorkerThread.isTerminated()) {
		}
	}
}
