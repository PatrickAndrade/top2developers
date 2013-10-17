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
		
	private int mIncrement;
	
	public IncrRequest(String key, int increment) {
		super(key);
		mIncrement = increment;
	}

	@Override
	public void perform() throws CloneNotSupportedException {
		Value<?> valueStored = KEY_VALUE_STORE.get(getKey());
		if (valueStored == null) {
			KEY_VALUE_STORE.put(getKey(), new ValueInteger(mIncrement));
			setMessageToReturn("(integer) " + mIncrement);
		} else {
			if (valueStored.supportIncrementDecrement()) {
				valueStored.increment(mIncrement);
				setMessageToReturn("(integer) " + valueStored);
			} else {
				setMessageToReturn("-Err not supported for this value");
			}
		}
		notifyRequestPerformed();
	}
}
