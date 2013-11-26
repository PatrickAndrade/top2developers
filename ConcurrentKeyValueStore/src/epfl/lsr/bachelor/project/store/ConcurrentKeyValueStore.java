package epfl.lsr.bachelor.project.store;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

import epfl.lsr.bachelor.project.server.request.AtomicAction;
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
        Lock myLock = retrieveLock(key);
        myLock.lock();
        Value<?> toReturn = mMap.get(key);
        myLock.unlock();
        return toReturn;
    }

    @Override
    public Value<?> put(String key, Value<?> value) {
        Lock myLock = retrieveLock(key);
        myLock.lock();
        Value<?> toReturn = mMap.put(key, value);
        myLock.unlock();
        return toReturn;
    }

    @Override
    public Value<?> remove(String key) {
        Lock myLock = retrieveLock(key);
        myLock.lock();
        Value<?> toReturn = mMap.remove(key);
        myLock.unlock();
        return toReturn;
    }

    @Override
    public void modify(AtomicAction action, String key) {
        Lock myLock = retrieveLock(key);
        myLock.lock();
        action.performAtomicAction();
        myLock.unlock();
    }
}
