package epfl.lsr.bachelor.project.benchmarks;

import java.net.InetSocketAddress;

import epfl.lsr.bachelor.project.client.PipelinedClient;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * This is the first benchmark of you KeyValueStore
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class KeyValueStoreBenchmark {
    private static final int SIZE = 1;

    public static void main(String[] args) {

        Thread[] myThreads = new Thread[SIZE];
        for (int i = 0; i < myThreads.length; i++) {
            myThreads[i] = new Thread(new TestingCode());
        }

        for (Thread thread : myThreads) {
            thread.start();
        }

    }

}

class TestingCode implements Runnable {

    private static final long TEN_POWER_NINE = (long) Math.pow(10, 9);
    private static final long TEN_POWER_SIX = (long) Math.pow(10, 6);
    // private static final long TEN_POWER_THREE = (long) Math.pow(10, 3);
    private static final long ITERATION = (long) Math.pow(10, 3);

    @Override
    public void run() {
        PipelinedClient client = new PipelinedClient(new InetSocketAddress("192.168.1.42", Constants.PORT));
        client.connect();

        // Set a first value
        client.set("a", "1000000");
        System.out.println(client.getNextAnswerFromServer());

        long totalTime = 0;
        long initTime = System.nanoTime();

        // Send pipelined requests
        for (long i = 0; i < ITERATION; i++) {
            client.get("a");
        }

        long finishedTime = System.nanoTime();
        totalTime = finishedTime - initTime;
        
        System.out.println(" -> Elapsed total time: " + totalTime / TEN_POWER_NINE + " seconds");
        System.out.println(" -> Average total time: " + totalTime / TEN_POWER_SIX / ITERATION + " milliseconds");
        
        // Read answers
        for (long i = 0; i < ITERATION; i++) {
            System.out.println(client.getNextAnswerFromServer());
        }

        client.disconnect();

    }

}
