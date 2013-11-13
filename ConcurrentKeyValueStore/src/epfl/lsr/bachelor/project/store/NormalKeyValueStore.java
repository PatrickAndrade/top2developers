package epfl.lsr.bachelor.project.store;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of the key value store. Use simply the existing map.
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 * @param <K> The key type
 * @param <V> The value type
 */
public final class NormalKeyValueStore<K, V> implements Map<K, V> {

	private static final NormalKeyValueStore<?, ?> INSTANCE = new NormalKeyValueStore<>();
	private Map<K, V> mMap;

	/**
	 * Default constructor that instantiate the {@link HashMap} encapsulated by this class
	 */
	private NormalKeyValueStore() {
		if (INSTANCE != null) {
			throw new IllegalStateException("Already instantiated");
		}

		mMap = new HashMap<K, V>();
	}

	/**
	 * Enables to get the unique instance of the Key-Value store
	 * 
	 * @return the Key-Value store instance
	 */
	public static NormalKeyValueStore<?, ?> getInstance() {
		return INSTANCE;
	}

	@Override
	public void clear() {
		mMap.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return mMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return mMap.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return mMap.entrySet();
	}

	@Override
	public V get(Object key) {
		return mMap.get(key);
	}

	@Override
	public boolean isEmpty() {
		return mMap.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return mMap.keySet();
	}

	@Override
	public V put(K key, V value) {
		return mMap.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		mMap.putAll(m);
	}

	@Override
	public V remove(Object key) {
		return mMap.remove(key);
	}

	@Override
	public int size() {
		return mMap.size();
	}

	@Override
	public Collection<V> values() {
		return mMap.values();
	}
}
