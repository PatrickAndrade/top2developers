package epfl.lsr.bachelor.project.benchmarks;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Benchmark1ToN {
	
	static String[] request = {"set", "get"};
	final static int maxNumberOfClients = 10;
	final static int numberRequestSend = 100;
	
	public static void main(String[] args) throws IOException {
		DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(new File("results.txt")));
		double average = 0;
		for (int numberOfClients = 1; numberOfClients < maxNumberOfClients; numberOfClients++) {
			GeneralBenchmarkPipelined generalBenchmarkPipelined = new GeneralBenchmarkPipelined(
					request, numberRequestSend, numberOfClients);
			average = generalBenchmarkPipelined.start();
			dataOutputStream.writeUTF(Double.toString(average) + "\n");
		}
		
		dataOutputStream.close();
	}
}
