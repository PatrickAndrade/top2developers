package epfl.lsr.bachelor.project.store;

import java.util.HashMap;
import java.util.Map;

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
    private static final ReaderWriterHelper<String> READER_WRITER_HELPER = new ReaderWriterHelper<String>();
    
    private Map<String, Value<?>> mMap;

    private HandMadeConcurrentKeyValueStore() {
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
    public static HandMadeConcurrentKeyValueStore getInstance() {
        return INSTANCE;
    }
    
    @Override
    public ReaderWriterHelper<String> getReaderWriterHelper() {
        return READER_WRITER_HELPER;
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
        action.performAtomicAction(key);
    }
}
