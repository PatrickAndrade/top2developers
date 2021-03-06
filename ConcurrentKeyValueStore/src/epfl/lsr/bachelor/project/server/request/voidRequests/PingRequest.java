package epfl.lsr.bachelor.project.server.request.voidRequests;

import epfl.lsr.bachelor.project.server.request.VoidRequest;

/**
 * Represent a ping request
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class PingRequest extends VoidRequest {

    public PingRequest() {
        super(null);
        setMessageToReturn("pong");
    }

    @Override
    public boolean canBePerformed() {
        return false;
    }

    @Override
    public void performAction() {
    }
}
