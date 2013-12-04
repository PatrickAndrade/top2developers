package epfl.lsr.bachelor.project.benchmarks;
/**
 * A benchmark that send ping command
 * 
 * @author Gregory Maitre & Patrick Andrade
 * 
 */
public class PingBenchmark {
	public static void main(String[] args) {
		int numberClient = 50;
		String[] request = {"ping"};
		int[] numberOfSend = {10000};
		GeneralBenchmarkPipelined generalBenchmarkPipelined = new GeneralBenchmarkPipelined(request, numberOfSend, numberClient);
		generalBenchmarkPipelined.start();
	}
}
