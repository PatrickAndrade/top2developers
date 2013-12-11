package epfl.lsr.bachelor.project.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import epfl.lsr.bachelor.project.server.request.AtomicAction;
import epfl.lsr.bachelor.project.util.Constants;
import epfl.lsr.bachelor.project.values.Value;

/**
 * Implementation of a KV-store that should be used with a multi-threaded
 * architecture. This uses a lock for each map where a key is binded as
 * synchronization mechanism
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class KeyValueStoreWithMapLocks extends KeyValueStore {
    private static final ReaderWriterHelper<Integer> READER_WRITER_HELPER = new ReaderWriterHelper<Integer>();

    private List<Map<String, Value<?>>> mHashMapsList;

    public KeyValueStoreWithMapLocks() {

        mHashMapsList = new ArrayList<Map<String, Value<?>>>();

        for (int i = 0; i < Constants.CONCURRENT_ARRAY_SIZE; i++) {
            mHashMapsList.add(new HashMap<String, Value<?>>());
        }
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
        int index = getMapIndex(key);
        action.performAtomicAction(READER_WRITER_HELPER.retrieveLock(index));
    }

    /**
     * Calculates the index of a certain key in order to find to which map it's
     * binded
     * 
     * @param key
     *            the key
     * @return the index of the map
     */
    private int getMapIndex(String key) {
        return Math.abs(key.hashCode() % Constants.CONCURRENT_ARRAY_SIZE);
    }

}
