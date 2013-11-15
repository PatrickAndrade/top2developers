package epfl.lsr.bachelor.project.connection;

import epfl.lsr.bachelor.project.server.request.Request;

/**
 * The interface that a connection or a connection worker must implement
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public interface ConnectionInterface {
	/**
     * Enables to notify a previous waitUntilRequestIsPerformed()-call
     */
    void notifyThatRequestIsPerformed(Request request);
}
