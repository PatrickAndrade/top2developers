package epfl.lsr.bachelor.project.benchmarks;

/**
 * This benchmark send get request to the server with two different key to test
 * if the key value store block all the key when we write and read at the same
 * time
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class ReadWriteBenchmark {
	public static void main(String[] args) throws InterruptedException {
		final int numberClient = 50;
		final int numberRequestSend = 10000;
		final int one = 1;
		String[] firstSet = {"set a 2"};
		int[] firstSetNumber = {one};
		GeneralBenchmarkPipelined firstSetGeneralBenchmarkPipelined = new GeneralBenchmarkPipelined(
				firstSet, firstSetNumber, one);
		
		String[] firstGetRequest = {"get a"};
		int[] firstGetNumber = {numberRequestSend};
		GeneralBenchmarkPipelined firstGetGeneralBenchmarkPipelined = new GeneralBenchmarkPipelined(
				firstGetRequest, firstGetNumber, numberClient);
		
		String[] secondGetRequest = {"get b"};
		int[] secondGetNumber = {numberRequestSend};
		GeneralBenchmarkPipelined secondGetGeneralBenchmarkPipelined = new GeneralBenchmarkPipelined(
				secondGetRequest, secondGetNumber, numberClient);
		
		String[] lastSet = {"set a 2"};
		int[] lastSetNumber = {numberRequestSend};
		GeneralBenchmarkPipelined lastSetGeneralBenchmarkPipelined = new GeneralBenchmarkPipelined(
				lastSet, lastSetNumber, one);
		
		firstSetGeneralBenchmarkPipelined.start();
		firstGetGeneralBenchmarkPipelined.start();
		secondGetGeneralBenchmarkPipelined.start();

		final int time = 50;
		Thread.sleep(time);
		lastSetGeneralBenchmarkPipelined.start();
	}
}
