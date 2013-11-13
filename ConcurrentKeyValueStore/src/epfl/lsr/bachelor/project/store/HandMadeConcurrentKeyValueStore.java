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
public final class HandMadeConcurrentKeyValueStore extends KeyValueStore {

    private static final HandMadeConcurrentKeyValueStore INSTANCE = new HandMadeConcurrentKeyValueStore();
    private Map<String, Value<?>> mMap;

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

    @Override
    public Value<?> get(String key) {
        Value<?> value = null;
        synchronized (key.intern()) {
            value = mMap.get(key);
        }
        return value;
    }

    @Override
    public Value<?> put(String key, Value<?> value) {
        Value<?> previousValue = null;
        synchronized (key.intern()) {
            previousValue = mMap.put(key, value);
        }
        return previousValue;
    }

    @Override
    public Value<?> remove(String key) {
        Value<?> value = null;
        synchronized (key.intern()) {
            value = mMap.remove(key);
        }
        return value;
    }
}
