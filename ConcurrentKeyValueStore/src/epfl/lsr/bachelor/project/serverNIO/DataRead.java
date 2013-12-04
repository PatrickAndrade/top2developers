package epfl.lsr.bachelor.project.serverNIO;

import java.nio.channels.Channel;

/**
 * This class encapsulate the data read for one request
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class DataRead {
	private String mRequest;
	private Channel mChannel;
	
	/**
	 * Default Constructor
	 * 
	 * @param request the request to be parse
	 * @param channel the channel from where the request is coming 
	 */
	public DataRead(String request, Channel channel) {
		mRequest = request;
		mChannel = channel;
	}

	/**
	 * Get method that return the request
	 * 
	 * @return the request
	 */
	public String getRequest() {
		return mRequest;
	}

	/**
	 * Get method that return the channel
	 * 
	 * @return the channel
	 */
	public Channel getChannel() {
		return mChannel;
	}
}
