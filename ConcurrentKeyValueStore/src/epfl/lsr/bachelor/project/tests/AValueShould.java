package epfl.lsr.bachelor.project.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import epfl.lsr.bachelor.project.values.Value;
import epfl.lsr.bachelor.project.values.ValueInteger;
import epfl.lsr.bachelor.project.values.ValueString;

/**
 * General tests for the value hierarchy
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class AValueShould {
	private Value<?> mValue;

	@Test
	public void enableToBeInitializedAsValueString() {
		final String value = "testValue";
		mValue = new ValueString(value);
		assertEquals(value, ((StringBuilder) mValue.getValue()).toString());
	}

	@Test
	public void enableToBeInitializedAsValueInteger() {
		final int value = 1;
		mValue = new ValueInteger(value);
		assertEquals(value, ((Integer) mValue.getValue()).intValue());
	}

	@Test
	public void notSupportIncrementDecrementIfItIsAValueString() {
		mValue = new ValueString("testValue");
		assertFalse(
				"The ValueString should not support increment/decrement command",
				mValue.supportIncrementDecrement());
		mValue.increment(1);
		assertEquals(
				"The ValueString should not support increment/decrement command",
				"testValue", ((StringBuilder) mValue.getValue()).toString());
		mValue.decrement(1);
		assertEquals(
				"The ValueString should not support increment/decrement command",
				"testValue", ((StringBuilder) mValue.getValue()).toString());
	}

	@Test
	public void supportIncrementDecrementIfItIsAValueInteger() {
		final int value = 2;
		mValue = new ValueInteger(value);
		assertTrue(
				"The ValueInteger should support incrment/decrement command",
				mValue.supportIncrementDecrement());
	}

	@Test
	public void notBeIncrementedIfItHasAMaxIntegerValue() {
		final int maxValue = Integer.MAX_VALUE;
		final int increment = 1;
		mValue = new ValueInteger(maxValue);
		mValue.increment(increment);
		assertEquals(maxValue, ((Integer) mValue.getValue()).intValue());
	}

	@Test
	public void notBeIncrementedIfItHasAMaxIntegerValueUsingDecrementWithANegativeNumber() {
		final int maxValue = Integer.MAX_VALUE;
		final int decrement = -1;
		mValue = new ValueInteger(maxValue);
		mValue.decrement(decrement);
		assertEquals(maxValue, ((Integer) mValue.getValue()).intValue());
	}

	@Test
	public void notBeDecrementedIfItHasAMinIntegerValue() {
		final int minValue = Integer.MIN_VALUE;
		final int decrement = 1;
		mValue = new ValueInteger(minValue);
		mValue.decrement(decrement);
		assertEquals(minValue, ((Integer) mValue.getValue()).intValue());
	}

	@Test
	public void notBeDecrementedIfItHasAMinIntegerValueUsingIncrementWithANegativeNumber() {
		final int minValue = Integer.MIN_VALUE;
		final int increment = -1;
		mValue = new ValueInteger(minValue);
		mValue.increment(increment);
		assertEquals(minValue, ((Integer) mValue.getValue()).intValue());
	}

	@Test
	public void canAppendAValueStringWithAValueInteger() {
		mValue = new ValueString("String");
		final int number = 3;
		Value<Integer> value = new ValueInteger(number);
		mValue.append(value);
		assertEquals("Can't union two value!", "String3",
				((StringBuilder) mValue.getValue()).toString());
	}
}