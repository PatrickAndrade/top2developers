package epfl.lsr.bachelor.project.server.request;

import java.io.IOException;

import epfl.lsr.bachelor.project.server.Connection;
import epfl.lsr.bachelor.project.values.Value;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
abstract public class Request {
	private String mKey;
	private Value<?> mValue;
	private Connection mConnection;

	public Request(String key) {
		mKey = key;
	}

	public Request(String key, Value<?> value) {
		mKey = key;
		mValue = value;
	}

	/**
	 * Call to perform the request. When we finish to peform the request,
	 * we must notify() the thread that wait for this monitor!
	 */
	abstract public void perform();

	/**
	 * Call to respond
	 */
	abstract public void respond() throws IOException;

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
}
