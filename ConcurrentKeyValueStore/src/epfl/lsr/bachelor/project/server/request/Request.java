package epfl.lsr.bachelor.project.server.request;

import java.io.IOException;
import java.nio.channels.SocketChannel;

import epfl.lsr.bachelor.project.connection.IOConnection;
import epfl.lsr.bachelor.project.serverNIO.NIOAnswerBuffer;
import epfl.lsr.bachelor.project.serverNIO.NIOWriterWorker;
import epfl.lsr.bachelor.project.store.ConcurrentArrayKeyValueStore;
import epfl.lsr.bachelor.project.store.HandMadeConcurrentKeyValueStore;
import epfl.lsr.bachelor.project.store.KeyValueStore;
import epfl.lsr.bachelor.project.store.ReaderWriterHelper;
import epfl.lsr.bachelor.project.util.Constants;
import epfl.lsr.bachelor.project.values.Value;

/**
 * Represent a request received from the client
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
abstract public class Request implements AtomicAction {
    private String mKey;
    private String mMessageToReturn = "";
    private Value<?> mValue;

    private IOConnection mConnection;

    private NIOAnswerBuffer mNIOAnswerBuffer;
    private SocketChannel mChannel;
    private NIOWriterWorker mWorker;

    private long mID = 0; // Default value, should be changed calling setID()

    // The static reference to the KeyValueStore
    protected static final KeyValueStore KEY_VALUE_STORE = HandMadeConcurrentKeyValueStore.getInstance();

    // The static reference to the ReaderWriterHelper (over keys)
    protected static final ReaderWriterHelper<String> READER_WRITER_HELPER_OVER_KEYS = HandMadeConcurrentKeyValueStore
            .getInstance().getReaderWriterHelper();

    // The static reference to the ReaderWriterHelper (over indexes)
    protected static final ReaderWriterHelper<Integer> READER_WRITER_HELPER_OVER_INDEXES = ConcurrentArrayKeyValueStore
            .getInstance().getReaderWriterHelper();

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
     * Call to perform the request. When we finish to perform the request, we
     * must notify() the thread that wait for this monitor!
     * 
     */
    public void perform() {
        KEY_VALUE_STORE.execute(this, mKey);
    }

    /**
     * Enables to give an answer back after performing a request
     */
    public void respond() throws IOException {
        if (mConnection != null) {
            mConnection.getDataOutputStream().writeBytes(mMessageToReturn + "\n");
            mConnection.getDataOutputStream().flush();
        } else {
            mNIOAnswerBuffer.add(mChannel, mMessageToReturn + "\n");
        }
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
     * Enables to retrieve the ID of the connection in NIO mode
     * 
     * @return the ID of the connection in NIO mode
     */
    public SocketChannel getChannel() {
        return mChannel;
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
    public void setConnection(IOConnection connection) {
        mConnection = connection;
        mChannel = null;
    }

    /**
     * Enables to set the channel associated with this request (in NIO context)
     * 
     * @param channel
     *            the channel associated with this request
     */
    public void setChannel(SocketChannel channel) {
        mChannel = channel;
        mConnection = null;
    }

    public void setNIOAnswerBuffer(NIOAnswerBuffer answerBuffer) {
        mNIOAnswerBuffer = answerBuffer;
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
     * Enables to set the NIO worker that send the request
     * 
     * @param worker
     *            the NIO worker that send the request
     */
    public void setWorker(NIOWriterWorker worker) {
        mWorker = worker;
    }

    /**
     * Enables to notify the thread in waiting-mode that the request has been
     * performed. This must be called after performing the request
     */
    public void notifyRequestPerformed(Request request) {
        if (mConnection != null) {
            mConnection.notifyThatRequestIsPerformed(request);
        } else {
            mWorker.notifyThatRequestIsPerformed(request);
        }
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
