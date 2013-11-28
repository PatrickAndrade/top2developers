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
    public void performAtomicAction(String key) {
        READER_WRITER_HELPER_OVER_KEYS.initWrite(key);
        performAction();
        READER_WRITER_HELPER_OVER_KEYS.endWrite(key);
        notifyRequestPerformed(this);
    }

    @Override
    public void performAtomicAction(int index) {
        READER_WRITER_HELPER_OVER_INDEXES.initWrite(index);
        performAction();
        READER_WRITER_HELPER_OVER_INDEXES.endWrite(index);
        notifyRequestPerformed(this);
    }

    @Override
    public void performAction() {
        setMessageToReturn(KEY_VALUE_STORE.increment(getKey(), mIncrement));
    }
}
