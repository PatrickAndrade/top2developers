package epfl.lsr.bachelor.project.server.request;
/**
 * This is an interface that enables the request to performed in an atomic way on the KV
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public interface AtomicAction {
    
    /**
     * Enables to execute an atomic action
     */
    void performAtomicAction();
}