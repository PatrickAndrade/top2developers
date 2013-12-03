package epfl.lsr.bachelor.project.server.request;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Represent a void request (like ping requests for example)
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public abstract class VoidRequest extends Request {

    public VoidRequest(String key) {
        super(key);
    }
    
    @Override
    public void performAtomicAction(ReadWriteLock lock) {
    }

}

