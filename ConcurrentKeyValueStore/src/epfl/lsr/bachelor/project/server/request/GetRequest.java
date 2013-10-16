package epfl.lsr.bachelor.project.server.request;

import epfl.lsr.bachelor.project.store.KeyValueStore;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class GetRequest extends Request {

	public GetRequest(String key) {
		super(key);
	}

	@Override
	public void perform() {
		@SuppressWarnings({ "unchecked"})
		KeyValueStore<String, Object> keyValueStore = (KeyValueStore<String, Object>) KeyValueStore.getInstance();
	}
}
