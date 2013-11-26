package epfl.lsr.bachelor.project.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import epfl.lsr.bachelor.project.server.request.AtomicAction;
import epfl.lsr.bachelor.project.util.Constants;
import epfl.lsr.bachelor.project.values.Value;

/**
 * Implementation of the key value store. Thought as multiples HashMaps
 * 
 * @author Patrick Andrade
 * 
 */
public final class ConcurrentArrayKeyValueStore extends KeyValueStore {
    private static final ConcurrentArrayKeyValueStore INSTANCE = new ConcurrentArrayKeyValueStore();
    private List<Map<String, Value<?>>> mHashMapsList;

    private ConcurrentArrayKeyValueStore() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }

        mHashMapsList = new ArrayList<Map<String, Value<?>>>();

        for (int i = 0; i < Constants.CONCURRENT_ARRAY_SIZE; i++) {
            mHashMapsList.add(new HashMap<String, Value<?>>());
        }
    }

    /**
     * Enables to get the unique instance of the Key-Value store
     * 
     * @return the Key-Value store instance
     */
    public static ConcurrentArrayKeyValueStore getInstance() {
        return INSTANCE;
    }

    @Override
    public Value<?> get(String key) {
        synchronized (mHashMapsList.get(getMapIndex(key))) {
            return mHashMapsList.get(getMapIndex(key)).get(key);
        }
    }

    @Override
    public Value<?> put(String key, Value<?> value) {
        synchronized (mHashMapsList.get(getMapIndex(key))) {
            return mHashMapsList.get(getMapIndex(key)).put(key, value);
        }
    }

    @Override
    public Value<?> remove(String key) {
        synchronized (mHashMapsList.get(getMapIndex(key))) {
            return mHashMapsList.get(getMapIndex(key)).remove(key);
        }
    }

    private int getMapIndex(String key) {
        return key.hashCode() % Constants.CONCURRENT_ARRAY_SIZE;
    }

    @Override
    public void modify(AtomicAction action, String key) {
        synchronized (mHashMapsList.get(getMapIndex(key))) {
            Lock myLock = retrieveLock(key);
            myLock.lock();
            action.performAtomicAction();
            myLock.unlock();
        }
    }

}
