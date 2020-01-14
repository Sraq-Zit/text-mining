package mlearning;
import java.io.*;
import java.util.*;


public abstract class CV {
	
	public static void validate(Classifier<? super Object, ? super Object> classifier, int folds) throws IOException {
		int foldLen = 100/folds;
		double acc = 0;
		double accuracy[] = new double[folds];
		for (int i = 0; i < folds; i++) 
			acc += accuracy[i] = classifier.test(classifier.getTestData(foldLen, i));
		
		System.out.println(Arrays.toString(accuracy) + " --> " + (acc / (double) accuracy.length));
	}
	
}
