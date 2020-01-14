package main;
import java.io.*;
import java.util.*;
import utils.*;

public class Chi2Main {
	public static double THRESHOLD = 1.9;
	public static Corpus corpus;
	public static Set<String> removed;
	public static void main(String[] args) throws IOException {
		start();
	}
	
	public static void start() throws IOException {
		corpus = new Corpus();
		removed = new HashSet<>();
		Set<String> indep = new HashSet<>();
		Chi2Calculator calculator;
		System.out.println("Corpus has " + corpus.getWordSet().size() + " words before chi2 test");
		do {
			indep.clear();
			calculator = new Chi2Calculator(corpus);
			calculator.listChi2max().forEach((k, v) -> {
				if (v < THRESHOLD || v == Double.NaN)
					indep.add(k);
			});
			removed.addAll(indep);
			corpus.remove(indep);
			System.out.println(indep.size() + " words with chi2 less than " + THRESHOLD + " :");
			//System.out.println(indep);
		} while(indep.size() > 0);

	}

}
