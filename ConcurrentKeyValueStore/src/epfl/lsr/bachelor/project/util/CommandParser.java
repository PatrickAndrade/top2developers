package epfl.lsr.bachelor.project.util;

import epfl.lsr.bachelor.project.server.request.DecrRequest;
import epfl.lsr.bachelor.project.server.request.DelRequest;
import epfl.lsr.bachelor.project.server.request.GetRequest;
import epfl.lsr.bachelor.project.server.request.IncrRequest;
import epfl.lsr.bachelor.project.server.request.Request;
import epfl.lsr.bachelor.project.server.request.SetRequest;
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
	private static final int DEL_ARGUMENTS = 2;
	private static final int INCR_DECR_ARGUMENTS = 2;

	public Request parse(String command) {
		String[] commandField = command.split(" ");

		if (commandField.length < 1) {
			return null;
		} else {
			switch (commandField[0]) {
				case "get":
					if (commandField.length < GET_ARGUMENTS) {
						return null;
					}
					
					return new GetRequest(commandField[1]);
					
				case "set":
					if (commandField.length < SET_ARGUMENTS) {
						return null;
					}

					Value<?> value = null;
					
					if (isInteger(commandField[2])) {
						value = new ValueInteger(Integer.valueOf(commandField[2]));
					} else {
						value = new ValueString(commandField[2]);
					}
					
					return new SetRequest(commandField[1], value);
				
				case "del":
					if (commandField.length < DEL_ARGUMENTS) {
						return null;
					}
					
					return new DelRequest(commandField[1]);
					
				case "incr":
				case "decr":
					if (commandField.length < INCR_DECR_ARGUMENTS) {
						return null;
					}
					
					if (commandField[0].equals("incr")) {
						return new IncrRequest(commandField[1]);
					} else {
						return new DecrRequest(commandField[1]);
					}
				case "quit":
				case "":
				default:
			}
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
