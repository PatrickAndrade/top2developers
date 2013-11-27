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
    public void performAtomicAction() {
        setMessageToReturn(KEY_VALUE_STORE.append(getKey(), getValue()));
        notifyRequestPerformed(this);
    }

}
