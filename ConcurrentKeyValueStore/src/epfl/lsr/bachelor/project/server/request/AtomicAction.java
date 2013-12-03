package epfl.lsr.bachelor.project.server.request;

import java.util.concurrent.locks.ReadWriteLock;

/**
 * This is an interface that enables the request to performed in an atomic way
 * on the KV
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
     * Enables to perform an action
     * 
     */
    void performAction();
}