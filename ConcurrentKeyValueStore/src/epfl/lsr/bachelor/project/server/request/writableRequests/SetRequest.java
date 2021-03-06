package epfl.lsr.bachelor.project.server.request.writableRequests;

import epfl.lsr.bachelor.project.server.request.WriteRequest;
import epfl.lsr.bachelor.project.util.Constants;
import epfl.lsr.bachelor.project.values.Value;

/**
 * Represent a set request
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class SetRequest extends WriteRequest {

    /**
     * Construct the set-request with the key and the value associated
     * 
     * @param key
     *            the key to map
     * @param value
     *            the value to be mapped
     */
    public SetRequest(String key, Value<?> value) {
        super(key, value);
    }

    @Override
    public void performAction() {
        if (KEY_VALUE_STORE.put(getKey(), getValue()) == null) {
            setMessageToReturn(Constants.STORED);
        } else {
            setMessageToReturn(Constants.REPLACED);
        }
    }
}
