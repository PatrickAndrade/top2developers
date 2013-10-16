package epfl.lsr.bachelor.project.server.request;

import epfl.lsr.bachelor.project.values.Value;
import epfl.lsr.bachelor.project.values.ValueInteger;

/**
 * Represent an increment request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class IncrRequest extends Request {
		
	public IncrRequest(String key) {
		super(key);
	}

	@Override
	public void perform() throws CloneNotSupportedException {
		Value<?> valueStored = KEY_VALUE_STORE.get(getKey());
		if (valueStored == null) {
			KEY_VALUE_STORE.put(getKey(), new ValueInteger(1));
			setMessageToReturn("(integer) 1");
		} else {
			if (valueStored.supportIncrementDecrement()) {
				valueStored.increment(1);
				setMessageToReturn("(integer) " + valueStored);
			} else {
				setMessageToReturn("-Err not supported for this value");
			}
		}
		getConnection().notifyThatRequestIsPerformed();
	}
}
