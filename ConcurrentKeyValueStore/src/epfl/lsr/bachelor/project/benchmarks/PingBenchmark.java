package epfl.lsr.bachelor.project.benchmarks;

/**
 * A benchmark that send ping command
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class PingBenchmark {
	public static void main(String[] args) {
		final int numberClient = 50;
		final int numberRequestSend = 10000;
		String[] request = {"ping"};
		GeneralBenchmarkPipelined generalBenchmarkPipelined = new GeneralBenchmarkPipelined(
				request, numberRequestSend, numberClient);
		generalBenchmarkPipelined.start();
	}
}
