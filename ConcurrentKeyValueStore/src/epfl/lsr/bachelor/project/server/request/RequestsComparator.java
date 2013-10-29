package epfl.lsr.bachelor.project.server.request;

import java.util.Comparator;

/**
 * Comparator for requests, compare by ID (a smaller ID means that the request has a bigger priority)
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class RequestsComparator implements Comparator<Request> {

    @Override
    public int compare(Request request0, Request request1) {
    	final long difference = request0.getID() - request1.getID();
    	
    	// Avoid overflows of integers, this returns either -1, 0 or 1
        return (int) (difference / Math.abs(difference));
    }

}
