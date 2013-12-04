package epfl.lsr.bachelor.project.util;

/**
 * Class that enables to use useful functions
 * 
 * @author Gregory Maitre & Patrick Andrade
 */
public final class Utilities {

    /**
     * Enables to know if a string represents an integer value
     * 
     * @param string
     *            the string to test
     * @return true if the string corresponds to an integer value, false
     *         otherwise
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
