package epfl.lsr.bachelor.project.starter;

import epfl.lsr.bachelor.project.server.Server;
import epfl.lsr.bachelor.project.server.ServerInterface;

/**
 * Point of entry of the application
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class Starter {

	public static void main(String[] args) {
		ServerInterface server = new Server();
		server.start();
	}

}

