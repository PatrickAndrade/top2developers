package epfl.lsr.bachelor.project.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import epfl.lsr.bachelor.project.util.Constants;

/**
 * A client that send requests to the server a in pipelined way without waiting
 * for the requests to be performed (non-blocking requests)
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class PipelinedClient {

    private Socket mSocket;
    private InetSocketAddress mIpPort;
    private BufferedReader mBufferedReader;
    private DataOutputStream mDataOutputStream;
    private ExecutorService mThreadPool;
    private ArrayList<Future<String>> mServerAnswers;

    public PipelinedClient(InetAddress inetaddress, int port) {
        mIpPort = new InetSocketAddress(inetaddress, port);
    }

    public PipelinedClient(InetSocketAddress inetSocketAddress) {
        mIpPort = inetSocketAddress;
    }

    /**
     * Enables to connect to the server. This MUST be called before executing
     * any request
     * 
     * @return if we are connected to the server
     */
    public boolean connect() {
        if (isConnected()) {
            return true;
        }

        try {
            mThreadPool = Executors.newFixedThreadPool(1);
            mServerAnswers = new ArrayList<Future<String>>();
            mSocket = new Socket(mIpPort.getAddress(), mIpPort.getPort());
            mBufferedReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            mDataOutputStream = new DataOutputStream(mSocket.getOutputStream());

        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Enables to know if we are connected to the server
     * 
     * @return if we are connected
     */
    public boolean isConnected() {
        return (mSocket != null) && mSocket.isConnected() && !mSocket.isClosed();
    }

    /**
     * Enables to disconnect the client, should always be called when we are
     * done with the server
     */
    public void disconnect() {
        try {
            mThreadPool.shutdownNow();
            mBufferedReader.close();
            mDataOutputStream.close();
            mSocket.close();
        } catch (IOException e) {
        }
    }

    /**
     * Enables to execute a custom command
     * 
     */
    public void customCommand(String command) {
        if (isConnected()) {
            if (command != null) {
                writeCommandAndSubmitToRequestReader(command);
            } else {
                throw new IllegalArgumentException("command was null at fakeCommand-method !");
            }
        }
    }

    /**
     * Enables to execute a get command over the specified key
     * 
     * @param key
     */
    public void get(String key) {
        if (isConnected()) {
            if (key != null) {
                writeCommandAndSubmitToRequestReader(Constants.GET_COMMAND + " " + key);
            } else {
                throw new IllegalArgumentException("key was null at get-method !");
            }
        }
    }

    /**
     * Enables to execute a set command over the specified key with the
     * specified value
     * 
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        if (isConnected()) {
            if (key != null && value != null) {
                writeCommandAndSubmitToRequestReader(Constants.SET_COMMAND + " " + key + " " + value);
            } else {
                throw new IllegalArgumentException("key or value was null at set-method !");
            }
        }
    }

    /**
     * Enables to execute a delete command over the specified key
     * 
     * @param key
     */
    public void delete(String key) {
        if (isConnected()) {
            if (key != null) {
                writeCommandAndSubmitToRequestReader(Constants.DEL_COMMAND + " " + key);
            } else {
                throw new IllegalArgumentException("key or value was null at delete-method !");
            }
        }
    }

    /**
     * Enables to execute an increment command over the specified key
     * 
     * @param key
     */
    public void increment(String key) {
        if (isConnected()) {
            if (key != null) {
                writeCommandAndSubmitToRequestReader(Constants.INCR_COMMAND + " " + key);
            } else {
                throw new IllegalArgumentException("key was null at increment-method !");
            }
        }
    }

    /**
     * Enables to execute an hincrement command over the specified key with the
     * specified increment
     * 
     * @param key
     * @param value
     */
    public void increment(String key, int value) {
        if (isConnected()) {
            if (key != null) {
                writeCommandAndSubmitToRequestReader(Constants.INCRBY_COMMAND + " " + key + " " + value);
            } else {
                throw new IllegalArgumentException("key was null at increment-method !");
            }
        }
    }

    /**
     * Enables to execute a decrement command over the specified key
     * 
     * @param key
     */
    public void decrement(String key) {
        if (isConnected()) {
            if (key != null) {
                writeCommandAndSubmitToRequestReader(Constants.DECR_COMMAND + " " + key);
            } else {
                throw new IllegalArgumentException("key was null at decrement-method !");
            }
        }
    }

    /**
     * Enables to execute a decrement command over the specified key with the
     * specified decrement
     * 
     * @param key
     * @param value
     */
    public void decrement(String key, int value) {
        if (isConnected()) {
            if (key != null) {
                writeCommandAndSubmitToRequestReader(Constants.DECRBY_COMMAND + " " + key + " " + value);
            } else {
                throw new IllegalArgumentException("key was null at decrement-method !");
            }
        }
    }
    
    /**
     * Enables to execute a ping command to the server
     */
    public void ping() {
    	if (isConnected()) {
    		writeCommandAndSubmitToRequestReader(Constants.PING_COMMAND);
        }
    }
    
    /**
     * Enables to execute an append command over the specified key with the
     * specified value to append
     * 
     * @param key
     * @param value
     */
    public void append(String key, String value) {
    	if (isConnected()) {
            if (key != null && value != null) {
                writeCommandAndSubmitToRequestReader(Constants.APPEND_COMMAND + " " + key + " " + value);
            } else {
                throw new IllegalArgumentException("key or value was null at append-method !");
            }
        }
    }

    /**
     * Enables to execute a quit command should be executed before a call to the
     * method disconnect
     * 
     */
    public void quit() {
        if (isConnected()) {
            writeCommandAndSubmitToRequestReader(Constants.QUIT_COMMAND);
        }
    }

    /**
     * Enables to get the answers from the server
     * 
     * @return the next answer not yet picked up by the client,
     *         <code>null</code> if there is no answer to be picked
     */
    public String getNextAnswerFromServer() {
        if (isConnected() && mServerAnswers.size() > 0) {
            try {
                return mServerAnswers.remove(0).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }
        return null;

    }

    private void writeCommandAndSubmitToRequestReader(String command) {
        try {
            mDataOutputStream.writeBytes(command + "\n");
            mDataOutputStream.flush();

            mServerAnswers.add(mThreadPool.submit(new RequestReader()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    private final class RequestReader implements Callable<String> {

        @Override
        public String call() throws Exception {
            try {
                return mBufferedReader.readLine();
            } catch (IOException e) {
                return null;
            }
        }
    }
}