package epfl.lsr.bachelor.project.values;


/**
 * This is an abstract value contained in the KV-store
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public abstract class Value<K> {
	private K mValue;

	/**
	 * Constructor of the value
	 * 
	 * @param value the value to be represented
	 */
	protected Value(K value) {
		this.mValue = value;
	}
	
	/**
	 * Enables to get the value
	 * 
	 * @return the value represented by this object
	 */
	public K getValue() {
		return mValue;
	};
	
	/**
	 * Enables to set the value to the value given as newValue
	 * 
	 * @param newValue the new value to be set
	 */
	public void setValue(K newValue) {
		this.mValue = newValue;
	}
	
	/**
	 * Enables to know if this kind of value support the commands incr/decr
	 * 
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
	abstract public Value<?> append(Value<?> value);
	
	@Override
	public abstract String toString();
}