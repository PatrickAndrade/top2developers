package epfl.lsr.bachelor.project.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import epfl.lsr.bachelor.project.util.CommandParser;

/**
 * Encapsulates a connection opened. This is subclassed by two classes, namely
 * {@link BlockingConnection} and {@link NonBlockingConnection}
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public abstract class Connection implements Runnable {

    private Socket mSocket;
    private BufferedReader mBufferedReader;
    private DataOutputStream mDataOutputStream;
    private CommandParser mCommandParser;
    private RequestBuffer mRequestBuffer;

    public Connection(Socket socket, RequestBuffer requestBuffer) throws IOException {
        mSocket = socket;
        mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());
        mCommandParser = new CommandParser();
        mRequestBuffer = requestBuffer;

        System.out.println("  -> Started connection with " + mSocket.getInetAddress());
    }
    
    /**
     * Enables to notify a previous waitUntilRequestIsPerformed()-call
     */
    public abstract void notifyThatRequestIsPerformed();

    /**
     * Enables to close properly the connection (ie it closes the socket)
     * 
     * @param socket
     * @throws IOException
     */
    public void closeConnection() throws IOException {
        if (mSocket != null) {
            mSocket.close();
            System.err.println(" -> Connection with " + mSocket.getInetAddress() + " aborted !");
        }
    }

    /**
     * Enables to get the {@link DataOutputStream} of this connection
     * 
     * @return the {@link DataOutputStream} of this connection
     */
    public DataOutputStream getDataOutputStream() {
        return mDataOutputStream;
    }

    /**
     * Enables to get the {@link BufferedReader} associated to this object
     * 
     * @return the {@link BufferedReader} associated to this object
     */
    protected BufferedReader getBufferedReader() {
        return mBufferedReader;
    }

    /**
     * Enables to get the {@link BufferedReader}
     * 
     * @return the buffer of requests
     */
    protected RequestBuffer getRequestBuffer() {
        return mRequestBuffer;
    }
    
    /**
     * Enables to get the {@link CommandParser} associated to this object
     * 
     * @return the {@link CommandParser} associated to this object
     */
    protected CommandParser getCommandParser() {
        return mCommandParser;
    }
}
