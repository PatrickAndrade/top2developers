package epfl.lsr.bachelor.project.starters;

import java.io.IOException;

import epfl.lsr.bachelor.project.pipe.MultiThreadPipe;
import epfl.lsr.bachelor.project.pipe.SingleThreadPipe;
import epfl.lsr.bachelor.project.pipe.WorkerPipeInterface;
import epfl.lsr.bachelor.project.server.RequestBuffer;
import epfl.lsr.bachelor.project.server.Server;
import epfl.lsr.bachelor.project.server.ServerInterface;
import epfl.lsr.bachelor.project.serverNIO.NIOServer;
import epfl.lsr.bachelor.project.store.KeyValueStore;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * Point of entry of the application KV-store
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public final class StartersConfiguration {

    private final static String HOST = "0.0.0.0";
    private static ServerInterface server = null;

    public static void start(final boolean isNIO, final boolean isPipelined, final boolean isMultiThreaded,
            final KeyValueStore keyValueStore) throws IOException {
        final RequestBuffer requestBuffer = new RequestBuffer();
        final WorkerPipeInterface worker = isMultiThreaded ? MultiThreadPipe.getInstance(requestBuffer)
                : SingleThreadPipe.getInstance(requestBuffer);
        KeyValueStore.setInstance(keyValueStore);

        server = isNIO ? new NIOServer(HOST, Constants.PORT, requestBuffer, worker) : new Server(Constants.PORT,
                requestBuffer, worker, isPipelined);
        server.start();
    }
}
