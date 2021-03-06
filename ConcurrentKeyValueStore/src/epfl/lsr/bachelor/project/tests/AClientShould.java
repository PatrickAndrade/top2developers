package epfl.lsr.bachelor.project.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import epfl.lsr.bachelor.project.client.Client;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * General tests for a client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class AClientShould {

	private Client mClient;

	@Before
	public void init() throws UnknownHostException {
		mClient = new Client(new InetSocketAddress("127.0.0.1", Constants.PORT));
		mClient.connect();
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
		mClient.disconnect();
		assertTrue("Can't quit the server!", !mClient.isConnected());
	}

	@Test
	public void enableToGetNILIfAKeyDoesNotExistInMemory() {
		assertEquals("Unable to get a key with no value", "NIL",
				mClient.get("NILKey"));
	}

	@Test
	public void enableToSetAValue() {
		String key = "setKey";
		String value = "setValue";
		assertEquals("Unable to set a key with a value", "STORED",
				mClient.set(key, value));
		mClient.delete(key);
	}

	@Test
	public void enableToSetAValueWithInteger() {
		String key = "setKey";
		String value = "4";
		assertEquals("Unable to set a key with a value", "STORED",
				mClient.set(key, value));
		mClient.delete(key);
	}

	@Test
	public void enableToSetAndGetAValue() {
		String value = "someValue";
		String key = "someKey";
		mClient.set(key, value);
		assertEquals("Unable to set a key with a value and get it", value,
				mClient.get(key));
		mClient.delete(key);
	}

	@Test
	public void enableToSetAKeyWithANewValue() {
		String key = "setKey";
		String value = "setValue";
		mClient.set(key, value);
		assertEquals("Unable to set a key with a value and get it", value,
				mClient.get(key));
		String newValue = "setNewValue";
		mClient.set(key, newValue);
		assertEquals("Unable to set a key (with a value) with a new value",
				newValue, mClient.get(key));
		mClient.delete(key);
	}

	@Test
	public void unableToDeleteAKeyWithNoValue() {
		String key = "deleteValue";
		assertEquals("Enable to delete a key with no value",
				"-Err no such value", mClient.delete(key));
	}

	@Test
	public void enableToSetAndDeleteAValue() {
		String value = "deleteValue";
		String key = "deleteKey";
		mClient.set(key, value);
		assertEquals("Unable to delete a key", "DELETED", mClient.delete(key));
	}

	@Test
	public void enableToIncrementANonExistingValue() {
		String key = "incrValue";
		assertEquals("Unable to increment a non existing key", "(integer) 1",
				mClient.increment(key));
		mClient.delete(key);
	}

	@Test
	public void enableToIncrementAnExistingValue() {
		String key = "incrValue";
		mClient.increment(key);
		assertEquals("Unable to increment an existing key", "(integer) 2",
				mClient.increment(key));
		mClient.delete(key);
	}

	@Test
	public void enableToIncrementAnExistingValueWithASpecificNumber() {
		String key = "incrValue";
		final int value = 4;
		assertEquals(
				"Unable to increment an existing key with a specific value",
				"(integer) 4", mClient.increment(key, value));
		mClient.delete(key);
	}

	@Test
	public void unableToDoAnOverflowWithIncrement() {
		String key = "keyIncrement";
		mClient.increment(key);
		assertEquals("Enable to do an overflow with Increment", "(integer) 1",
				mClient.increment(key, Integer.MAX_VALUE));
		mClient.delete(key);
	}

	@Test
	public void unableToIncrementAnNonInteger() {
		String key = "keyIncrement";
		String value = "valueIncrement";
		mClient.set(key, value);
		assertEquals("Enable to increment a string",
				"-Err not supported for this value", mClient.increment(key));
		mClient.delete(key);
	}

	@Test
	public void enableToDecrementANonExistingValue() {
		String key = "decrValue";
		assertEquals("Unenable to decrement a non existing key",
				"(integer) -1", mClient.decrement(key));
		mClient.delete(key);
	}

	@Test
	public void enableToDecrementAnExistingValue() {
		String key = "decrValue";
		mClient.decrement(key);
		assertEquals("Unable to decrement an existing key", "(integer) -2",
				mClient.decrement(key));
		mClient.delete(key);
	}

	@Test
	public void enableTodecrementAnExistingValueWithASpecificNumber() {
		String key = "decrValue";
		final int value = 4;
		assertEquals(
				"Unable to decrement an existing key with a specific value",
				"(integer) -4", mClient.decrement(key, value));
		mClient.delete(key);
	}

	@Test
	public void unableToDoAnOverflowWithDecrement() {
		String key = "keyDecrement";
		mClient.decrement(key);
		assertEquals("Enable to do an overflow with Decrement", "(integer) -1",
				mClient.decrement(key, Integer.MIN_VALUE));
		mClient.delete(key);
	}

	@Test
	public void unableToDecrementAnNonInteger() {
		String key = "keyDecrement";
		String value = "valueDecrement";
		mClient.set(key, value);
		assertEquals("Enable to decrement a string",
				"-Err not supported for this value", mClient.decrement(key));
		mClient.delete(key);
	}

	@Test
	public void unableToHaveAndEmptyKey() {
		String key = "";
		final int value = 4;

		assertEquals("Enable to set an empty key", "-Err '"
				+ Constants.EMPTY_STRING + "' can't be a key",
				mClient.set(key, String.valueOf(value)));
		assertEquals("Enable to set an empty key", "-Err '"
				+ Constants.EMPTY_STRING + "' can't be a key",
				mClient.increment(key, value));
		assertEquals("Enable to set an empty key", "-Err '"
				+ Constants.EMPTY_STRING + "' can't be a key",
				mClient.decrement(key, value));
	}

	@Test
	public void enableToPingTheServer() {
		assertEquals("Can't ping the server", "pong", mClient.ping());
	}

	@Test
	public void enableToAppendAKeyWithNoValue() {
		String key = "keyAppend";
		String value = "valueAppend";
		assertEquals("Can't append a key with no value",
				"(integer) " + value.length(), mClient.append(key, value));
		assertEquals("Can't append a key with no value", value,
				mClient.get(key));
		mClient.delete(key);
	}

	@Test
	public void enableToAppendAKeyWithAValue() {
		String key = "keyAppend";
		String value1 = "value";
		String value2 = "Append";
		assertEquals("Can't append a key with a value",
				"(integer) " + value1.length(), mClient.append(key, value1));
		assertEquals("Can't append a key with a value", value1,
				mClient.get(key));

		assertEquals("Can't append a key with a value",
				"(integer) " + (value1.length() + value2.length()),
				mClient.append(key, value2));
		assertEquals("Can't append a key with a value", value1 + value2,
				mClient.get(key));
		mClient.delete(key);
	}

	@Test
	public void beFreeToSendBadCommand() {
		String command = "fake key value";
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				"-Err unable to execute command 'fake'",
				mClient.customCommand(command));
	}

	@Test
	public void beFreeToSendNoCommand() {
		String command = "";
		assertEquals("Receive an error when the client send nothing",
				Constants.EMPTY_STRING, mClient.customCommand(command));
	}

	@Test
	public void beFreeToSendBadGetCommand() {
		String key = "";
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				"-Err get request one argument", mClient.get(key));
		mClient.delete(key);
	}

	@Test
	public void beFreeToSendBadSetCommand() {
		String keyValue = "";
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				"-Err set request two arguments",
				mClient.set(keyValue, keyValue));
		mClient.delete(keyValue);
	}

	@Test
	public void beFreeToSendBadIncrementDecrementCommand() {
		String key = "";
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				"-Err incr/decr request one argument", mClient.increment(key));
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				"-Err incr/decr request one argument", mClient.decrement(key));
	}

	@Test
	public void beFreeToSendBadIncrementByCommand() {
		String command = "incrby keyIncr ";
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				"-Err incrby/decrby request one argument",
				mClient.customCommand(command));
	}

	@Test
	public void beFreeToSendBadDecrementByCommand() {
		String command = "decrby keyIncr ";
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				"-Err incrby/decrby request one argument",
				mClient.customCommand(command));
	}

	@Test
	public void beFreeToSendBadIncrementByCommandWithNoIntegerIncrementDecrement() {
		String command = "incrby keyIncr valueIncr";
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				"-Err incrby/decrby need an integer as argument",
				mClient.customCommand(command));
	}

	@Test
	public void beFreeToSendBadDecrementByCommandWithNoIntegerIncrementDecrement() {
		String command = "decrby keyIncr valueIncr";
		assertEquals(
				"Doesn't receive an error when the client send some bad commands",
				"-Err incrby/decrby need an integer as argument",
				mClient.customCommand(command));
	}

	@Test
	public void beFreeToSendRealyBigValue() {
		String key = "key";
		String value = "value";
		
		final int nbValue = 10;
		for (int i = 0; i < nbValue; i++) {
			value += value;
		}
		
		mClient.set(key, value);
		assertEquals("Can't set a big value and get it", value, mClient.get(key));
		mClient.delete(key);
	}
}
