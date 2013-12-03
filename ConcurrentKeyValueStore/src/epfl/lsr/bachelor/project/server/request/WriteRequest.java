package epfl.lsr.bachelor.project.server.request;

import java.util.concurrent.locks.ReadWriteLock;

import epfl.lsr.bachelor.project.values.Value;

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
    
    public WriteRequest(String key, Value<?> value) {
        super(key, value);
    }
    
    @Override
    public void performAtomicAction(ReadWriteLock lock) {
        lock.writeLock().lock();
        performAction();
        lock.writeLock().unlock();
        notifyRequestPerformed(this);
    }
}

