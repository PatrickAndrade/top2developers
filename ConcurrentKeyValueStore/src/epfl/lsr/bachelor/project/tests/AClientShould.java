package epfl.lsr.bachelor.project.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import epfl.lsr.bachelor.project.client.Client;
import epfl.lsr.bachelor.project.server.Server;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * Tests of the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class AClientShould {

	private Client mClient;

	@BeforeClass
	public static void doFirst() {
		new Thread(new Runnable() {

			public void run() {
				Server.start();

			}
		}).start();
	}
	
	@AfterClass
	public static void doLast() {
		Server.stop();
	}

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
		assertEquals("Unable to get a key with no value",
				Constants.PROGRAMM_NAME + "NIL", mClient.get("NILKey"));
	}
	
	@Test
	public void enableToSetAValue() {
		String key = "setKey";
		String value = "setValue";
		assertEquals("Unable to set a key with a value",
				Constants.PROGRAMM_NAME + "STORED", mClient.set(key, value));
		mClient.delete(key);
	}
	
	@Test
	public void enableToSetAValueWithInteger() {
		String key = "setKey";
		String value = "4";
		assertEquals("Unable to set a key with a value",
				Constants.PROGRAMM_NAME + "STORED", mClient.set(key, value));
		mClient.delete(key);
	}

	@Test
	public void enableToSetAndGetAValue() {
		String value = "someValue";
		String key = "someKey";
		mClient.set(key, value);
		assertEquals("Unable to set a key with a value and get it", 
				Constants.PROGRAMM_NAME + value, mClient.get(key));
		mClient.delete(key);
	}
	
	@Test
	public void enableToSetAKeyWithANewValue() {
		String key = "setKey";
		String value = "setValue";
		mClient.set(key, value);
		assertEquals("Unable to set a key with a value and get it",
				Constants.PROGRAMM_NAME + value, mClient.get(key));
		String newValue = "setNewValue";
		mClient.set(key, newValue);
		assertEquals("Unable to set a key (with a value) with a new value", 
				Constants.PROGRAMM_NAME + newValue, mClient.get(key));
		mClient.delete(key);
	}

	@Test
	public void unableToDeleteAKeyWithNoValue() {
		String key = "deleteValue";
		assertEquals("Enable to delete a key with no value", 
				Constants.PROGRAMM_NAME + "-Err no such value", mClient.delete(key));
	}
	
	@Test
	public void enableToSetAndDeleteAValue() {
		String value = "deleteValue";
		String key = "deleteKey";
		mClient.set(key, value);
		assertEquals("Unable to delete a key", 
				Constants.PROGRAMM_NAME + "DELETED", mClient.delete(key));
	}

	@Test
	public void enableToIncrementANonExistingValue() {
		String key = "incrValue";
		assertEquals("Unable to increment a non existing key", 
				Constants.PROGRAMM_NAME + "(integer) 1", mClient.increment(key));
		mClient.delete(key);
	}
	
	@Test
	public void enableToIncrementAnExistingValue() {
		String key = "incrValue";
		mClient.increment(key);
		assertEquals("Unable to increment an existing key", 
				Constants.PROGRAMM_NAME + "(integer) 2", mClient.increment(key));
		mClient.delete(key);
	}
	
	@Test
	public void enableToIncrementAnExistingValueWithASpecificNumber() {
		String key = "incrValue";
		final int value = 4;
		assertEquals("Unable to increment an existing key with a specific value", 
				Constants.PROGRAMM_NAME + "(integer) 4", mClient.increment(key, value));
		mClient.delete(key);
	}
	
	@Test
	public void unableToDoAnOverflowWithIncrement() {
		String key = "keyIncrement";
		mClient.increment(key);
		assertEquals("Enable to do an overflow with Increment",
				Constants.PROGRAMM_NAME + "(integer) 1", mClient.increment(key, Integer.MAX_VALUE));
		mClient.delete(key);
	}
	
	@Test
	public void unableToIncrementAnNonInteger() {
		String key = "keyIncrement";
		String value = "valueIncrement";
		mClient.set(key, value);
		assertEquals("Enable to increment a string",
				Constants.PROGRAMM_NAME + "-Err not supported for this value",
				mClient.increment(key));
		mClient.delete(key);
	}
	
	@Test
	public void enableToDecrementANonExistingValue() {
		String key = "decrValue";
		assertEquals("Unenable to decrement a non existing key", 
				Constants.PROGRAMM_NAME + "(integer) -1", mClient.decrement(key));
		mClient.delete(key);
	}
	
	@Test
	public void enableToDecrementAnExistingValue() {
		String key = "decrValue";
		mClient.decrement(key);
		assertEquals("Unable to decrement an existing key", 
				Constants.PROGRAMM_NAME + "(integer) -2", mClient.decrement(key));
		mClient.delete(key);
	}
	
	@Test
	public void enableTodecrementAnExistingValueWithASpecificNumber() {
		String key = "decrValue";
		final int value = 4;
		assertEquals("Unable to decrement an existing key with a specific value", 
				Constants.PROGRAMM_NAME + "(integer) -4", mClient.decrement(key, value));
		mClient.delete(key);
	}
	
	@Test
	public void unableToDoAnOverflowWithDecrement() {
		String key = "keyDecrement";
		mClient.decrement(key);
		assertEquals("Enable to do an overflow with Decrement",
				Constants.PROGRAMM_NAME + "(integer) -1", mClient.decrement(key, Integer.MIN_VALUE));
		mClient.delete(key);
	}
	
	@Test
	public void unableToDecrementAnNonInteger() {
		String key = "keyDecrement";
		String value = "valueDecrement";
		mClient.set(key, value);
		assertEquals("Enable to decrement a string",
				Constants.PROGRAMM_NAME + "-Err not supported for this value",
				mClient.decrement(key));
		mClient.delete(key);
	}
	
	@Test
	public void unableToHaveAndEmptyKey() {
		String key = "";
		final int value = 4;
		
		assertEquals("Enable to set an empty key", 
				Constants.PROGRAMM_NAME + "-Err '" + Constants.EMPTY_STRING + "' can't be a key",
				mClient.set(key, String.valueOf(value)));
		assertEquals("Enable to set an empty key", 
				Constants.PROGRAMM_NAME + "-Err '" + Constants.EMPTY_STRING + "' can't be a key",
				mClient.increment(key, value));
		assertEquals("Enable to set an empty key", 
				Constants.PROGRAMM_NAME + "-Err '" + Constants.EMPTY_STRING + "' can't be a key",
				mClient.decrement(key, value));
	}
	
	@Test
	public void beFreeToSendBadCommand() {
		String command = "fake key value";
		assertEquals("Doesn't receive an error when the client send some bad commands", 
				Constants.PROGRAMM_NAME + "-Err unable to execute command 'fake'", mClient.fakeCommand(command));
	}
	
	@Test
	public void beFreeToSendNoCommand() {
		String command = "";
		assertEquals("Receive an error when the client send nothing", 
				Constants.PROGRAMM_NAME, mClient.fakeCommand(command));
	}
	
	@Test
	public void beFreeToSendBadGetCommand() {
		String key = "";
		assertEquals("Doesn't receive an error when the client send some bad commands", 
				Constants.PROGRAMM_NAME + "-Err get request one argument", mClient.get(key));
		mClient.delete(key);
	}
	
	@Test
	public void beFreeToSendBadSetCommand() {
		String keyValue = "";
		assertEquals("Doesn't receive an error when the client send some bad commands", 
				Constants.PROGRAMM_NAME + "-Err set request two arguments", mClient.set(keyValue, keyValue));
		mClient.delete(keyValue);
	}
	
	@Test
	public void beFreeToSendBadIncrementDecrementCommand() {
		String key = "";
		assertEquals("Doesn't receive an error when the client send some bad commands", 
				Constants.PROGRAMM_NAME + "-Err incr/decr request one argument", mClient.increment(key));
		assertEquals("Doesn't receive an error when the client send some bad commands", 
				Constants.PROGRAMM_NAME + "-Err incr/decr request one argument", mClient.decrement(key));
	}
	
	@Test
	public void beFreeToSendBadHIncrementCommand() {
		String command = "hincr keyIncr ";
		assertEquals("Doesn't receive an error when the client send some bad commands", 
				Constants.PROGRAMM_NAME + "-Err hincr/hdecr request one argument",
				mClient.fakeCommand(command));
	}
	
	@Test
	public void beFreeToSendBadHDecrementCommand() {
		String command = "hdecr keyIncr ";
		assertEquals("Doesn't receive an error when the client send some bad commands", 
				Constants.PROGRAMM_NAME + "-Err hincr/hdecr request one argument",
				mClient.fakeCommand(command));
	}
	
	@Test
	public void beFreeToSendBadHIncrementCommandWithNoIntegerIncrementDecrement() {
		String command = "hincr keyIncr valueIncr";
		assertEquals("Doesn't receive an error when the client send some bad commands", 
				Constants.PROGRAMM_NAME + "-Err hincr/hdecr need an integer as argument",
				mClient.fakeCommand(command));
	}
	
	@Test
	public void beFreeToSendBadHDecrementCommandWithNoIntegerIncrementDecrement() {
		String command = "hdecr keyIncr valueIncr";
		assertEquals("Doesn't receive an error when the client send some bad commands", 
				Constants.PROGRAMM_NAME + "-Err hincr/hdecr need an integer as argument",
				mClient.fakeCommand(command));
	}
}