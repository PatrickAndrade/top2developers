package epfl.lsr.bachelor.project.store;

import java.util.HashMap;
import java.util.Map;

import epfl.lsr.bachelor.project.values.Value;

/**
 * Implementation of the key value store. Use simply the existing map.
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class HandMadeConcurrentKeyValueStore {

    private static final HandMadeConcurrentKeyValueStore INSTANCE = new HandMadeConcurrentKeyValueStore();
    private Map<String, Value<?>> mMap;

    /**
     * Default constructor that instantiate the {@link HashMap} encapsulated by
     * this class
     */
    private HandMadeConcurrentKeyValueStore() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }

        mMap = new HashMap<String, Value<?>>();
    }

    /**
     * Enables to get the unique instance of the Key-Value store
     * 
     * @return the Key-Value store instance
     */
    public static HandMadeConcurrentKeyValueStore getInstance() {
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
        Value<?> value = null;
        synchronized (key.intern()) {
            value = mMap.get(key);
        }
        return value;
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
        synchronized (key.intern()) {
            mMap.put(key, value);
        }
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
        Value<?> value = null;
        synchronized (key.intern()) {
            value = mMap.remove(key);
        }
        return value;
    }
}
