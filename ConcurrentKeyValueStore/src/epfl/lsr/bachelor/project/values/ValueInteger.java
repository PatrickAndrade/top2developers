package epfl.lsr.bachelor.project.values;

import epfl.lsr.bachelor.project.util.Utilities;

/**
 * Integer Value
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class ValueInteger extends Value<Integer> {

	public ValueInteger(int value) {
		super(value);
	}

	@Override
	public boolean supportIncrementDecrement() {
		return true;
	}

	@Override
	// This will increment the value by k
	public void increment(int k) {
		if ((double) getValue() / (double) Integer.MAX_VALUE + (double) k
				/ (double) Integer.MAX_VALUE <= 1
				&& (double) getValue() / (double) Integer.MIN_VALUE
						+ (double) k / (double) Integer.MIN_VALUE <= 1) {
			setValue(getValue() + k);
		}
	}

	@Override
	// This will decrement the value by k, or in others words,
	// increment by -k
	public void decrement(int k) {
		increment(-k);
	}
	
	@Override
	public String toString() {
		return getValue().toString();
	}

	public Value<?> append(Value<?> value) {
		String newValueString = String.valueOf(getValue()) + String.valueOf(value.getValue());
		Value<?> newValue = null;
		if (!Utilities.isInteger(newValueString)) {
			newValue = new ValueString(newValueString);
		} else {
			setValue(Integer.valueOf(newValueString));
		}
		
		return newValue;
	}
}
