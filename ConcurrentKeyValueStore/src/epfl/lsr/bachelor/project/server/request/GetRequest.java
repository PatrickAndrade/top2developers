package epfl.lsr.bachelor.project.server.request;

import java.io.IOException;

import epfl.lsr.bachelor.project.store.KeyValueStore;
import epfl.lsr.bachelor.project.values.Value;

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
		KeyValueStore<String, Value<?>> keyValueStore = (KeyValueStore<String, Value<?>>) KeyValueStore.getInstance();
		setValue(keyValueStore.get(getKey()));
		getConnection().notifyThatRequestIsPerformed();
	}

	@Override
	public void respond() throws IOException {
		getConnection().getDataOutputStream().writeChars(getValue().getValue() + "\n");
	}
}