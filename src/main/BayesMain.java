package main;
import java.io.*;
import mlearning.*;


public class BayesMain {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) throws IOException {
		//for (; Chi2Main.THRESHOLD < 1; Chi2Main.THRESHOLD += 0.01) {
			Chi2Main.start();
			NaiveBayes bayesien = new NaiveBayes(Chi2Main.corpus);
			bayesien.setIndependentWords(Chi2Main.removed);
			CV.validate((Classifier) bayesien, 4);
		// bayesien.test(Chi2Main.corpus.getTestData());
		//}
		
	}

}
