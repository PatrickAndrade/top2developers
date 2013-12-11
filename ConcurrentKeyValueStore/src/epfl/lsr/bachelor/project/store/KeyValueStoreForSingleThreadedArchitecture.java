package epfl.lsr.bachelor.project.store;

import java.util.HashMap;
import java.util.Map;

import epfl.lsr.bachelor.project.server.request.AtomicAction;
import epfl.lsr.bachelor.project.values.Value;

/**
 * Implementation of a KV-store that should be used only with a single-threaded
 * architecture, otherwise this will not ensure any kind of synchronization
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class KeyValueStoreForSingleThreadedArchitecture extends KeyValueStore {

    private Map<String, Value<?>> mMap;

    public KeyValueStoreForSingleThreadedArchitecture() {
        mMap = new HashMap<String, Value<?>>();
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

    @Override
    public void execute(AtomicAction action, String key) {
        action.performAction();
    }
}
