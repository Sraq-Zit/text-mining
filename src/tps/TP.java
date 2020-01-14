package tps;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import main.BayesMain;
import main.Chi2Main;
import main.ClusteringMain;
import main.SearchEngine;
import mlearning.Clustering;
import utils.Document;
import utils.Class;

public final class TP {
	
	public static void main(String[] args) throws IOException {
		/*-----------------------------------------------------------*/
		
		//		TP1		//
		Class.MAX_ITEMS = 100; // Specify max number of documents
		SearchEngine.main(args);

		/*-----------------------------------------------------------*/
		
		//		TP2		//
		
		Document.CHARSET = StandardCharsets.ISO_8859_1; // Avoid having MalformedInputException
		Class.MAX_ITEMS = 20; // use 20 documents per class to optimize time of execution
		Chi2Main.main(args);
		
		/*-----------------------------------------------------------*/
		
		//		TP3 + TP4 + TP5		//
		BayesMain.main(args);		/* TP3: Naive bayes				: mlearning.NaiveBayes	
									 * TP4: Cross validation		: mlearning.CV.validate
									 * TP5: Evaluation metrics		: mlearning.NaiveBayes.test (from line 64)
									 * (Precision, Recall, FMesure)	! Table printed via utils.EvaluationPrinter
									 */
		/*-----------------------------------------------------------*/
		//		TP6		//
		ClusteringMain.ALGO = Clustering.Algorithm.KMEANS;
		ClusteringMain.main(args);
		
		/*-----------------------------------------------------------*/

		//		TP7		//
		ClusteringMain.ALGO = Clustering.Algorithm.HIERARCHICAL;
		ClusteringMain.main(args);
		
		/*-----------------------------------------------------------*/
		
		
		
	}

}
