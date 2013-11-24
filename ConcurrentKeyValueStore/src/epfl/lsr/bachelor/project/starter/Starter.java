package epfl.lsr.bachelor.project.starter;

import java.io.IOException;

import epfl.lsr.bachelor.project.pipe.SingleThreadPipe;
import epfl.lsr.bachelor.project.pipe.WorkerPipeInterface;
import epfl.lsr.bachelor.project.server.RequestBuffer;
import epfl.lsr.bachelor.project.server.Server;
import epfl.lsr.bachelor.project.server.ServerInterface;
import epfl.lsr.bachelor.project.serverNIO.NIOServer;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * Point of entry of the application
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class Starter {

	public final static String HOST = "127.0.0.1";
	private static ServerInterface server = null;

	public static void main(String[] args) throws IOException {
		boolean isNIO = true;
		RequestBuffer requestBuffer = new RequestBuffer();
		WorkerPipeInterface worker = SingleThreadPipe
				.getInstance(requestBuffer);

		if (isNIO) {
			server = new NIOServer(HOST, Constants.PORT, requestBuffer, worker);
		} else {
			server = new Server(Constants.PORT, requestBuffer, worker);
		}

		server.start();
	}
}
