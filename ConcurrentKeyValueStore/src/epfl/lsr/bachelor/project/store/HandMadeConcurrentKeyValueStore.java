package epfl.lsr.bachelor.project.store;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import epfl.lsr.bachelor.project.server.request.AtomicAction;
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
    private Map<String, Lock> mLocksMap;

    private HandMadeConcurrentKeyValueStore() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }

        mMap = new HashMap<String, Value<?>>();
        mLocksMap = new HashMap<String, Lock>();
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
        Lock myLock = retrieveLock(key);
        myLock.lock();
        action.performAtomicAction();
        myLock.unlock();
    }
    
    /**
     * Enables to retrieve the lock corresponding to the specified key
     * 
     * @param key the key that identifies the lock
     * 
     * @return the lock mapped by the key
     */
    private synchronized Lock retrieveLock(String key) {
        Lock mapLock = mLocksMap.get(key);
        if (mapLock != null) {
            return mapLock;
        }
        mLocksMap.put(key, new ReentrantLock());
        return mLocksMap.get(key);
    }
}
