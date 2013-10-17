package epfl.lsr.bachelor.project.tests;

import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import epfl.lsr.bachelor.project.client.Client;
import epfl.lsr.bachelor.project.server.Server;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class AClientShould {

	private Client mClient;
	
	
	@Before
	public void init() throws UnknownHostException {
		mClient = new Client(InetAddress.getLocalHost(), Constants.PORT);
		if (mClient.connect()) {
			System.out.println("Connected");
		}
	}
	
	@After
	public void after() {
		if (mClient.isConnected()) {
			mClient.disconnect();
		}
	}
	
	@Test
	public void enableToConnectWithTheServer() {
		assertTrue("Can't connect the server!", mClient.isConnected());
	}
	
	@Test
	public void enableToGetAValue() {
		String value = mClient.get("Key");
		assertTrue("Can't send a get request!", value != null);
	}
	
}
