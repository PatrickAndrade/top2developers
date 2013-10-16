package epfl.lsr.bachelor.project.server.request;

import java.io.IOException;

import epfl.lsr.bachelor.project.store.KeyValueStore;
import epfl.lsr.bachelor.project.values.Value;

/**
 * Represent a get request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class GetRequest extends Request {

	public GetRequest(String key) {
		super(key);
	}

	@Override
	public void perform() throws CloneNotSupportedException {
		@SuppressWarnings({"unchecked"})
		KeyValueStore<String, Value<?>> keyValueStore = (KeyValueStore<String, Value<?>>) KeyValueStore.getInstance();
		Value<?> value = keyValueStore.get(getKey());
		if (value != null) {
			setValue(value.clone());
		}
		getConnection().notifyThatRequestIsPerformed();
	}

	@Override
	public void respond() throws IOException {
		if ((getValue() != null) && (getValue().getValue() != null)) {
			getConnection().getDataOutputStream().writeChars(getValue().getValue() + "\n");
		} else {
			getConnection().getDataOutputStream().writeChars("NIL\n");
		}
	}
}
