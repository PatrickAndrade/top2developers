package epfl.lsr.bachelor.project.store;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

/**
 * This class is used to nicely implement the readers-writer problem without
 * any priority
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class ReaderWriterHelper<T> {

    private Map<T, Semaphore> mReaderWriterMap = new HashMap<T, Semaphore>();
    private Map<T, Semaphore> mWriterMap = new HashMap<T, Semaphore>();
    private Map<T, Semaphore> mMutex = new HashMap<T, Semaphore>();

    private int readersTotal = 0;

    /**
     * Enables to initiate a reading action
     * 
     * @param key
     *            the key
     */
    public void initRead(T key) {
        try {
            retrieveSemaphore(key, mReaderWriterMap).acquire();
            retrieveSemaphore(key, mMutex).acquire();
            readersTotal++;
            if (readersTotal == 1) {
                retrieveSemaphore(key, mWriterMap).acquire();
            }

        } catch (InterruptedException e) {
        } finally {
            retrieveSemaphore(key, mMutex).release();
            retrieveSemaphore(key, mReaderWriterMap).release();
        }
    }

    /**
     * Enables to end a reading action
     * 
     * @param key
     *            the key
     */
    public void endRead(T key) {
        try {
            retrieveSemaphore(key, mMutex).acquire();
            readersTotal--;
            if (readersTotal == 0) {
                retrieveSemaphore(key, mWriterMap).release();
            }
        } catch (InterruptedException e) {
        } finally {
            retrieveSemaphore(key, mMutex).release();
        }
    }

    /**
     * Enables to initiate a writing action
     * 
     * @param key
     *            the key
     */
    public void initWrite(T key) {
        try {
            retrieveSemaphore(key, mReaderWriterMap).acquire();
            retrieveSemaphore(key, mWriterMap).acquire();
        } catch (InterruptedException e) {
        }
    }

    /**
     * Enables to end a writing action
     * 
     * @param key
     *            the key
     */
    public void endWrite(T key) {
        retrieveSemaphore(key, mWriterMap).release();
        retrieveSemaphore(key, mReaderWriterMap).release();
    }

    /**
     * Enables to retrieve the semaphore corresponding to the specified key
     * 
     * @param key
     *            the key that identifies the lock
     * 
     * @param map
     *            the map where we should lookup for the specified key
     * 
     * @return the semaphore mapped by the key
     */
    private synchronized Semaphore retrieveSemaphore(T key, Map<T, Semaphore> map) {
        Semaphore mapSemaphore = map.get(key);
        if (mapSemaphore != null) {
            return mapSemaphore;
        }
        map.put(key, new Semaphore(1));
        return map.get(key);
    }
}
