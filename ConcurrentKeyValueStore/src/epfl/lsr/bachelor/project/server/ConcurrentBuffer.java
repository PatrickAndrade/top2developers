package epfl.lsr.bachelor.project.server;

import java.util.concurrent.Semaphore;

import epfl.lsr.bachelor.project.server.request.Request;

/**
 * Concurrent fixed-size buffer of {@link Request}s
 * 
 * @author Gregory Maitre & Patrick Andrade
 *
 */
public class ConcurrentBuffer {

	private int mSize;
	private int mInput;
	private int mOutput;
	
	private Request[] mTable;
	
	private Semaphore mMutex1;
	private Semaphore mMutex2;
	private Semaphore mRequestsCounter;
	private Semaphore mFreeSlots;

	/**
	 * Constructor of a concurrent fixed-size buffer
	 * 
	 * @param capacity the fixed-size
	 */
	public ConcurrentBuffer(int capacity) {
		mSize = capacity;
		mInput = 0;
		mOutput = 0;
		mTable = new Request[capacity];
		mMutex1 = new Semaphore(1);
		mMutex2 = new Semaphore(1);
		mRequestsCounter = new Semaphore(0);
		mFreeSlots = new Semaphore(capacity);
	}

	/**
	 * Enables to put a request in the buffers
	 * 
	 * @param request the request to be added
	 */
	public void put(Request request) {
		try {
			mMutex1.acquire();

			mFreeSlots.acquire();

			mTable[mInput] = request;

			mInput = (mInput + 1) % mSize;

			mRequestsCounter.release();
		} catch (InterruptedException e) {
			System.out.println("Some exception has happened in put() method !");
		} finally {
			mMutex1.release();

		}
	}

	/**
	 * Enables to get the next request to be performed
	 * 
	 * @return the next request to be performed
	 */
	public Request get() {
		Request task = null;
		try {
			mMutex2.acquire();

			mRequestsCounter.acquire();

			task = mTable[mOutput];

			mTable[mOutput] = null;

			mOutput = (mOutput + 1) % mSize;

			mFreeSlots.release();

		} catch (InterruptedException e) {
			System.out.println("Some exception has happened in get() method !");
		} finally {
			mMutex2.release();
		}
		return task;
	}

}
