package epfl.lsr.bachelor.project.server.request;

import epfl.lsr.bachelor.project.util.Constants;
import epfl.lsr.bachelor.project.values.Value;

/**
 * Represent a set request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class SetRequest extends Request {

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
        if (KEY_VALUE_STORE.put(getKey(), getValue()) == null) {
            setMessageToReturn(Constants.STORED);
        } else {
            setMessageToReturn(Constants.REPLACED);
        }
    }
}
