package epfl.lsr.bachelor.project.store;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import epfl.lsr.bachelor.project.values.Value;
import epfl.lsr.bachelor.project.server.request.AtomicAction;

/**
 * This interface enables to dynamically use different types of implementations
 * for the Key-Value store
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public abstract class KeyValueStore {

    private Map<String, Lock> mLocksMap;

    protected KeyValueStore() {
        mLocksMap = new HashMap<String, Lock>();
    }

    /**
     * Enables to retrieve some value mapped to a key
     * 
     * @param key
     *            the key
     * @return the value mapped to the key, <code>null</code> if there is no
     *         value mapped
     */
    public abstract Value<?> get(String key);

    /**
     * Enables to associate some value to some key
     * 
     * @param key
     *            the key
     * @param value
     *            the value to be associated to the key
     * @return the value already mapped to this key, <code>null</code> if there
     *         was no previous mapping
     */
    public abstract Value<?> put(String key, Value<?> value);

    /**
     * Enables to remove the mapping for a key
     * 
     * @param key
     *            the key
     * @return the value to which this map previously associated the key, or
     *         <code>null</code> if the map contained no mapping for the key.
     */
    public abstract Value<?> remove(String key);

    /**
     * Enables to perform some atomic action in the KV
     * 
     * @param action
     *            the atomic action to be done
     * @param key
     *            the key on which we should perform the action
     */
    public abstract void modify(AtomicAction action, String key);
    
    /**
     * Enables to retrieve the lock corresponding to the specified key
     * 
     * @param key the key that identifies the lock
     * 
     * @return the lock mapped by the key
     */
    public synchronized Lock retrieveLock(String key) {
        Lock mapLock = mLocksMap.get(key);
        if (mapLock != null) {
            return mapLock;
        }
        mLocksMap.put(key, new ReentrantLock());
        return mLocksMap.get(key);
    }
}
