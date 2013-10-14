/*
 *	Author:      Grégory Ludovic Maitre
 *	Date:        15 oct. 2013
 */

package epfl.concurency.bachelorProject;

public class CommandParser {

	@SuppressWarnings("unchecked")
	final KeyValueStore<String, String> KEYVALUESTORE = (KeyValueStore<String, String>) KeyValueStore.getInstance();
	
	public String parse(String command) {
		String[] commandField = command.split(" ");
		
		if (commandField.length < 1) {
			return "Error";
		}

		switch(commandField[0]) {
		case "get":
			if (commandField.length < 2) {
				return "Error";
			}
			String value = KEYVALUESTORE.get(commandField[1]);
			if (value == null) {
				return "Error";
			}
			return value;
		case "set":
			if (commandField.length < 3) {
				return "Error";
			}
			KEYVALUESTORE.put(commandField[1], commandField[2]);
			return "Done";
		case "quit":
			break;
		default:
			return "Error";
		}
		
		return "";
	}
}
