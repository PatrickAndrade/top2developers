package epfl.lsr.bachelor.project.server.request.writableRequests;

import epfl.lsr.bachelor.project.server.request.WriteRequest;

/**
 * Represent an increment(by) request
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class IncrRequest extends WriteRequest {
    private int mIncrement;

    /**
     * Construct the increment(by)-request with the key and the increment associated
     * 
     * @param key
     *            the key to map
     * @param increment
     *            the increment wanted
     */
    public IncrRequest(String key, int increment) {
        super(key);
        mIncrement = increment;
    }

    @Override
    public void performAction() {
        setMessageToReturn(KEY_VALUE_STORE.increment(getKey(), mIncrement));
    }
}
