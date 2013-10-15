/*
 *	Author:      Grégory Ludovic Maitre
 *	Date:        14 oct. 2013
 */

package epfl.concurency.bachelorProject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * First implementation of the key value store.
 * Use simply the existing map.
 * @author Gregory Maitre
 *
 * @param <K> The key type
 * @param <V> The value type
 */
class KeyValueStore<K, V> implements Map<K, V> {

	private static final KeyValueStore<?, ?> INSTANCE = new KeyValueStore<>();
	private Map<K, V> map;
	
	
	private KeyValueStore() {
		if (INSTANCE != null) {
			throw new IllegalStateException("Already instantiated");
		}
		
		map = new HashMap<K, V>();
	}
	
	public static KeyValueStore<?, ?> getInstance() {
		return INSTANCE ;
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return map.entrySet();
	}

	@Override
	public V get(Object key) {
		return map.get(key);
	}

	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return map.keySet();
	}

	@Override
	public V put(K key, V value) {
		return map.put(key, value);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		map.putAll(m);
	}

	@Override
	public V remove(Object key) {
		return map.remove(key);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public Collection<V> values() {
		return map.values();
	}
}
