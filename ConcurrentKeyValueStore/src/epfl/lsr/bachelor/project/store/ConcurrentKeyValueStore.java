package epfl.lsr.bachelor.project.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import epfl.lsr.bachelor.project.values.Value;

/**
 * Implementation of a concurrent key value store.
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class ConcurrentKeyValueStore extends KeyValueStore {

	private static final ConcurrentKeyValueStore INSTANCE = new ConcurrentKeyValueStore();
	private Map<String, Value<?>> mMap;

	private ConcurrentKeyValueStore() {
		if (INSTANCE != null) {
			throw new IllegalStateException("Already instantiated");
		}
		mMap = new ConcurrentHashMap<String, Value<?>>();
	}

	/**
	 * Enables to get the unique instance of the Key-Value store
	 * 
	 * @return the Key-Value store instance
	 */
	public static ConcurrentKeyValueStore getInstance() {
		return INSTANCE;
	}
	
	@Override
	public Value<?> get(String key) {
		return mMap.get(key);
	}

	@Override
	public Value<?> put(String key, Value<?> value) {
		return mMap.put(key, value);
	}

	@Override
	public Value<?> remove(String key) {
		return mMap.remove(key);
	}
}
