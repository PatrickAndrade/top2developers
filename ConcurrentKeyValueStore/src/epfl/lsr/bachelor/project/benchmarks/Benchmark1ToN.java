package epfl.lsr.bachelor.project.benchmarks;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

public class Benchmark1ToN {

	static String[] request = { "set", "get" };
	final static int maxNumberOfClients = 500;
	final static int numberRequestSend = 100;

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
				maxNumberOfClients);
		double average = 0;

		for (int numberOfClients = 1; numberOfClients <= maxNumberOfClients; numberOfClients++) {
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

class BarreDeProgression extends JFrame {

	private static final long serialVersionUID = 1L;
	private JProgressBar barreProgression;
	private int i = 0;

	public BarreDeProgression(int max) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Barre de progression");
		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(175, 70));
		setResizable(false);
		barreProgression = new JProgressBar(0, max - 1);
		barreProgression.setStringPainted(true);
		getContentPane().add(barreProgression);
		pack();
		setVisible(true);
	}

	public void increment() {
		i++;
		barreProgression.setValue(i);
	}
}
