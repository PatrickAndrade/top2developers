package epfl.lsr.bachelor.project.benchmarks;

/**
 * A benchmark that send one set for each client and get it several times
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class SetGetItBenchmark {
	public static void main(String[] args) {
		final int numberClient = 50;
		final int numberRequestSend = 10000;
		final int one = 1;
		String[] request = {"set", "get"};
		int[] numberOfSend = {one, numberRequestSend};
		GeneralBenchmarkPipelined generalBenchmarkPipelined = new GeneralBenchmarkPipelined(
				request, numberOfSend, numberClient);
		generalBenchmarkPipelined.start();
	}
}
