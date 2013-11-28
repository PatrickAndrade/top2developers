package epfl.lsr.bachelor.project.server.request;

/**
 * Represent a get request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class PingRequest extends Request {

    public PingRequest() {
        super(null);
        setMessageToReturn("pong");
    }

    @Override
    public void performAtomicAction(String key) {
    }

    @Override
    public void performAtomicAction(int index) {
    }

    @Override
    public boolean canBePerformed() {
        return false;
    }

    @Override
    public void performAction() {
    }
}
