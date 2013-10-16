package epfl.lsr.bachelor.project.server.request;
/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
abstract public class Request {
	private String mKey;
	
	public Request(String key) {
		mKey = key;
	}
	/**
	 * Call to perform the request
	 */
	abstract public void perform();

	public String getKey() {
		return mKey;
	}
}
