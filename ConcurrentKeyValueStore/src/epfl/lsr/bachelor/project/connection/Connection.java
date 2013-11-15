package epfl.lsr.bachelor.project.connection;

import epfl.lsr.bachelor.project.server.request.Request;

/**
 * TODO: Comment this class
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public interface Connection {
	/**
     * Enables to notify a previous waitUntilRequestIsPerformed()-call
     */
    void notifyThatRequestIsPerformed(Request request);
}
