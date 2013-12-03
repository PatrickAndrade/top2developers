package epfl.lsr.bachelor.project.store;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This class is used to nicely implement the readers-writer problem without
 * any priority
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class ReaderWriterHelper<T> {

    private Map<T, ReadWriteLock> mReadWriteLockMap = new HashMap<T, ReadWriteLock>();

    /**
     * Enables to retrieve the {@link ReadWriteLock} corresponding to the specified key
     * 
     * @param key
     *            the key that identifies the lock
     * 
     * @return the lock mapped by the key
     */
    public synchronized ReadWriteLock retrieveLock(T key) {
        ReadWriteLock lock = mReadWriteLockMap.get(key);
        if (lock != null) {
            return lock;
        }
        mReadWriteLockMap.put(key, new ReentrantReadWriteLock());
        return mReadWriteLockMap.get(key);
    }
}
