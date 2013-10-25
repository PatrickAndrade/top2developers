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
        return (int) (request0.getID() - request1.getID());
    }

}
