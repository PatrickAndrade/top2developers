package epfl.lsr.bachelor.project.starters;

import java.io.IOException;

import epfl.lsr.bachelor.project.store.KeyValueStoreWithMapLocks;

/**
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class StarterNIOMultiMapsLock {
    public static void main(String[] args) throws IOException {
        StartersConfiguration.start(true, false, true, new KeyValueStoreWithMapLocks());
    }
}

