package epfl.lsr.bachelor.project.server.request;

import epfl.lsr.bachelor.project.values.Value;
import epfl.lsr.bachelor.project.values.ValueString;

/**
 * Represent a get request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class AppendRequest extends Request {

	public AppendRequest(String key, Value<?> value) {
		super(key, value);
	}
	
	@Override
	public void perform() throws CloneNotSupportedException {
		Value<?> value = KEY_VALUE_STORE.get(getKey());
		
		if (value == null) {
			value = new ValueString("");
		}
		
		value = value.append(getValue());
		KEY_VALUE_STORE.put(getKey(), value);
		
		if (value.supportIncrementDecrement()) {
			setMessageToReturn("(integer) " + value.getValue());
		} else {
			setMessageToReturn("(integer) " + ((String) value.getValue()).length());
		}
		
		notifyRequestPerformed();
	}

}
