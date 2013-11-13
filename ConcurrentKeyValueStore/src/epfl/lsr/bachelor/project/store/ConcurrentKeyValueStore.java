package epfl.lsr.bachelor.project.store;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import epfl.lsr.bachelor.project.values.Value;

/**
 * Implementation of the key value store. Use simply the existing map.
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 * @param <K> The key type
 * @param <V> The value type
 */
public final class ConcurrentKeyValueStore{

	private static final ConcurrentKeyValueStore INSTANCE = new ConcurrentKeyValueStore();
	private Map<String, Value<?>> mMap;

	/**
	 * Default constructor that instantiate the {@link ConcurrentHashMap} encapsulated by this class
	 */
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
	
    /**
     * Enables to retrieve some value mapped to a key
     * 
     * @param key
     *            the key
     * @return the value mapped to the key, <code>null</code> if there is no
     *         value mapped
     */
	public Value<?> get(String key) {
		return mMap.get(key);
	}

    /**
     * Enables to associate some value to some key
     * 
     * @param key
     *            the key
     * @param value
     *            the value to be associated to the key
     */
	public void put(String key, Value<?> value) {
		mMap.put(key, value);
	}

    /**
     * Enables to remove the mapping for a key
     * 
     * @param key
     *            the key
     * @return the value to which this map previously associated the key, or
     *         <code>null</code> if the map contained no mapping for the key.
     */
	public Value<?> remove(String key) {
		return mMap.remove(key);
	}
}
