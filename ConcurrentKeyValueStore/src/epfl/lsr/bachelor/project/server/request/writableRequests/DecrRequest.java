package epfl.lsr.bachelor.project.server.request.writableRequests;

import epfl.lsr.bachelor.project.server.request.WriteRequest;

/**
 * Represent a decrement request
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class DecrRequest extends WriteRequest {
    private int mDecrement;

    /**
     * Construct the decrement(by)-request with the key and the decrement
     * associated
     * 
     * @param key
     *            the key to map
     * @param decrement
     *            the decrement wanted
     */
    public DecrRequest(String key, int decrement) {
        super(key);
        mDecrement = decrement;
    }

    @Override
    public void performAction() {
        setMessageToReturn(KEY_VALUE_STORE.decrement(getKey(), mDecrement));
    }

}
