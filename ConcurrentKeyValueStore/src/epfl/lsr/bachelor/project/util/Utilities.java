package epfl.lsr.bachelor.project.util;
/**
 * Encapsulate some util functions
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class Utilities {
	/**
	 * Enables to know if a string represents an integer value
	 * 
	 * @param string the string to test
	 * @return does this string correspond to a number
	 */
	public static boolean isInteger(String string) {
		try {
			Integer.valueOf(string);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
