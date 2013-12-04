package epfl.lsr.bachelor.project.pipe;

import epfl.lsr.bachelor.project.server.RequestBuffer;

/**
 * Single thread that performs the requests (it's like a pipe between RequestBuffer and KeyValueStore)
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
final public class SingleThreadPipe implements WorkerPipeInterface {

	private static SingleThreadPipe sInstance;
	private ThreadPipe mWorker;
	
	/**
	 * Default constructor that will link the buffer of requests to the single thread
	 * 
	 * @param requestBuffer the buffer to be linked to
	 */
	private SingleThreadPipe(RequestBuffer requestBuffer) {
		mWorker = new ThreadPipe(requestBuffer);
	}
	
	/**
	 * Enables to get the instance of the single thread
	 * 
	 * @param requestBuffer the buffer to be linked to
	 * @return the instance of the single thread
	 */
	public static SingleThreadPipe getInstance(RequestBuffer requestBuffer) {
		if (sInstance == null) {
			sInstance = new SingleThreadPipe(requestBuffer);
		}
		return sInstance;
	}
	
	public void start() {
		new Thread(mWorker).start();
	}
	
	/**
	 * Enables to close properly the thread attached to this {@link Runnable} object
	 */
	public void close() {
		mWorker.close();
	}
}
