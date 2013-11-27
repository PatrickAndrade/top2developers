package epfl.lsr.bachelor.project.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import epfl.lsr.bachelor.project.server.request.AtomicAction;
import epfl.lsr.bachelor.project.util.Constants;
import epfl.lsr.bachelor.project.values.Value;

/**
 * Implementation of the key value store. Thought as multiples HashMaps
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class ConcurrentArrayKeyValueStore extends KeyValueStore {
    private static final ConcurrentArrayKeyValueStore INSTANCE = new ConcurrentArrayKeyValueStore();
    private List<Map<String, Value<?>>> mHashMapsList;
    private Map<Integer, Lock> mLocksMap;

    private ConcurrentArrayKeyValueStore() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Already instantiated");
        }

        mHashMapsList = new ArrayList<Map<String, Value<?>>>();
        mLocksMap = new HashMap<Integer, Lock>();

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
        return mHashMapsList.get(getMapIndex(key)).get(key);
    }

    @Override
    public Value<?> put(String key, Value<?> value) {
        return mHashMapsList.get(getMapIndex(key)).put(key, value);
    }

    @Override
    public Value<?> remove(String key) {
        return mHashMapsList.get(getMapIndex(key)).remove(key);
    }

    @Override
    public void execute(AtomicAction action, String key) {
        Lock myLock = retrieveLock(getMapIndex(key));
        myLock.lock();
        action.performAtomicAction();
        myLock.unlock();
    }

    /**
     * Enables to retrieve the lock associated to the given map (index)
     * 
     * @param index
     * @return the lock associated with the map, if there's no mapping it
     *         creates one
     */
    private synchronized Lock retrieveLock(Integer index) {
        Lock mapLock = mLocksMap.get(index);
        if (mapLock != null) {
            return mapLock;
        }
        mLocksMap.put(index, new ReentrantLock());
        return mLocksMap.get(index);
    }

    /**
     * Calculates the index of a certain key in order to find to which map it's
     * bind
     * 
     * @param key
     * @return
     */
    private int getMapIndex(String key) {
        return key.hashCode() % Constants.CONCURRENT_ARRAY_SIZE;
    }

}
