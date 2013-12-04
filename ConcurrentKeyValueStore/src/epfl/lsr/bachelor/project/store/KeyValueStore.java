package epfl.lsr.bachelor.project.store;

import epfl.lsr.bachelor.project.server.request.AtomicAction;
import epfl.lsr.bachelor.project.util.Constants;
import epfl.lsr.bachelor.project.values.Value;
import epfl.lsr.bachelor.project.values.ValueInteger;

/**
 * Abstract super-class of the different types of implementations
 * for the KV-store
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public abstract class KeyValueStore {

    /**
     * Enables to perform some atomic action in the KV-store
     * 
     * @param action
     *            the atomic action to be done
     * @param key
     *            the key on which we should perform the action
     */
    public abstract void execute(AtomicAction action, String key);

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
     * Enables to increment the mapping for a certain key
     * 
     * @param key
     *            the key
     * @return a string containing some useful message that could be used
     */
    public String increment(String key, int increment) {
        Value<?> valueStored = this.get(key);

        // If there is not already a value stored we force to create it with
        // initial value increment
        if (valueStored == null) {
            this.put(key, new ValueInteger(increment));
            return Constants.INTEGER + " " + increment;
        } else {

            // If the value stored support to be incremented we do this
            // otherwise we return an error message
            if (valueStored.supportIncrementDecrement()) {
                valueStored.increment(increment);
                return Constants.INTEGER + " " + valueStored;
            } else {
                return Constants.NOT_SUPPORTED;
            }
        }
    }

    /**
     * Enables to decrement the mapping for a certain key
     * 
     * @param key
     *            the key
     * @return a string containing some useful message that could be used
     */
    public String decrement(String key, int decrement) {
        Value<?> valueStored = this.get(key);

        // If there is not already a value stored we force to create it with
        // initial value -decrement
        if (valueStored == null) {
            this.put(key, new ValueInteger(-decrement));
            return Constants.INTEGER + " " + -decrement;
        } else {

            // If the value stored support to be decremented we do this
            // otherwise we return an error message
            if (valueStored.supportIncrementDecrement()) {
                valueStored.decrement(decrement);
                return Constants.INTEGER + " " + valueStored;
            } else {
                return Constants.NOT_SUPPORTED;
            }
        }
    }

    /**
     * Enables to append some value to the mapping for a certain key
     * 
     * @param key
     *            the key
     * @param toAppend
     *            the value to be append
     * @return a string containing some useful message that could be used
     */
    public String append(String key, Value<?> toAppend) {
        Value<?> value = this.get(key);

        if (value == null) {
            value = toAppend;
            this.put(key, value);
        }

        if (value != toAppend) {
            // Return null if we can append directly in this instance of Value
            Value<?> newValue = value.append(toAppend);
            if (newValue != null) {
                this.put(key, newValue);
                value = newValue;
            }
        }
        if (value.supportIncrementDecrement()) {
            return "(integer) " + value.getValue();
        } else {
            return "(integer) " + ((StringBuilder) value.getValue()).length();
        }
    }
}
