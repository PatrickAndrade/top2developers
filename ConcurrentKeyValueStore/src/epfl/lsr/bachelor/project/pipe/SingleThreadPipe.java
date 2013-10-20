package epfl.lsr.bachelor.project.pipe;

import java.util.concurrent.atomic.AtomicBoolean;

import epfl.lsr.bachelor.project.server.RequestBuffer;
import epfl.lsr.bachelor.project.server.request.Request;

/**
 * Thread that performs the requests (it's like a pipe between RequestBuffer and KeyValueStore)
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
final public class SingleThreadPipe implements Runnable {

	private RequestBuffer mRequestBuffer;
	private static SingleThreadPipe instance;
	private static AtomicBoolean closed = new AtomicBoolean();
	
	/**
	 * Default constructor that will link the buffer of requests to the single thread
	 * 
	 * @param requestBuffer the buffer to be linked to
	 */
	private SingleThreadPipe(RequestBuffer requestBuffer) {
		mRequestBuffer = requestBuffer;
		closed.set(false);
	}
	
	/**
	 * Enables to get the instance of the single thread
	 * 
	 * @param requestBuffer the buffer to be linked to
	 * @return the instance of the single thread
	 */
	public static SingleThreadPipe getInstance(RequestBuffer requestBuffer) {
		if (instance == null) {
			instance = new SingleThreadPipe(requestBuffer);
		}
		return instance;
	}
	
	@Override
	public void run() {
		// The work of the thread is simply to perform all the requests sent by the clients
		while (!closed.get()) {
			// It gets a request in the buffer
			Request request = mRequestBuffer.take();
			//It executes the request if the thread has not been already closed
			if (!closed.get()) {
				try {
					request.perform();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Enables to close properly the thread attached to this {@link Runnable} object
	 */
	public void close() {
		closed.set(true);
		mRequestBuffer.notifyAllThreadsToStopWaiting();
	}
}
