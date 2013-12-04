package epfl.lsr.bachelor.project.benchmarks;

/**
 * A benchmark that append a value to a key
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class AppendBenchmark {
	public static void main(String[] args) {
		final int numberClient = 50;
		final int numberRequestSend = 10000;
		String[] request = {"append a a"};
		int[] numberOfSend = {numberRequestSend};
		GeneralBenchmarkPipelined generalBenchmarkPipelined = new GeneralBenchmarkPipelined(
				request, numberOfSend, numberClient);
		generalBenchmarkPipelined.start();
	}
}
