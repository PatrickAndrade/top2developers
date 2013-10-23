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
 * The client that send the request to the server in pipelined waay
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

           // new Thread(RequestReader.getInstance(mServerAnswers)).start();

        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public boolean isConnected() {
        return (mSocket != null) && mSocket.isConnected();
    }

    public void disconnect() {
        try {
            mThreadPool.shutdownNow();
            mBufferedReader.close();
            mDataOutputStream.close();
            mSocket.close();
        } catch (IOException e) {
        }
    }

    /*
     * public String fakeCommand(String command) { return
     * sendAndGetAnswer(command); }
     */
    
    
    public void get(String key) {
        if (key != null) {
            String command = Constants.GET_COMMAND + " " + key;
            try {
                mDataOutputStream.writeBytes(command + "\n");
                mDataOutputStream.flush();

                mServerAnswers.add(mThreadPool.submit(new RequestSender(mBufferedReader)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("key was null at get-method !");
        }
    }

    public void set(String key, String value) {
        if (key != null && value != null) {
            String command = Constants.SET_COMMAND + " " + key + " " + value;
            try {
                mDataOutputStream.writeBytes(command + "\n");
                mDataOutputStream.flush();

                mServerAnswers.add(mThreadPool.submit(new RequestSender(mBufferedReader)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("key or value was null at set-method !");
        }
    }

    public String getNextAnswerFromServer() {        
        try {
            return mServerAnswers.remove(0).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return null;
    }
        
    /*
     * public String delete(String key) { return (key == null) ? null :
     * sendAndGetAnswer(Constants.DEL_COMMAND + " " + key); }
     * 
     * public String increment(String key) { return (key == null) ? null :
     * sendAndGetAnswer(Constants.INCR_COMMAND + " " + key); }
     * 
     * public String increment(String key, int value) { return (key == null) ?
     * null : sendAndGetAnswer(Constants.HINCR_COMMAND + " " + key + " " +
     * value); }
     * 
     * public String decrement(String key) { return (key == null) ? null :
     * sendAndGetAnswer(Constants.DECR_COMMAND + " " + key); }
     * 
     * public String decrement(String key, int value) { return (key == null) ?
     * null : sendAndGetAnswer(Constants.HDECR_COMMAND + " " + key + " " +
     * value); }
     * 
     * public String quit() { return sendAndGetAnswer("quit"); }
     * 
     * private String sendAndGetAnswer(String toBeWritten) { try {
     * mDataOutputStream.writeBytes(toBeWritten + "\n");
     * mDataOutputStream.flush(); return mBufferedReader.readLine(); } catch
     * (IOException e) { return null; } }
     */
}

final class RequestSender implements Callable<String> {

    private final BufferedReader mBufferedReader;

    public RequestSender(BufferedReader bufferedReader) {
        mBufferedReader = bufferedReader;
    }

    @Override
    public String call() throws Exception {
        try {
            return mBufferedReader.readLine();
        } catch (IOException e) {
            return null;
        }
    }

}
/*
final class RequestReader implements Runnable {
    private ArrayList<Future<String>> mServerAnswers;
    private static RequestReader mInstanceReader = null;

    private RequestReader(ArrayList<Future<String>> serverAnswers) {
        mServerAnswers = serverAnswers;
    }

    public static RequestReader getInstance(ArrayList<Future<String>> serverAnswers) {
        if (mInstanceReader == null) {
            mInstanceReader = new RequestReader(serverAnswers);
        }
        return mInstanceReader;
    }

    @Override
    public void run() {

    }
}*/