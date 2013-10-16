package epfl.lsr.bachelor.project.pipe;

import epfl.lsr.bachelor.project.server.RequestBuffer;
import epfl.lsr.bachelor.project.server.request.Request;

/**
 * Thread that performs the requests (it's like a pipe between {@link RequestBuffer} and {@link KeyValueStore}
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
final public class SingleThreadPipe implements Runnable {

	private RequestBuffer mRequestBuffer;
	private static SingleThreadPipe instance;
	
	private SingleThreadPipe(RequestBuffer requestBuffer) {
		mRequestBuffer = requestBuffer;
	}
	
	public static SingleThreadPipe create(RequestBuffer requestBuffer) {
		if (instance == null) {
			instance = new SingleThreadPipe(requestBuffer);
		}
		return instance;
	}
	
	@Override
	public void run() {
		while (true) {
			Request request = mRequestBuffer.take();
			try {
				request.perform();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}
}
