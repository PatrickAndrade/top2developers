package epfl.lsr.bachelor.project.benchmarks;
/**
 * A benchmark that increment and decrement a value
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class IncrementDecrementBenchmark {
	public static void main(String[] args) {
		final int numberClient = 50;
		final int numberRequestSend = 10000;
		String[] request = {"incr a", "decr a"};
		GeneralBenchmarkPipelined generalBenchmarkPipelined = new GeneralBenchmarkPipelined(
				request, numberRequestSend, numberClient);
		generalBenchmarkPipelined.start();
	}
}
