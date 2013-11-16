package epfl.lsr.bachelor.project.pipe;

import java.util.concurrent.atomic.AtomicBoolean;

import epfl.lsr.bachelor.project.server.RequestBuffer;
import epfl.lsr.bachelor.project.server.request.Request;

/**
 * This class implement a pipe between the server and the key value store
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
class ThreadPipe implements Runnable {
	private RequestBuffer mRequestBuffer;
	private static AtomicBoolean sClosed = new AtomicBoolean();

	/**
	 * Default constructor that will link the buffer of requests to the single
	 * thread
	 * 
	 * @param requestBuffer
	 *            the buffer to be linked to
	 */
	public ThreadPipe(RequestBuffer requestBuffer) {
		mRequestBuffer = requestBuffer;
		sClosed.set(false);
	}

	@Override
	public void run() {
		// The work of the thread is simply to perform all the requests sent by
		// the clients
		while (!sClosed.get()) {
			// It gets a request in the buffer
			Request request = mRequestBuffer.take();
			// It executes the request if the thread has not been already closed
			if (!sClosed.get()) {
				try {
					request.perform();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Enables to close properly the thread attached to this {@link Runnable}
	 * object
	 */
	public void close() {
		sClosed.set(true);
		mRequestBuffer.notifyAllThreadsToStopWaiting();
	}
}
