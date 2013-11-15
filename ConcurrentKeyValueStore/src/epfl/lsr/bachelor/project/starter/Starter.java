package epfl.lsr.bachelor.project.starter;

import java.io.IOException;

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

	public static void main(String[] args) throws IOException {
		ServerInterface server = new NIOServer("127.0.0.1", Constants.PORT);
		server.start();
	}

}

