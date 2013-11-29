package epfl.lsr.bachelor.project.benchmarks;

/**
 * A benchmark that send one set for each client and get it several times
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class SetAValueAndGetItBenchmark {
	public static void main(String[] args) {
		int numberClient = 50;
		String[] request = { "set a 2", "get a" };
		int[] numberOfSend = { 1, 10000 };
		GeneralBenchmarkPipelined generalBenchmarkPipelined = new GeneralBenchmarkPipelined(
				request, numberOfSend, numberClient);
		generalBenchmarkPipelined.start();
	}
}
