package epfl.lsr.bachelor.project.util;

/**
 * Wrap in one class all the constants needed
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class Constants {
	public static final int PORT = 22122;
	public static final int NUMBER_OF_PIPELINED_CONNECTIONS = 10;
	
	public static final String EMPTY_STRING = "";
	public static final String WELCOME = "Server 1.0.1 (developed in JAVA)" + "\n\n" +
			"Running in standard mode" + "\n" + "Port: " + PORT + "\n";
	public static final String SERVER_CLOSED = "The server has been closed !";
	public static final String PROGRAMM_NAME = "ConcurrentKeyValueStore:" + Constants.PORT + " > ";
	
	public static final int GET_ARGUMENTS = 2;
	public static final int SET_ARGUMENTS = 3;
	public static final int DEL_ARGUMENTS = 2;
	public static final int INCR_DECR_ARGUMENTS = 2;
	public static final int HINCR_HDECR_ARGUMENTS = 3;
	public static final int APPEND_ARGUMENTS = 3;
	
	public static final String GET_COMMAND = "get";
	public static final String SET_COMMAND = "set";
	public static final String DEL_COMMAND = "del";
	public static final String INCR_COMMAND = "incr";
	public static final String DECR_COMMAND = "decr";
	public static final String HINCR_COMMAND = "hincr";
	public static final String HDECR_COMMAND = "hdecr";
	public static final String APPEND_COMMAND = "append";
	public static final String PING_COMMAND = "ping";
	public static final String QUIT_COMMAND = "quit";
	
	public static final String STORED = "STORED";
	public static final String REPLACED = "REPLACED";
	public static final String DELETED = "DELETED";
	public static final String INTEGER = "(integer)";
	public static final String NO_SUCH_VALUE = "-Err no such value";
	public static final String NOT_SUPPORTED = "-Err not supported for this value";
	
}

