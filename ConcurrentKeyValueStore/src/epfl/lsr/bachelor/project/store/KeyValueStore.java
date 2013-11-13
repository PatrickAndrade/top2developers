package epfl.lsr.bachelor.project.store;

import epfl.lsr.bachelor.project.values.Value;

/**
 * This interface enables to dynamically use different types of implementations
 * for the Key-Value store
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public abstract class KeyValueStore {

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
}
