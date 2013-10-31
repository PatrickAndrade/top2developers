package epfl.lsr.bachelor.project.connection;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

import epfl.lsr.bachelor.project.server.request.Request;

/**
 * This class sort the request to send in order to ensure the correct
 * order of the answer
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class PriorityWaitingRequestQueue<T extends Request> {
	
	private PriorityBlockingQueue<T> mRequestToSend;
	private long mNextItem;
	private boolean mClosed;

	/**
	 * Construct the PriorityBlockingGenericQueue
	 * 
	 * @param initialCapacity the initial capacity of the queue
	 * @param comparator the comparator between two request
	 */
	public PriorityWaitingRequestQueue(int initialCapacity,
			Comparator<T> comparator) {
		mRequestToSend = new PriorityBlockingQueue<>(initialCapacity,
				comparator);
		mNextItem = 0;
		mClosed = false;
	}

	/**
	 * Return the next request to send or wait until the next request is ready
	 * 
	 * @return the next request to send
	 */
	public synchronized T poll() {

		// We first check if the queue has some element and if so,
		// we take it and ensure that it's the next request to be
		// answered, else we wait

		while (!mClosed
				&& ((mRequestToSend.peek() == null) 
				|| (mNextItem != mRequestToSend.peek().getID()))) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

		mNextItem++;
		return mRequestToSend.poll();
	}

	/**
	 * Add a request to the PriorityWaitingRequestQueue
	 * 
	 * @param request the request to add
	 */
	public synchronized void add(T request) {
		mRequestToSend.add(request);

		if (request.getID() == mNextItem) {
			notify();
		}
	}

	/**
	 * Close the thread that wait
	 */
	public synchronized void close() {
		mClosed = true;
		notify();
	}
}
