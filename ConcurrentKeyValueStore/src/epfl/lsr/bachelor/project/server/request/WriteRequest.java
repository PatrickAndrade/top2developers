package epfl.lsr.bachelor.project.server.request;

/**
 * Represent a writable request
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public abstract class WriteRequest extends Request {

    public WriteRequest(String key) {
        super(key);
    }

}

