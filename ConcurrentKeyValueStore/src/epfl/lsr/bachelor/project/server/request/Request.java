package epfl.lsr.bachelor.project.server.request;

import java.io.IOException;

import epfl.lsr.bachelor.project.connection.Connection;
import epfl.lsr.bachelor.project.store.KeyValueStore;
import epfl.lsr.bachelor.project.util.Constants;
import epfl.lsr.bachelor.project.values.Value;

/**
 * Represent a request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
abstract public class Request {
    private String mKey;
    private String mMessageToReturn = "";
    private Value<?> mValue;
    private Connection mConnection;
    private long mID = 0; // Default value, should be changed calling setID()

    @SuppressWarnings("unchecked")
    // The static reference to the KeyValueStore
    protected static final KeyValueStore<String, Value<?>> KEY_VALUE_STORE =
        (KeyValueStore<String, Value<?>>) KeyValueStore.getInstance();

    /**
     * Default constructor
     * 
     * @param key
     *            the key to reference the value stored
     */
    public Request(String key) {
        mKey = key;
    }

    /**
     * Constructor to simplify the construction of a {@link SetRequest}
     * 
     * @param key
     *            the key to reference the value stored
     * @param value
     *            the value to be stored
     */
    public Request(String key, Value<?> value) {
        mKey = key;
        mValue = value;
    }

    /**
     * Call to perform the request. When we finish to peform the request, we
     * must notify() the thread that wait for this monitor!
     * 
     * @throws CloneNotSupportedException
     */
    abstract public void perform() throws CloneNotSupportedException;

    /**
     * Enables to give an answer back after performing a request
     */
    public void respond() throws IOException {
    	System.out.println("gonna write : " + mMessageToReturn);//TODO
        mConnection.getDataOutputStream().writeBytes(mMessageToReturn + "\n");
        mConnection.getDataOutputStream().flush();
    }

    /**
     * Enables to know if the request can be performed
     * 
     * @return true or false depending on if it can be performed
     */
    public boolean canBePerformed() {
        return true;
    }

    /**
     * Enables to return the key associated with this request
     * 
     * @return the key associated with this request
     */
    public String getKey() {
        return mKey;
    }

    /**
     * Enables to return the value associated with this request
     * 
     * @return the value associated with this request
     */
    public Value<?> getValue() {
        return mValue;
    }

    /**
     * Enables to retrieve the ID of the request
     * 
     * @return the ID
     */
    public long getID() {
        return mID;
    }

    /**
     * Enables to set the ID of this request
     * 
     * @param mNextRequestID
     *            the ID of the request
     */
    public void setID(long mNextRequestID) {
        mID = mNextRequestID;
    }

    /**
     * Enables to set the connection associated with this request
     * 
     * @param connection
     *            the connection associated with this request
     */
    public void setConnection(Connection connection) {
        mConnection = connection;
    }

    /**
     * Enables to set the message to return after performing this request
     * 
     * @param messageToReturn
     *            the message to be returned
     */
    public void setMessageToReturn(String messageToReturn) {
        mMessageToReturn = messageToReturn;
    }

    /**
     * Enables to notify the thread in waiting-mode that the request has been
     * performed. This must be called after performing the request
     */
    public void notifyRequestPerformed(Request request) {
        mConnection.notifyThatRequestIsPerformed(request);
    }

    /**
     * Enables to know if the the message to be returned after performing the
     * request is empty
     * 
     * @return is the message empty or not
     */
    public boolean isMessageEmpty() {
        return mMessageToReturn.equals(Constants.EMPTY_STRING);
    }

}
