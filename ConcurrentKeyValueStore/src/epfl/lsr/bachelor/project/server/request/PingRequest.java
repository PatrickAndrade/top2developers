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
	public void perform() throws CloneNotSupportedException {
	}

	@Override
	public boolean canBePerformed() {
		return false;
	}
}
