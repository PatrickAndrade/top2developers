package epfl.lsr.bachelor.project.server.request;

/**
 * Represent an decrement request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class DecrRequest extends Request {
	private int mDecrement;
	
	/**
	 * Construct the (h)decrement-request with the key and the decrement associated
	 * 
	 * @param key the key to map
	 * @param decrement the decrement wanted
	 */
	public DecrRequest(String key, int decrement) {
		super(key);
		mDecrement = decrement;
	}

	@Override
	public void performAtomicAction() {
	    setMessageToReturn(KEY_VALUE_STORE.decrement(getKey(), mDecrement));
		notifyRequestPerformed(this);
	}

}
