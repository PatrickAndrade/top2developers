package epfl.lsr.bachelor.project.store;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * First implementation of the key value store. Use simply the existing map.
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 * @param <K>
 *            The key type
 * @param <V>
 *            The value type
 */
public final class KeyValueStore<K, V> implements Map<K, V> {

	private static final KeyValueStore<?, ?> INSTANCE = new KeyValueStore<>();
	private Map<K, V> mMap;

	private KeyValueStore() {
		if (INSTANCE != null) {
			throw new IllegalStateException("Already instantiated");
		}

		mMap = new HashMap<K, V>();
	}

	public static KeyValueStore<?, ?> getInstance() {
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
