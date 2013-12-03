package epfl.lsr.bachelor.project.store;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import epfl.lsr.bachelor.project.server.request.AtomicAction;
import epfl.lsr.bachelor.project.values.Value;

/**
 * Implementation of a concurrent key value store.
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class KeyValueStoreWithGlobalLock extends KeyValueStore {

	private static final KeyValueStoreWithGlobalLock INSTANCE = new KeyValueStoreWithGlobalLock();
	private static final ReadWriteLock GLOBAL_LOCK = new ReentrantReadWriteLock();
	
	private Map<String, Value<?>> mMap;

	private KeyValueStoreWithGlobalLock() {
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
	public static KeyValueStoreWithGlobalLock getInstance() {
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
        action.performAtomicAction(GLOBAL_LOCK);
    }
}
