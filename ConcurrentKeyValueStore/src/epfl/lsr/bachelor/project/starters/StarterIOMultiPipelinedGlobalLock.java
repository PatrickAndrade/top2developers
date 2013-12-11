package epfl.lsr.bachelor.project.starters;

import java.io.IOException;

import epfl.lsr.bachelor.project.store.KeyValueStoreWithGlobalLock;

/**
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class StarterIOMultiPipelinedGlobalLock {
    public static void main(String[] args) throws IOException {
        StartersConfiguration.start(false, true, true, new KeyValueStoreWithGlobalLock());
    }
}

