package epfl.lsr.bachelor.project.server.request.writableRequests;

import epfl.lsr.bachelor.project.server.request.WriteRequest;
import epfl.lsr.bachelor.project.values.Value;

/**
 * Represent an append request
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class AppendRequest extends WriteRequest {

    public AppendRequest(String key, Value<?> value) {
        super(key, value);
    }

    @Override
    public void performAction() {
        setMessageToReturn(KEY_VALUE_STORE.append(getKey(), getValue()));
    }

}
