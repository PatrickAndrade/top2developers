package epfl.lsr.bachelor.project.values;

import epfl.lsr.bachelor.project.util.Utilities;

/**
 * Abstract Value contained in the Key-Value store
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public abstract class Value<K> implements Cloneable {
	private K mValue;

	/**
	 * Constructor of the object
	 * @param value the value to be represented
	 */
	public Value(K value) {
		this.mValue = value;
	}
	
	/**
	 * This enables to get the value
	 * @return the value represented by this object
	 */
	public K getValue() {
		return mValue;
	};
	
	/**
	 * Set the value to the value given as newValue
	 * @param newValue the new value to be set
	 */
	public void setValue(K newValue) {
		this.mValue = newValue;
	}
	
	/**
	 * This enables to know if this kind of value support the commands Incr/Decr
	 * @return true or false depending on the value itself
	 */
	public abstract boolean supportIncrementDecrement();
	
	/**
	 * Enables to increment the value if it's supported by the value
	 * 
	 * @param k the number of increment
	 */
	public abstract void increment(int k);
	
	/**
	 * Enables to decrement the value if it's supported by the value
	 * 
	 * @param k the number of decrement
	 */
	public abstract void decrement(int k);
	
	/**
	 * Enables to append some value
	 * 
	 * @param value the value to be append
	 * 
	 * @return the new value resulting from appending
	 */
	public Value<?> append(Value<?> value) {
		Value<?> newValue = new ValueString(String.valueOf(getValue()) + value.getValue());
		if (Utilities.isInteger((String) newValue.getValue())) {
			newValue = new ValueInteger(Integer.valueOf((String) newValue.getValue()));
		}
		
		return newValue;
	}
	
	@Override
	public abstract String toString();
}