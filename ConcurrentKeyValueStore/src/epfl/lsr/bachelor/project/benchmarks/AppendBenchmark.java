package epfl.lsr.bachelor.project.benchmarks;

/**
 * A benchmark that append a value to a key
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class AppendBenchmark {
	public static void main(String[] args) {
		int numberClient = 50;
		String[] request = { "append a a" };
		int[] numberOfSend = { 10000 };
		GeneralBenchmarkPipelined generalBenchmarkPipelined = new GeneralBenchmarkPipelined(
				request, numberOfSend, numberClient);
		generalBenchmarkPipelined.start();
	}
}
