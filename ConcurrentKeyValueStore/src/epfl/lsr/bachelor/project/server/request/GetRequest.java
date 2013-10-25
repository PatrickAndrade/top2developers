package epfl.lsr.bachelor.project.server.request;

import epfl.lsr.bachelor.project.values.Value;

/**
 * Represent a get request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class GetRequest extends Request {

	/**
	 * Construct the get-request with the key that maps the wanted value
	 * 
	 * @param key the key that maps the wanted value
	 */
	public GetRequest(String key) {
		super(key);
	}

	@Override
	public void perform() throws CloneNotSupportedException {
		Value<?> value = KEY_VALUE_STORE.get(getKey());
		// If there exists a value mapped by this key we return it otherwise we return NIL
		if (value != null) {
			setMessageToReturn(value.toString());
		} else {
			setMessageToReturn("NIL");
		}
		notifyRequestPerformed(this);
	}
}
