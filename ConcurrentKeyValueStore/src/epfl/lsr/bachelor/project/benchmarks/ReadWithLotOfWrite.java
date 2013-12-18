package epfl.lsr.bachelor.project.benchmarks;

import java.net.InetSocketAddress;

import epfl.lsr.bachelor.project.client.PipelinedClient;
import epfl.lsr.bachelor.project.util.Constants;

/**
 * Read with lot of write
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class ReadWithLotOfWrite {
	public static void main(String[] args) throws InterruptedException {
		final int numberClient = 50;
		final int numberRequestSend = 10000;
		final int one = 1;
		
		String[] firstGetRequest = {"get a"};
		int[] firstGetNumber = {numberRequestSend};
		GeneralBenchmarkPipelined firstGetGeneralBenchmarkPipelined = new GeneralBenchmarkPipelined(
				firstGetRequest, firstGetNumber, numberClient);
		
		String[] secondGetRequest = {"get b"};
		int[] secondGetNumber = {numberRequestSend};
		GeneralBenchmarkPipelined secondGetGeneralBenchmarkPipelined = new GeneralBenchmarkPipelined(
				secondGetRequest, secondGetNumber, numberClient);
		
		firstGetGeneralBenchmarkPipelined.start();
		secondGetGeneralBenchmarkPipelined.start();

		final int time = 50;
		Thread.sleep(time);

		PipelinedClient client = new PipelinedClient(new InetSocketAddress(
				"127.0.0.1", Constants.PORT));
		client.connect();
		for (int i = 0; i < numberRequestSend; i++) {
			client.set(i % 2 == 0 ? "a" : "b", String.valueOf(one));
		}
		
		for (int i = 0; i < numberRequestSend; i++) {
			client.getNextAnswerFromServer();
		}
		client.disconnect();
	}
}
