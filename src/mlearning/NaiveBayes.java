package mlearning;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import utils.*;
import utils.Class;

public class NaiveBayes implements Classifier<String, String> {

	private Corpus Corp;
	private int m = 0;
	private Set<String> independent = new HashSet<>();

	private Map<String, Double> probabilities = new HashMap<>();

	public NaiveBayes(Corpus corpus) {
		Corp = corpus;
		Set<String> wordSet = Corp.getWordSet();
		m = wordSet.size();

	}

	public void setIndependentWords(Set<String> set) {
		this.independent = set;
	}

	@Override
	public String predict(String text) {
		PriorityQueue<Pair> prob = new PriorityQueue<>(
				Collections.reverseOrder());
		Document queryDoc = new Document(Arrays.asList(text.split(" ")));
		queryDoc.eliminateWords(independent);
		List<String> query = queryDoc.getWords();
		Corp.forEach((k, c) -> {
			Pair p = new Pair(k, Corp.probability(k));
			query.forEach(w -> {
				p.Value *= p(w, c);
			});
			prob.add(p);
		});
		Pair p = prob.poll();
		return p.key;
	}

	@Override
	public double test(Map<String, String> testData) {
		Map<String, Integer> tp = new HashMap<>();
		Corp.getCorp().forEach((c, v) -> tp.put(c, 0));
		Map<String, Integer> pred = new HashMap<>(tp);
		Map<String, Integer> actu = new HashMap<>(tp);

		int accuracy = 0;
		for (Map.Entry<String, String> el : testData.entrySet()) {
			String prediction = predict(el.getKey());
			pred.put(prediction, pred.get(prediction) + 1);
			actu.put(el.getValue(), actu.get(el.getValue()) + 1);
			if (prediction.equals(el.getValue())) {
				tp.put(prediction, tp.get(prediction) + 1);
				accuracy++;
			}
		}

		System.out.println();
		System.out.println("Evaluation metrics :");
		
		EvaluationPrinter eval = new EvaluationPrinter();
		
		Corp.forEach((c, v) -> {

			double p = tp.get(c) / (double) pred.get(c), 
					r = tp.get(c) / (double) actu.get(c),
					f = 2 * p * r / (p + r);
			

			if(pred.get(c) == 0) p = 0;
			if(actu.get(c) == 0) r = 0;
			
			if(p + r == 0) return;
			
			eval.addLine(c, new double[]{p, r, f});
			
		});

		eval.print();
		
		System.out.println("Accuracy: " + new DecimalFormat("00.0%").format( accuracy / (double) testData.size()));
		System.out.println();
		return accuracy / (double) testData.size();
	}

	private double p(String w, Class c) {
		String key = w + "_" + c;
		if (probabilities.containsKey(key))
			return probabilities.get(key);
		int n = c.countOccurrence();
		int nk = c.countOccurrence(w);
		double p = (nk + 1) / (double) (n + m);
		probabilities.put(key, p);
		// System.out.println("p("+c+", "+w+") = " + p);
		return p;
	}

	@Override
	public Map<String, String> getTestData(int len, int offset)
			throws IOException {
		Map<String, String> map = Corp.getTestData(len, offset);
		m = Corp.getWordSet().size();
		return map;
	}
}
