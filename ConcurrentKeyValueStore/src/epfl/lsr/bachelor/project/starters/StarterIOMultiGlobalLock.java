package epfl.lsr.bachelor.project.starters;

import java.io.IOException;

import epfl.lsr.bachelor.project.store.KeyValueStoreWithGlobalLock;

/**
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class StarterIOMultiGlobalLock {
    public static void main(String[] args) throws IOException {
        StartersConfiguration.start(false, false, true, new KeyValueStoreWithGlobalLock());
    }
}

