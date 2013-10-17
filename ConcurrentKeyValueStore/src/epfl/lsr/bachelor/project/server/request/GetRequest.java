package epfl.lsr.bachelor.project.server.request;

import epfl.lsr.bachelor.project.values.Value;

/**
 * Represent a get request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class GetRequest extends Request {

	public GetRequest(String key) {
		super(key);
	}

	@Override
	public void perform() throws CloneNotSupportedException {
		Value<?> value = KEY_VALUE_STORE.get(getKey());
		if (value != null) {
			setMessageToReturn(value.toString());
		} else {
			setMessageToReturn("NIL");
		}
		notifyRequestPerformed();
	}
}
