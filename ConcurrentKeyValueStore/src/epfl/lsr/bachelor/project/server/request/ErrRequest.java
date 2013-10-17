package epfl.lsr.bachelor.project.server.request;
/**
 * Represent an error of command requested
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class ErrRequest extends Request {
	
	public ErrRequest(String errorMessage) {
		super(null);
		setMessageToReturn(errorMessage);
	}

	@Override
	public boolean canBePerformed() {
		return false;
	}
	
	@Override
	public void perform() throws CloneNotSupportedException {
	}
}
