package epfl.lsr.bachelor.project.server.request;

/**
 * Represent an error of command requested
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class ErrRequest extends Request {

    /**
     * Construct an error request that will simply contain an error-message
     * 
     * @param errorMessage
     *            the error-message to send to the client
     */
    public ErrRequest(String errorMessage) {
        super(null);
        setMessageToReturn(errorMessage);
    }

    @Override
    public boolean canBePerformed() {
        return false;
    }

    @Override
    public void performAtomicAction(String key) {
    }

    @Override
    public void performAtomicAction(int index) {
    }

    @Override
    public void performAction() {
    }
}
