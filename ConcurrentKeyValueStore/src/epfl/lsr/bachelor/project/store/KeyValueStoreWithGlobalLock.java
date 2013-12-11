package epfl.lsr.bachelor.project.store;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import epfl.lsr.bachelor.project.server.request.AtomicAction;
import epfl.lsr.bachelor.project.values.Value;

/**
 * Implementation of a KV-store that should be used with a
 * multi-threaded architecture. This uses a simple lock for all the keys as
 * synchronization mechanism
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class KeyValueStoreWithGlobalLock extends KeyValueStore {

    private static final ReadWriteLock GLOBAL_LOCK = new ReentrantReadWriteLock();

    private Map<String, Value<?>> mMap;

    public KeyValueStoreWithGlobalLock() {
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
        action.performAtomicAction(GLOBAL_LOCK);
    }
}
