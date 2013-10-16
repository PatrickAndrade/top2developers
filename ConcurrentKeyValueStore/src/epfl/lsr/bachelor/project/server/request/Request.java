package epfl.lsr.bachelor.project.server.request;

import java.io.IOException;

import epfl.lsr.bachelor.project.server.Connection;
import epfl.lsr.bachelor.project.store.KeyValueStore;
import epfl.lsr.bachelor.project.values.Value;

/**
 * Represent a request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
abstract public class Request {
	private String mKey;
	private String mMessageToReturn = "";
	private Value<?> mValue;
	private Connection mConnection;

	@SuppressWarnings("unchecked")
	protected static final KeyValueStore<String, Value<?>> KEY_VALUE_STORE =
		(KeyValueStore<String, Value<?>>) KeyValueStore.getInstance();

	public Request(String key) {
		mKey = key;
	}

	public Request(String key, Value<?> value) {
		mKey = key;
		mValue = value;
	}

	/**
	 * Call to perform the request. When we finish to peform the request, we
	 * must notify() the thread that wait for this monitor!
	 * 
	 * @throws CloneNotSupportedException
	 */
	abstract public void perform() throws CloneNotSupportedException;

	/**
	 * Call to respond
	 */
	public void respond() throws IOException {
		mConnection.getDataOutputStream().writeChars(mMessageToReturn + "\n");
	}

	public String getKey() {
		return mKey;
	}

	public Value<?> getValue() {
		return mValue;
	}

	protected void setValue(Value<?> value) {
		mValue = value;
	}

	public void setConnection(Connection connection) {
		mConnection = connection;
	}

	public Connection getConnection() {
		return mConnection;
	}

	public String getMessageToReturn() {
		return mMessageToReturn;
	}

	public void setMessageToReturn(String messageToReturn) {
		mMessageToReturn = messageToReturn;
	}
}
