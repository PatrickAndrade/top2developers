package epfl.lsr.bachelor.project.util;

import epfl.lsr.bachelor.project.server.request.DecrRequest;
import epfl.lsr.bachelor.project.server.request.DelRequest;
import epfl.lsr.bachelor.project.server.request.ErrRequest;
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

	public Request parse(String command) {
		String[] commandField = command.split(" ");

		if (commandField.length < 1) {
			return new ErrRequest("");
		} else {
			switch (commandField[0]) {
				case Constants.GET_COMMAND:
					if (commandField.length < Constants.GET_ARGUMENTS) {
						return new ErrRequest("-Err get request one argument");
					}
					
					return new GetRequest(commandField[1]);
					
				case Constants.SET_COMMAND:
					if (commandField.length < Constants.SET_ARGUMENTS) {
						return new ErrRequest("-Err set request two arguments");
					}

					Value<?> value = null;
					
					if (isInteger(commandField[2])) {
						value = new ValueInteger(Integer.valueOf(commandField[2]));
					} else {
						value = new ValueString(commandField[2]);
					}
					
					return new SetRequest(commandField[1], value);
				
				case Constants.DEL_COMMAND:
					if (commandField.length < Constants.DEL_ARGUMENTS) {
						return new ErrRequest("-Err del request one argument");
					}
					
					return new DelRequest(commandField[1]);
					
				case Constants.INCR_COMMAND:
				case Constants.DECR_COMMAND:
					if (commandField.length < Constants.INCR_DECR_ARGUMENTS) {
						return new ErrRequest("-Err " + Constants.INCR_COMMAND + "/" +
								Constants.DECR_COMMAND + " request one argument");
					}
					
					if (commandField[0].equals(Constants.INCR_COMMAND)) {
						return new IncrRequest(commandField[1], 1);
					} else {
						return new DecrRequest(commandField[1], 1);
					}
				case Constants.HINCR_COMMAND:
				case Constants.HDECR_COMMAND:
					if (commandField.length < Constants.HINCR_HDECR_ARGUMENTS) {
						return new ErrRequest("-Err " + Constants.HINCR_COMMAND + "/" +
								Constants.HDECR_COMMAND + " request one argument");
					}
					
					if (!isInteger(commandField[2])) {
						return new ErrRequest("-Err " + Constants.HINCR_COMMAND + "/" +
								Constants.HDECR_COMMAND + " need an integer as argument");
					}
					
					if (commandField[0].equals(Constants.HINCR_COMMAND)) {
						return new IncrRequest(commandField[1], Integer.valueOf(commandField[2]));
					} else {
						return new DecrRequest(commandField[1], Integer.valueOf(commandField[2]));
					}
				case Constants.EMPTY_STRING:
					return new ErrRequest("");
				default:
			}

			return new ErrRequest("-Err unable to execute command '" + commandField[0] + "'");
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
