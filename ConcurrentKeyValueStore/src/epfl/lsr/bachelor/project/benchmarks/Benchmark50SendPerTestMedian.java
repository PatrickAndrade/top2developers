package epfl.lsr.bachelor.project.benchmarks;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Benchmark50SendPerTestMedian {

	static String[] request = { "set", "get" };
	final static int maxNumberOfClients = 500;
	final static int numberRequestSend = 100;
	final static int step = 20;
	final static int numberOfTest = 20;

	public static void main(String[] args) throws IOException {
		System.out.println("Test begin");
		File folder = new File("Results");

		if (folder.exists() && !folder.isDirectory()) {
			folder.delete();
			folder.mkdir();
		}

		File file = new File(folder.getPath() + File.separatorChar
				+ "StarterNIOSingle.txt");

		if (file.exists()) {
			file.delete();
		}

		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		BarreDeProgression barreDeProgression = new BarreDeProgression(
				maxNumberOfClients / step);

		for (int numberOfClients = 1; numberOfClients <= maxNumberOfClients; numberOfClients += step) {
			ArrayList<Double> sampleTested = new ArrayList<Double>();
			
			for (int test = 0; test < numberOfTest; test++) {
				GeneralBenchmarkPipelined generalBenchmarkPipelined = new GeneralBenchmarkPipelined(
						request, numberRequestSend, numberOfClients);
				sampleTested.add(generalBenchmarkPipelined.start());
				
			}
			
			Collections.sort(sampleTested);
			writer.write(Double.toString(0.5 * (sampleTested.get((int)(sampleTested.size() / 2)) 
					+ sampleTested.get((int)(sampleTested.size() / 2) + 1))) + "\n");
			barreDeProgression.increment();
		}

		System.out.println("Test end");
		writer.close();
	}

}