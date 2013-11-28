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
	
	public DataRead(String request, Channel channel) {
		mRequest = request;
		mChannel = channel;
	}

	public String getRequest() {
		return mRequest;
	}

	public Channel getChannel() {
		return mChannel;
	}
}
