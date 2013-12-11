package epfl.lsr.bachelor.project.starters;

import java.io.IOException;

import epfl.lsr.bachelor.project.store.KeyValueStoreWithKeyLocks;

/**
 * << HERE YOU SHOULD COMMENT >>
 * 
 * @author Patrick Andrade
 * 
 */
public class StarterNIOMultiKeysLock {
    public static void main(String[] args) throws IOException {
        StartersConfiguration.start(true, false, true, new KeyValueStoreWithKeyLocks());
    }
}

