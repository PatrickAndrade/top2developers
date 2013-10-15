package epfl.lsr.bachelor.project.util;

import epfl.lsr.bachelor.project.store.KeyValueStore;

/**
 * First implementation of a command parser to handle requests from clients.
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class CommandParser {

	@SuppressWarnings("unchecked")
	private final static KeyValueStore<String, String> KEYVALUESTORE = (KeyValueStore<String, String>) KeyValueStore
			.getInstance();

	private final static int GET_ARGUMENTS = 2;
	private final static int SET_ARGUMENTS = 3;

	public String parse(String command) {
		String[] commandField = command.split(" ");

		if (commandField.length < 1) {
			return "";
		} else {
			switch (commandField[0]) {
				case "get":
					if (commandField.length < GET_ARGUMENTS) {
						return "-ERR wrong number of arguments for 'get' command";
					}
					String value = KEYVALUESTORE.get(commandField[1]);
					if (value == null) {
						return "NIL";
					}
					return value;
				case "set":
					if (commandField.length < SET_ARGUMENTS) {
						return "-ERR wrong number of arguments for 'set' command";
					}
					KEYVALUESTORE.put(commandField[1], commandField[2]);
					return "STORED";
				case "quit":
				case "":
					return "";
				default:
			}
			return "-ERR unknown command '" + commandField[0] + "'";
		}
	}
}
