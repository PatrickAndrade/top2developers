package epfl.lsr.bachelor.project.server.request;

import epfl.lsr.bachelor.project.values.Value;

/**
 * Represent a set request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class SetRequest extends Request {

	public SetRequest(String key, Value<?> value) {
		super(key, value);
	}

	@Override
	public void perform() {
		if (KEY_VALUE_STORE.put(getKey(), getValue()) == null) {
			setMessageToReturn("STORED");
		} else {
			setMessageToReturn("REPLACED");
		}
		getConnection().notifyThatRequestIsPerformed();
	}
}
