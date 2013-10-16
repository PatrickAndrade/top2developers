package epfl.lsr.bachelor.project.values;

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
		setValue(getValue() + k);
	}

	@Override
	// This will decrement the value by k, or in others words,
	// increment by -k
	public void decrement(int k) {
		increment(-k);
	}

}
