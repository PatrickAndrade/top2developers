package epfl.lsr.bachelor.project.server.request;

import epfl.lsr.bachelor.project.util.Constants;


/**
 * Represent a deletion request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class DelRequest extends Request {

	/**
	 * Construct the deletion-request with
	 * the key that maps the value that we want to delete
	 * 
	 * @param key the key that map the value
	 */
	public DelRequest(String key) {
		super(key);
	}

	@Override
	public void perform() throws CloneNotSupportedException {
		// If there is a value mapped by the key we delete it otherwise we
		// tell the client that this key doesn't contain a value mapped
		if (KEY_VALUE_STORE.remove(getKey()) == null) {
			setMessageToReturn(Constants.NO_SUCH_VALUE);
		} else {
			setMessageToReturn(Constants.DELETED);
		}
		notifyRequestPerformed();
	}
}
