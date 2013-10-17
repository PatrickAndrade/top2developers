package epfl.lsr.bachelor.project.server.request;


/**
 * Represent a deletion request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class DelRequest extends Request {

	public DelRequest(String key) {
		super(key);
	}

	@Override
	public void perform() throws CloneNotSupportedException {
		if (KEY_VALUE_STORE.remove(getKey()) == null) {
			setMessageToReturn("-Err no such value");
		} else {
			setMessageToReturn("DELETED");
		}
		notifyRequestPerformed();
	}
}
