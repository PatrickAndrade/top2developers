package epfl.lsr.bachelor.project.util;

import epfl.lsr.bachelor.project.server.request.Request;
import epfl.lsr.bachelor.project.server.request.readableRequests.GetRequest;
import epfl.lsr.bachelor.project.server.request.voidRequests.ErrRequest;
import epfl.lsr.bachelor.project.server.request.voidRequests.PingRequest;
import epfl.lsr.bachelor.project.server.request.writableRequests.AppendRequest;
import epfl.lsr.bachelor.project.server.request.writableRequests.DecrRequest;
import epfl.lsr.bachelor.project.server.request.writableRequests.DelRequest;
import epfl.lsr.bachelor.project.server.request.writableRequests.IncrRequest;
import epfl.lsr.bachelor.project.server.request.writableRequests.SetRequest;
import epfl.lsr.bachelor.project.values.Value;
import epfl.lsr.bachelor.project.values.ValueInteger;
import epfl.lsr.bachelor.project.values.ValueString;

/**
 * This is a command parser to handle requests from clients
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class CommandParser {

	public Request parse(String command) {
		
		if (command == null) {
			return new ErrRequest("");
		} else {
			
			// We first split the line-command
			String[] commandField = command.split(" ");
			
			if ((commandField.length > 1) && (commandField[1].equals(Constants.EMPTY_STRING))) {
				return new ErrRequest("-Err '" + Constants.EMPTY_STRING + "' can't be a key");
			}
			
			// We handle the command
			switch (commandField[0]) {
			
				// Handle get-command
				case Constants.GET_COMMAND:
					if (commandField.length < Constants.GET_ARGUMENTS) {
						return new ErrRequest("-Err " + Constants.GET_COMMAND + " request one argument");
					}
					return new GetRequest(commandField[1]);
					
				// Handle set-command
				case Constants.SET_COMMAND:
					if (commandField.length < Constants.SET_ARGUMENTS) {
						return new ErrRequest("-Err " + Constants.SET_COMMAND + " request two arguments");
					}
					Value<?> setValue = null;
					if (Utilities.isInteger(commandField[2])) {
						setValue = new ValueInteger(Integer.valueOf(commandField[2]));
					} else {
						setValue = new ValueString(commandField[2]);
					}
					return new SetRequest(commandField[1], setValue);
				
				// Handle del-command
				case Constants.DEL_COMMAND:
					if (commandField.length < Constants.DEL_ARGUMENTS) {
						return new ErrRequest("-Err del request one argument");
					}
					return new DelRequest(commandField[1]);
					
				// Handle incr/decr-command
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
				
				// Handle incrby/decrby-command
				case Constants.INCRBY_COMMAND:
				case Constants.DECRBY_COMMAND:
					if (commandField.length < Constants.HINCR_HDECR_ARGUMENTS) {
						return new ErrRequest("-Err " + Constants.INCRBY_COMMAND + "/" +
								Constants.DECRBY_COMMAND + " request one argument");
					}
					// If the increment/decrement is not an integer we simply tell the client that
					// we cannot perform the request
					if (!Utilities.isInteger(commandField[2])) {
						return new ErrRequest("-Err " + Constants.INCRBY_COMMAND + "/" +
								Constants.DECRBY_COMMAND + " need an integer as argument");
					}
					if (commandField[0].equals(Constants.INCRBY_COMMAND)) {
						return new IncrRequest(commandField[1], Integer.valueOf(commandField[2]));
					} else {
						return new DecrRequest(commandField[1], Integer.valueOf(commandField[2]));
					}
				
				case Constants.APPEND_COMMAND:
					if (commandField.length < Constants.APPEND_ARGUMENTS) {
						return new ErrRequest("-Err " + Constants.APPEND_COMMAND + " request two arguments");
					}
					Value<?> appendValue = null;
					if (Utilities.isInteger(commandField[2])) {
						appendValue = new ValueInteger(Integer.valueOf(commandField[2]));
					} else {
						appendValue = new ValueString(commandField[2]);
					}
					return new AppendRequest(commandField[1], appendValue);
					
				case Constants.PING_COMMAND:
					return new PingRequest();
					
				// Handle the "empty"-command
				case Constants.EMPTY_STRING:
					return new ErrRequest("");
					
				// Handle the unknown-commands
				default:
			}
	
			return new ErrRequest("-Err unable to execute command '" + commandField[0] + "'");
		}
	}
}
