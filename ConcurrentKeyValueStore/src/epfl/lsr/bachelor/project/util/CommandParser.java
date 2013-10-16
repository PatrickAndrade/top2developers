package epfl.lsr.bachelor.project.util;

import epfl.lsr.bachelor.project.server.request.GetRequest;
import epfl.lsr.bachelor.project.server.request.Request;
import epfl.lsr.bachelor.project.server.request.SetRequest;
import epfl.lsr.bachelor.project.store.KeyValueStore;
import epfl.lsr.bachelor.project.values.Value;
import epfl.lsr.bachelor.project.values.ValueInteger;
import epfl.lsr.bachelor.project.values.ValueString;

/**
 * First implementation of a command parser to handle requests from clients.
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class CommandParser {

	private final static int GET_ARGUMENTS = 2;
	private final static int SET_ARGUMENTS = 3;

	public Request parse(String command) {
		String[] commandField = command.split(" ");

		if (commandField.length < 1) {
//			return "";
			return null;
		} else {
			switch (commandField[0]) {
				case "get":
					if (commandField.length < GET_ARGUMENTS) {
//						return "-ERR wrong number of arguments for 'get' command";
						return null;
					}
//					String value = KEYVALUESTORE.get(commandField[1]);
//					if (value == null) {
//						return "NIL";
//					}
					return new GetRequest(commandField[1]);
				case "set":
					if (commandField.length < SET_ARGUMENTS) {
//						return "-ERR wrong number of arguments for 'set' command";
						return null;
					}
//					KEYVALUESTORE.put(commandField[1], commandField[2]);
//					return "STORED";
					Value<?> value = null;
					if (isInteger(commandField[2])) {
						value = new ValueInteger(Integer.valueOf(commandField[2]));
					} else {
						value = new ValueString(commandField[2]);
					}
					
					return new SetRequest(commandField[1], value);
				case "quit":
				case "":
//					return "";
				default:
			}
//			return "-ERR unknown command '" + commandField[0] + "'";
			return null;
		}
	}
	
	public boolean isInteger(String string) {
		try {
			Integer.valueOf(string);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
