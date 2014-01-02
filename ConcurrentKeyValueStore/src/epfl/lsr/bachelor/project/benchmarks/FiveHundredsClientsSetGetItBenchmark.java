package epfl.lsr.bachelor.project.benchmarks;

/**
 * A benchmark that send one set for each client and get it several times
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class FiveHundredsClientsSetGetItBenchmark {
	public static void main(String[] args) {
		final int numberClient = 500;
		final int numberRequestSend = 10000;
		String[] request = {"set", "get"};
		GeneralBenchmarkPipelined generalBenchmarkPipelined = new GeneralBenchmarkPipelined(
				request, numberRequestSend, numberClient);
		generalBenchmarkPipelined.start();
	}
}
