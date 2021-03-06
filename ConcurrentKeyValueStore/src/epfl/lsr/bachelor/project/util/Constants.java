package epfl.lsr.bachelor.project.util;

/**
 * Wrap in one class all the constants needed
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class Constants {
	public static final int PORT = 22122;
	public static final int NUMBER_OF_PIPELINED_CONNECTIONS = 100;
	public static final int NUMBER_OF_PIPELINED_REQUESTS = 100;
	public static final int READ_BUFFER_NIO = 9216;
	public static final int NUMBER_OF_PIPELINED_WORKER = 100;
	public static final int CONCURRENT_ARRAY_SIZE = 3 * NUMBER_OF_PIPELINED_WORKER;
	public static final char NIO_TELNET_LAST_CHAR = '\r';
	
	public static final String EMPTY_STRING = "";
	public static final String WELCOME_STANDARD = "Standard Server (developed in JAVA)" + "\n\n" +
			"Running in standard mode" + "\n" + "Port: " + PORT + "\n";
	public static final String WELCOME_NIO = "NIO Server (developed in JAVA)" + "\n\n" +
			"Running in NIO mode" + "\n" + "Port: " + PORT + "\n";
	public static final String SERVER_CLOSED = "The server has been closed !";
	
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
	public static final String INCRBY_COMMAND = "incrby";
	public static final String DECRBY_COMMAND = "decrby";
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