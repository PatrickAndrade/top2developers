package epfl.lsr.bachelor.project.values;

/**
 * String Value
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class ValueString extends Value<StringBuilder> {

	public ValueString(String value) {
		super(new StringBuilder(value));
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
	// This method has no effect since Strings cannot be decremented
	public void decrement(int k) {
		return;
	}

	@Override
	public String toString() {
		return getValue().toString();
	}

	public Value<?> append(Value<?> value) {
		if (value.supportIncrementDecrement()) {
			getValue().append(value.getValue());
		} else {
			getValue().append(((StringBuilder) value.getValue()).toString());
		}
		return null;
	}
}