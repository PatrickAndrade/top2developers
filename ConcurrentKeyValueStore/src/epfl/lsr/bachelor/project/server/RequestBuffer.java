package epfl.lsr.bachelor.project.server;

import java.util.LinkedList;

import epfl.lsr.bachelor.project.server.request.Request;

/**
 * {@link RequestBuffer} is the buffer between the {@link KeyValueStore} and the {@link TelnetServer}
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class RequestBuffer {
	private LinkedList<Request> mRequestList = new LinkedList<Request>();
	
	public synchronized void add(Request request) {
		mRequestList.add(request);
		notify();
	}
	
	public synchronized Request take() {
		if (mRequestList.isEmpty()) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mRequestList.removeFirst();
	}
}
