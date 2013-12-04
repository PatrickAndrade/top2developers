package epfl.lsr.bachelor.project.server.request;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * Interface that enables the request to be performed in an atomic way
 * in the KV-store
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public interface AtomicAction {

    /**
     * Enables to perform an atomic action synchronized over a specified lock
     * 
     * @param lock
     *            the lock
     */
    void performAtomicAction(ReadWriteLock lock);


    /**
     * Enables to perform an action without any synchronization
     * 
     */
    void performAction();
}