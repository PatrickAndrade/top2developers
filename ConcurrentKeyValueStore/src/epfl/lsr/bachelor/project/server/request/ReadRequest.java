package epfl.lsr.bachelor.project.server.request;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Represent a readable request
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public abstract class ReadRequest extends Request {

    protected ReadRequest(String key) {
        super(key);
    }
    
    @Override
    public void performAtomicAction(ReadWriteLock lock) {
        lock.readLock().lock();
        performAction();
        lock.readLock().unlock();
        notifyRequestPerformed(this);
    }

}

