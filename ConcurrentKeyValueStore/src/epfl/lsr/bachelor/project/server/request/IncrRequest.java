package epfl.lsr.bachelor.project.server.request;

import epfl.lsr.bachelor.project.util.Constants;
import epfl.lsr.bachelor.project.values.Value;
import epfl.lsr.bachelor.project.values.ValueInteger;

/**
 * Represent an (h)increment request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class IncrRequest extends Request {
	private int mIncrement;
	
	/**
	 * Construct the increment-request with the key and the increment associated
	 * 
	 * @param key the key to map
	 * @param increment the increment wanted
	 */
	public IncrRequest(String key, int increment) {
		super(key);
		mIncrement = increment;
	}

    @Override
    public void performAtomicAction() {
        Value<?> valueStored = KEY_VALUE_STORE.get(getKey());
        // If there is not already a value stored we force to create it with initial value increment
        if (valueStored == null) {
            KEY_VALUE_STORE.put(getKey(), new ValueInteger(mIncrement));
            setMessageToReturn(Constants.INTEGER + " " + mIncrement);
        } else {
            // If the value stored support to be incremented we do this otherwise we return an error message
            if (valueStored.supportIncrementDecrement()) {
                valueStored.increment(mIncrement);
                setMessageToReturn(Constants.INTEGER + " " + valueStored);
            } else {
                setMessageToReturn(Constants.NOT_SUPPORTED);
            }
        }
        notifyRequestPerformed(this);  
    }
}
