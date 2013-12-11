package epfl.lsr.bachelor.project.starters;

import java.io.IOException;

import epfl.lsr.bachelor.project.store.KeyValueStoreForSingleThreadedArchitecture;

/**
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class StarterIOSinglePipelined {
    public static void main(String[] args) throws IOException {
        StartersConfiguration.start(false, true, false, new KeyValueStoreForSingleThreadedArchitecture());
    }
}

