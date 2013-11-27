package epfl.lsr.bachelor.project.server.request;

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
    public void performAtomicAction() {
        setMessageToReturn(KEY_VALUE_STORE.increment(getKey(), mIncrement));
        notifyRequestPerformed(this);
    }
}
