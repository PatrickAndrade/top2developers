package epfl.lsr.bachelor.project.server.request;

import epfl.lsr.bachelor.project.values.Value;

/**
 * Represent a get request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class GetRequest extends Request {

    /**
     * Construct the get-request with the key that maps the wanted value
     * 
     * @param key
     *            the key that maps the wanted value
     */
    public GetRequest(String key) {
        super(key);
    }

    @Override
    public void performAtomicAction(String key) {
        READER_WRITER_HELPER_OVER_KEYS.initRead(key);
        performAction();
        READER_WRITER_HELPER_OVER_KEYS.endRead(key);
        notifyRequestPerformed(this);
    }

    @Override
    public void performAtomicAction(int index) {
        READER_WRITER_HELPER_OVER_INDEXES.initRead(index);
        performAction();
        READER_WRITER_HELPER_OVER_INDEXES.endRead(index);
        notifyRequestPerformed(this);
    }

    @Override
    public void performAction() {
        Value<?> value = KEY_VALUE_STORE.get(getKey());
        // If there exists a value mapped by this key we return it otherwise we
        // return NIL
        if (value != null) {
            setMessageToReturn(value.toString());
        } else {
            setMessageToReturn("NIL");
        }
    }
}
