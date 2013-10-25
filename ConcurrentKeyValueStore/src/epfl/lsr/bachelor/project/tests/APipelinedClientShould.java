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

import epfl.lsr.bachelor.project.client.PipelinedClient;
import epfl.lsr.bachelor.project.server.Server;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * Tests of the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class APipelinedClientShould {

	private PipelinedClient mClient;

//	@BeforeClass
//	public static void doFirst() {
//		new Thread(new Runnable() {
//
//			public void run() {
//				Server.start();
//
//			}
//		}).start();
//	}
//
//	@AfterClass
//	public static void doLast() {
//		Server.stop();
//	}

	@Before
	public void init() throws UnknownHostException {
		mClient = new PipelinedClient(InetAddress.getLocalHost(),
				Constants.PORT);
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
	public void enableToQuit() throws InterruptedException {
		mClient.quit();
		mClient.getNextAnswerFromServer();
		mClient.disconnect();
		assertTrue("Can't quit the server!", !mClient.isConnected());
	}

	@Test
	public void enableToGetNILIfAKeyDoesNotExistInMemory() {
		mClient.get("NILKey");
		assertEquals("Unable to get a key with no value",
				Constants.PROGRAMM_NAME + "NIL",
				mClient.getNextAnswerFromServer());
	}

	@Test
	public void enableToSetAValue() {
		String key = "setKey";
		String value = "setValue";
		mClient.set(key, value);
		assertEquals("Unable to set a key with a value",
				Constants.PROGRAMM_NAME + "STORED",
				mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void enableToSetAValueWithInteger() {
		String key = "setKey";
		String value = "4";
		mClient.set(key, value);
		assertEquals("Unable to set a key with a value",
				Constants.PROGRAMM_NAME + "STORED",
				mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void enableToSetAndGetAValue() {
		String value = "someValue";
		String key = "someKey";
		mClient.set(key, value);
		mClient.getNextAnswerFromServer();
		mClient.get(key);
		assertEquals("Unable to set a key with a value and get it",
				Constants.PROGRAMM_NAME + value,
				mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void enableToSetAKeyWithANewValue() {
		String key = "setKey";
		String value = "setValue";
		mClient.set(key, value);
		mClient.getNextAnswerFromServer();
		mClient.get(key);
		assertEquals("Unable to set a key with a value and get it",
				Constants.PROGRAMM_NAME + value,
				mClient.getNextAnswerFromServer());
		String newValue = "setNewValue";
		mClient.set(key, newValue);
		mClient.getNextAnswerFromServer();
		mClient.get(key);
		assertEquals("Unable to set a key (with a value) with a new value",
				Constants.PROGRAMM_NAME + newValue,
				mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void unableToDeleteAKeyWithNoValue() {
		String key = "deleteValue";
		mClient.delete(key);
		assertEquals("Enable to delete a key with no value",
				Constants.PROGRAMM_NAME + "-Err no such value",
				mClient.getNextAnswerFromServer());
	}

	@Test
	public void enableToSetAndDeleteAValue() {
		String value = "deleteValue";
		String key = "deleteKey";
		mClient.set(key, value);
		mClient.getNextAnswerFromServer();
		mClient.delete(key);
		assertEquals("Unable to delete a key", Constants.PROGRAMM_NAME
				+ "DELETED", mClient.getNextAnswerFromServer());
	}

	@Test
	public void enableToIncrementANonExistingValue() {
		String key = "incrValue";
		mClient.increment(key);
		assertEquals("Unable to increment a non existing key",
				Constants.PROGRAMM_NAME + "(integer) 1",
				mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void enableToIncrementAnExistingValue() {
		String key = "incrValue";
		mClient.increment(key);
		mClient.getNextAnswerFromServer();
		mClient.increment(key);
		assertEquals("Unable to increment an existing key",
				Constants.PROGRAMM_NAME + "(integer) 2",
				mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void enableToIncrementAnExistingValueWithASpecificNumber() {
		String key = "incrValue";
		final int value = 4;
		mClient.increment(key, value);
		assertEquals(
				"Unable to increment an existing key with a specific value",
				Constants.PROGRAMM_NAME + "(integer) 4",
				mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void unableToDoAnOverflowWithIncrement() {
		String key = "keyIncrement";
		mClient.increment(key);
		mClient.getNextAnswerFromServer();
		mClient.increment(key, Integer.MAX_VALUE);
		assertEquals("Enable to do an overflow with Increment",
				Constants.PROGRAMM_NAME + "(integer) 1",
				mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void unableToIncrementAnNonInteger() {
		String key = "keyIncrement";
		String value = "valueIncrement";
		mClient.set(key, value);
		mClient.getNextAnswerFromServer();
		mClient.increment(key);
		assertEquals("Enable to increment a string", Constants.PROGRAMM_NAME
				+ "-Err not supported for this value", mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void enableToDecrementANonExistingValue() {
		String key = "decrValue";
		mClient.decrement(key);
		assertEquals("Unenable to decrement a non existing key",
				Constants.PROGRAMM_NAME + "(integer) -1",
				mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void enableToDecrementAnExistingValue() {
		String key = "decrValue";
		mClient.decrement(key);
		mClient.getNextAnswerFromServer();
		mClient.decrement(key);
		assertEquals("Unable to decrement an existing key",
				Constants.PROGRAMM_NAME + "(integer) -2",
				mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void enableTodecrementAnExistingValueWithASpecificNumber() {
		String key = "decrValue";
		final int value = 4;
		mClient.decrement(key, value);
		assertEquals(
				"Unable to decrement an existing key with a specific value",
				Constants.PROGRAMM_NAME + "(integer) -4",
				mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void unableToDoAnOverflowWithDecrement() {
		String key = "keyDecrement";
		mClient.decrement(key);
		mClient.getNextAnswerFromServer();
		mClient.decrement(key, Integer.MIN_VALUE);
		assertEquals("Enable to do an overflow with Decrement",
				Constants.PROGRAMM_NAME + "(integer) -1",
				mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void unableToDecrementAnNonInteger() {
		String key = "keyDecrement";
		String value = "valueDecrement";
		mClient.set(key, value);
		mClient.getNextAnswerFromServer();
		mClient.decrement(key);
		assertEquals("Enable to decrement a string", Constants.PROGRAMM_NAME
				+ "-Err not supported for this value", mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void unableToHaveAndEmptyKey() {
		String key = "";
		final int value = 4;

		mClient.set(key, String.valueOf(value));
		assertEquals("Enable to set an empty key", Constants.PROGRAMM_NAME
				+ "-Err '" + Constants.EMPTY_STRING + "' can't be a key",
				mClient.getNextAnswerFromServer());
		mClient.increment(key, value);
		assertEquals("Enable to set an empty key", Constants.PROGRAMM_NAME
				+ "-Err '" + Constants.EMPTY_STRING + "' can't be a key",
				mClient.getNextAnswerFromServer());
		mClient.decrement(key, value);
		assertEquals("Enable to set an empty key", Constants.PROGRAMM_NAME
				+ "-Err '" + Constants.EMPTY_STRING + "' can't be a key",
				mClient.getNextAnswerFromServer());
	}

	@Test
	public void enableToPingTheServer() {
		mClient.ping();
		assertEquals("Can't ping the server", Constants.PROGRAMM_NAME + "pong",
				mClient.getNextAnswerFromServer());
	}

	@Test
	public void enableToAppendAKeyWithNoValue() {
		String key = "keyAppend";
		String value = "valueAppend";
		mClient.append(key, value);
		assertEquals("Can't append a key with no value",
				Constants.PROGRAMM_NAME + "(integer) " + value.length(),
				mClient.getNextAnswerFromServer());
		mClient.get(key);
		assertEquals("Can't append a key with no value",
				Constants.PROGRAMM_NAME + value, mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void enableToAppendAKeyWithAValue() {
		String key = "keyAppend";
		String value1 = "value";
		String value2 = "Append";
		mClient.append(key, value1);
		assertEquals("Can't append a key with a value", Constants.PROGRAMM_NAME
				+ "(integer) " + value1.length(), mClient.getNextAnswerFromServer());
		mClient.get(key);
		assertEquals("Can't append a key with a value", Constants.PROGRAMM_NAME
				+ value1, mClient.getNextAnswerFromServer());

		mClient.append(key, value2);
		assertEquals("Can't append a key with a value", Constants.PROGRAMM_NAME
				+ "(integer) " + (value1.length() + value2.length()),
				mClient.getNextAnswerFromServer());
		mClient.get(key);
		assertEquals("Can't append a key with a value", Constants.PROGRAMM_NAME
				+ value1 + value2, mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void beFreeToSendBadCommand() {
		String command = "fake key value";
		mClient.fakeCommand(command);
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				Constants.PROGRAMM_NAME
						+ "-Err unable to execute command 'fake'",
				mClient.getNextAnswerFromServer());
	}

	@Test
	public void beFreeToSendNoCommand() {
		String command = "";
		mClient.fakeCommand(command);
		assertEquals("Receive an error when the client send nothing",
				Constants.PROGRAMM_NAME, mClient.getNextAnswerFromServer());
	}

	@Test
	public void beFreeToSendBadGetCommand() {
		String key = "";
		mClient.get(key);
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				Constants.PROGRAMM_NAME + "-Err get request one argument",
				mClient.getNextAnswerFromServer());
		mClient.delete(key);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void beFreeToSendBadSetCommand() {
		String keyValue = "";
		mClient.set(keyValue, keyValue);
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				Constants.PROGRAMM_NAME + "-Err set request two arguments",
				mClient.getNextAnswerFromServer());
		mClient.delete(keyValue);
		mClient.getNextAnswerFromServer();
	}

	@Test
	public void beFreeToSendBadIncrementDecrementCommand() {
		String key = "";
		mClient.increment(key);
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				Constants.PROGRAMM_NAME + "-Err incr/decr request one argument",
				mClient.getNextAnswerFromServer());
		mClient.decrement(key);
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				Constants.PROGRAMM_NAME + "-Err incr/decr request one argument",
				mClient.getNextAnswerFromServer());
	}

	@Test
	public void beFreeToSendBadHIncrementCommand() {
		String command = "hincr keyIncr ";
		mClient.fakeCommand(command);
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				Constants.PROGRAMM_NAME
						+ "-Err hincr/hdecr request one argument",
				mClient.getNextAnswerFromServer());
	}

	@Test
	public void beFreeToSendBadHDecrementCommand() {
		String command = "hdecr keyIncr ";
		mClient.fakeCommand(command);
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				Constants.PROGRAMM_NAME
						+ "-Err hincr/hdecr request one argument",
				mClient.getNextAnswerFromServer());
	}

	@Test
	public void beFreeToSendBadHIncrementCommandWithNoIntegerIncrementDecrement() {
		String command = "hincr keyIncr valueIncr";
		mClient.fakeCommand(command);
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				Constants.PROGRAMM_NAME
						+ "-Err hincr/hdecr need an integer as argument",
				mClient.getNextAnswerFromServer());
	}

	@Test
	public void beFreeToSendBadHDecrementCommandWithNoIntegerIncrementDecrement() {
		String command = "hdecr keyIncr valueIncr";
		mClient.fakeCommand(command);
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				Constants.PROGRAMM_NAME
						+ "-Err hincr/hdecr need an integer as argument",
				mClient.getNextAnswerFromServer());
	}
}