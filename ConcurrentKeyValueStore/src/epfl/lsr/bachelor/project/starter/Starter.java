package epfl.lsr.bachelor.project.starter;

import java.io.IOException;

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
	
	public static void main(String[] args) throws IOException {
		boolean isNIO = true;
		ServerInterface server = null;
		
		if (isNIO) {
			server = new NIOServer(HOST, Constants.PORT);
		} else {
			server = new Server(Constants.PORT);
		}
		
		server.start();
	}

}

