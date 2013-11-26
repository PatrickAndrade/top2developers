package epfl.lsr.bachelor.project.store;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

import epfl.lsr.bachelor.project.server.request.AtomicAction;
import epfl.lsr.bachelor.project.values.Value;

/**
 * Implementation of the key value store
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class NormalKeyValueStore extends KeyValueStore {

	private static final NormalKeyValueStore INSTANCE = new NormalKeyValueStore();
	private Map<String, Value<?>> mMap;
        
	private NormalKeyValueStore() {
	    super();
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
	public static KeyValueStore getInstance() {
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
