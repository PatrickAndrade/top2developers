package epfl.lsr.bachelor.project.values;

/**
 * String Value
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class ValueString extends Value<String> {
	
	public ValueString(String value) {
		super(value);
	}

	@Override
	// Returns false because Strings do not support this commands
	public boolean supportIncrementDecrement() {
		return false;
	}

	@Override
	// This method has no effect since Strings cannot be incremented
	public void increment(int k) {
		return;
	}

	@Override
	
	//This method has no effect since Strings cannot be decremented
	public void decrement(int k) {
		return;
	}

	@Override
	// This will return a clone of the ValueString
	public Value<String> clone() throws CloneNotSupportedException {
		return new ValueString(getValue());
	}

	@Override
	public String toString() {
		return getValue();
	}

}