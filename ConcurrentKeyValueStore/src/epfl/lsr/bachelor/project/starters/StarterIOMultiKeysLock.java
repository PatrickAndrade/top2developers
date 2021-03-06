package epfl.lsr.bachelor.project.starters;

import java.io.IOException;

import epfl.lsr.bachelor.project.store.KeyValueStoreWithKeyLocks;

/**
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class StarterIOMultiKeysLock {
    public static void main(String[] args) throws IOException {
        StartersConfiguration.start(false, false, true, new KeyValueStoreWithKeyLocks());
    }
}

