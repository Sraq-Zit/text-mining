package utils;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;


public class Class {
	public static int MAX_ITEMS = 5;
	
	public Set<String> words = new HashSet<>();
	
	private String root; // path of data
	private Map<String, Document> Docs = new HashMap<>(); // <doc, <word, tf-idf>>
	private Map<String, Double> Idf = new HashMap<>(); // <word, idf>
	private Map<String, Integer> Occ = new HashMap<>();


	public Class() throws IOException {
		this("D:/Master/S3/TextMining/Texts");
	}

	public Class(Map<String, Document> docs) {
		Docs = new HashMap<>(docs);
		calculateIdf();
	}
	
	public Class(String path) throws IOException {
		//System.out.println(path);
		root = path;
		// initialize data path on File object
		File file = new File(root);
		// iterate over text files
		int i = 0;
		for (File f : file.listFiles()) {
			try {
				Docs.put(f.getName(), new Document(f.getAbsolutePath()));
				if(i++ >= MAX_ITEMS) break;
			} catch (Exception e) {
				System.out.println(f.getAbsolutePath() + " | error: " + e.getMessage());
			}
		}
		
		calculateIdf();
	}
	
	public Set<String> getWords() {
		return Idf.keySet();
	}
	
	
	public Set<String> getTexts() {
		Set<String> texts = new HashSet<>();
		Docs.forEach((k, doc) -> texts.add(String.join(" ", doc.getWords())));
		return texts;
	}
	
	public int countOccurrence(String w) {
		if(Occ.containsKey(w)) return Occ.get(w);
		int count = count(d -> d.words.containsKey(w) ? d.words.get(w) : 0);
		Occ.put(w, count);
		return count;
	}
	
	public Map<String, Document> getDocs() {
		return Docs;
	}

	public int countOccurrence() {
		int count = 0;
		for (String t : words)
			count += countOccurrence(t);
		
		return count;
	}
	
	public void removeWords(Set<String> ws) {
		ws.forEach(w -> Idf.remove(w));
		for (String k : Docs.keySet()){
			Document d = Docs.get(k);
			d.eliminateWords(ws);
			Docs.put(k, d);
		}
		
	}
	
	public Class slice(int pourcentage, int offset) throws IOException {
		int l = pourcentage = (int) (Docs.size() * pourcentage / 100d);
		int i = 0;
		Map<String, Document> corpus = new HashMap<>();
		for (Entry<String, Document> entry : new HashSet<>(Docs.entrySet()))
			if(i++ >= offset * pourcentage && l-- > 0) {
				corpus.put(entry.getKey(), entry.getValue());
				Docs.remove(entry.getKey());
			}
		return new Class(corpus);
	}
	
	public int count(Function<Document, Integer> cond){
		int c = 0;
		for (Entry<String, Document> doc : Docs.entrySet())
			c += cond.apply(doc.getValue());
		
		return c;
	}
	
	private double getTfIdf(String doc, String word){
		if(!Docs.get(doc).wordSet.contains(word) || !Idf.containsKey(word))
			return 0;
		
		return Docs.get(doc).getTf(word) * Idf.get(word);
	}
	
	private Map<String, Double> getTfIdf(String doc){
		Map<String, Double> tfidf = new HashMap<>();
		Docs.get(doc).wordSet.forEach(s -> tfidf.put(s, getTfIdf(doc, s)));
		return tfidf;
	}
	// idf boolean tells whether to add up counts of idf or not
	private void calculateIdf(){
		Idf = new HashMap<>();
		Occ = new HashMap<>();
		
		for (Entry<String, Document> doc : Docs.entrySet()){
			doc.getValue().wordSet.forEach(word -> {
				double count = Idf.containsKey(word) ? Idf.get(word) : 0;
            	Idf.put(word, count+1);
			});
			
			doc.getValue().words.forEach((w, o) -> words.add(w));
			
		}
		
		for (Entry<String, Double> e : new HashMap<>(Idf).entrySet()){
			Idf.put(e.getKey(),
					Math.log(Idf.keySet().size() / e.getValue()));
		}
	}
	
	public PriorityQueue<Pair> getCosineSimilarity(String query) throws IOException{
		/*
		 * we calculate this formula : http://prntscr.com/pp7634
		 * it is cosine similarity, in this case cosine of tf-idf of the query and each document
		 * we save the result in a map <doc, cosine>
		 */
		
		PriorityQueue<Pair> cos = new PriorityQueue<Pair>();
		Document req = new Document(query);
		
		Map<String, Double> tf_idf = new HashMap<>();
		for (String s : req.wordSet)
			if(Idf.containsKey(s)) // calculate tf-idf if we have the word in our corpus
				tf_idf.put(s, req.getTf(s) * Idf.get(s));
		
		for (String doc : Docs.keySet()){
			double q_n = Norm(tf_idf.values()); // check http://prntscr.com/pp793p
			double doc_n = Norm(getTfIdf(doc).values()); // check http://prntscr.com/pp79fx
			double cosine = DotProduct(tf_idf, getTfIdf(doc)) / (q_n * doc_n); // DotProduct is this http://prntscr.com/pp7azz 
			cos.add(new Pair(doc, cosine));
		}
		
		return cos;
	}
	
	public static float Norm(Collection<Double> arr){
		float norm = 0;
		for (double v : arr) {
			norm += v*v;
		}
		return (float) Math.sqrt(norm);
	}
	
	public static float DotProduct(Map<String, Double> map1, Map<String, Double> map2) {
		float product = 0;
		for (Entry<String, Double> e : map1.entrySet())
			if(map2.containsKey(e.getKey()))
				product += e.getValue() * map2.get(e.getKey());
		return product;
	}
	
	@Override
	public String toString() {
		return Docs.toString();
	}
	
}
