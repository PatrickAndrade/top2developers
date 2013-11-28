package epfl.lsr.bachelor.project.server.request;

/**
 * This is an interface that enables the request to performed in an atomic way
 * on the KV
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public interface AtomicAction {

    /**
     * Enables to perform an atomic action synchronized over a specified key
     * 
     * @param key
     *            the key
     */
    void performAtomicAction(String key);

    /**
     * Enables to perform an atomic action synchronized over a specified index
     * 
     * @param index
     *            the index
     */
    void performAtomicAction(int index);

    /**
     * Enables to perform an action
     * 
     */
    void performAction();
}