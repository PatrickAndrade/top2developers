package epfl.lsr.bachelor.project.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import epfl.lsr.bachelor.project.client.Client;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * Tests of the client
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
	public void enableToGetNILIfAKeyDoesNotExistInMemory() {
		assertEquals(Constants.PROGRAMM_NAME + "NIL", mClient.get("NILKey"));
	}
	
	@Test
	public void enableToSetAndGetAValue() {
		String value = "someValue";
		String key = "someKey";
		mClient.set(key, value);
		assertEquals(Constants.PROGRAMM_NAME + value, mClient.get(key));
	}
	
	@Test
	public void enableToSetAndDeleteAValue() {
		String value = "deleteValue";
		String key = "deleteKey";
		mClient.set(key, value);
		assertEquals(Constants.PROGRAMM_NAME + "DELETED", mClient.delete(key));
	}
	
}
