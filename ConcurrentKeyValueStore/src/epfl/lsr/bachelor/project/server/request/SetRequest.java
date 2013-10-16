package epfl.lsr.bachelor.project.server.request;

import java.io.IOException;

import epfl.lsr.bachelor.project.store.KeyValueStore;
import epfl.lsr.bachelor.project.values.Value;

/**
 * Represent a set request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class SetRequest extends Request {

	public SetRequest(String key, Value<?> value) {
		super(key, value);
	}

	@Override
	public void perform() {
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		KeyValueStore<String, Value<?>> keyValueStore = (KeyValueStore<String, Value<?>>) KeyValueStore.getInstance();
		keyValueStore.put(getKey(), getValue());
		getConnection().notifyThatRequestIsPerformed();
	}

	@Override
	public void respond() throws IOException {
		getConnection().getDataOutputStream().writeChars("Done\n");
	}

}
