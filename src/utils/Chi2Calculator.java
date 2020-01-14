package utils;
import java.util.*;



public class Chi2Calculator {
	private Corpus Corp;
	public Chi2Calculator(Corpus corpus) {
		Corp = corpus;
	}
	
	public Set<String> ExtractWords(){
		Set<String> words = new HashSet<>();
		Corp.forEach((k, c) -> words.addAll(c.getWords()));
		return words;
	}
	
	public Map<String, Double> listChi2max(){
		Map<String, Double> list = new HashMap<>();
		ExtractWords().forEach(w -> list.put(w, chi2max(w)));
		return list;
	}
	
	public double chi2max(String t) {
		PriorityQueue<Double> queue = new PriorityQueue<>(Collections.reverseOrder());
		for (Class c : Corp.classes()) 
			queue.add(chi2(t, c));
		return queue.poll();
	}
	
	private double chi2(String t, Class cls) {
		int N = cls.getDocs().size(),
			A = cls.count(doc -> doc.has(t) ? 1 : 0),
			B = 0,
			C = N - A,
			D = 0;
		
		for (Class c : Corp.classes())
			if(c != cls) {
				B += c.count(doc -> doc.has(t) ? 1 : 0);
				D += c.count(doc -> doc.has(t) ? 0 : 1);
			}
		
		return N * Math.pow(A*D - B*C, 2) / (double) ((A+B) * (A+C) * (B+D) * (C+D));
		           
	}
	
}
