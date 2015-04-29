package epfl.lsr.bachelor.project.benchmarks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BenchmarkNTimesM {
	
	static String[] request = {"set", "get"};
	final static int numberOfExperiments = 50;
	final static int numberRequestSend = 100;
	final static int numberOfClients = 100;
	
	public static void main(String[] args) throws IOException {
		System.out.println("Test begin");
		File file = new File("resultsIOMultiPipelinedKeysLock_NtimesM.txt");
		
		if (file.exists()) {
			file.delete();
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		BarreDeProgression barreDeProgression = new BarreDeProgression(numberOfExperiments);
		double average = 0;
		
		for (int experiments = 1; experiments <= numberOfExperiments; experiments++) {
			GeneralBenchmarkPipelined generalBenchmarkPipelined = new GeneralBenchmarkPipelined(
					request, numberRequestSend, numberOfClients);
			average = generalBenchmarkPipelined.start();
			writer.write(Double.toString(average) + "\n");
			barreDeProgression.increment();
		}
		
		System.out.println("Test end");
		writer.close();
	}
}
