package epfl.lsr.bachelor.project.server.request;

import epfl.lsr.bachelor.project.values.Value;
import epfl.lsr.bachelor.project.values.ValueInteger;

/**
 * Represent an decrement request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class DecrRequest extends Request {
	
	private int mDecrement;
	
	public DecrRequest(String key, int decrement) {
		super(key);
		mDecrement = decrement;
	}

	@Override
	public void perform() throws CloneNotSupportedException {
		Value<?> valueStored = KEY_VALUE_STORE.get(getKey());
		if (valueStored == null) {
			KEY_VALUE_STORE.put(getKey(), new ValueInteger(-mDecrement));
			setMessageToReturn("(integer) " + -mDecrement);
		} else {
			if (valueStored.supportIncrementDecrement()) {
				valueStored.decrement(mDecrement);
				setMessageToReturn("(integer) " + valueStored);
			} else {
				setMessageToReturn("-Err not supported for this value");
			}
		}
		getConnection().notifyThatRequestIsPerformed();
	}

}
