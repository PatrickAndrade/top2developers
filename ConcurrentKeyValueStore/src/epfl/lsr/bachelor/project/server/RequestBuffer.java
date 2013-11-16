package epfl.lsr.bachelor.project.server;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import epfl.lsr.bachelor.project.server.request.Request;

/**
 * This is the buffer between the Key-Value store and the server containing {@link Request}s
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class RequestBuffer {
	// The list of the requests to be performed
	private LinkedList<Request> mRequestList = new LinkedList<Request>();
	private AtomicBoolean mClosed = new AtomicBoolean();
	
	public RequestBuffer() {
		mClosed.set(false);
	}
	
	/**
	 * Add a request to be performed later on
	 * 
	 * @param request the request to be performed
	 */
	public synchronized void add(Request request) {
		// Add to the list and notify it has something new
		mRequestList.add(request);
		notify();
	}
	
	/**
	 * Take the next request to be performed
	 * 
	 * @return the request to be performed
	 */
	public synchronized Request take() {
		// If the list isn't empty we take the next request otherwise we wait
		while (!mClosed.get() && mRequestList.isEmpty()) {
			
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return (!mClosed.get()) ? mRequestList.removeFirst() : null;
		//return mRequestList.removeFirst();
	}
	
	/**
	 * Enables to notify all the threads that are currently waiting for request.
	 * This method also make that all waiting-take() calls will return <code>null</code>
	 */
	public synchronized void notifyAllThreadsToStopWaiting() {
		mClosed.set(true);
		notifyAll();
	}
}
