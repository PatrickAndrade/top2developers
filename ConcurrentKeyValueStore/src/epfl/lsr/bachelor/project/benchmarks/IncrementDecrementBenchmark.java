package epfl.lsr.bachelor.project.benchmarks;
/**
 * A benchmark that increment and decrement a value
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class IncrementDecrementBenchmark {
	public static void main(String[] args) {
		int numberClient = 50;
		String[] request = { "incr a", "decr a" };
		int[] numberOfSend = { 10000, 10000 };
		GeneralBenchmarkPipelined generalBenchmarkPipelined = new GeneralBenchmarkPipelined(
				request, numberOfSend, numberClient);
		generalBenchmarkPipelined.start();
	}
}
