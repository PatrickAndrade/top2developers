package epfl.lsr.bachelor.project.server.request;

import epfl.lsr.bachelor.project.util.Constants;
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
	
	/**
	 * Construct the (h)decrement-request with the key and the decrement associated
	 * 
	 * @param key the key to map
	 * @param decrement the decrement wanted
	 */
	public DecrRequest(String key, int decrement) {
		super(key);
		mDecrement = decrement;
	}

	@Override
	public void performAtomicAction() {
		Value<?> valueStored = KEY_VALUE_STORE.get(getKey());
		// If there is not already a value stored we force to create it with initial value -decrement
		if (valueStored == null) {
			KEY_VALUE_STORE.put(getKey(), new ValueInteger(-mDecrement));
			setMessageToReturn(Constants.INTEGER + " " + -mDecrement);
		} else {
			// If the value stored support to be decremented we do this otherwise we return an error message
			if (valueStored.supportIncrementDecrement()) {
				valueStored.decrement(mDecrement);
				setMessageToReturn(Constants.INTEGER + " " + valueStored);
			} else {
				setMessageToReturn(Constants.NOT_SUPPORTED);
			}
		}
		notifyRequestPerformed(this);
	}

}
