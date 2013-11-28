package epfl.lsr.bachelor.project.server.request;

import epfl.lsr.bachelor.project.util.Constants;

/**
 * Represent a deletion request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class DelRequest extends Request {

    /**
     * Construct the deletion-request with the key that maps the value that we
     * want to delete
     * 
     * @param key
     *            the key that map the value
     */
    public DelRequest(String key) {
        super(key);
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
        // If there is a value mapped by the key we delete it otherwise we
        // tell the client that this key doesn't contain a value mapped
        if (KEY_VALUE_STORE.remove(getKey()) == null) {
            setMessageToReturn(Constants.NO_SUCH_VALUE);
        } else {
            setMessageToReturn(Constants.DELETED);
        }
    }
}
