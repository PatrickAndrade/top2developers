package epfl.lsr.bachelor.project.server.request;

import epfl.lsr.bachelor.project.values.Value;

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
        setMessageToReturn(KEY_VALUE_STORE.append(getKey(), getValue()));
    }

}
