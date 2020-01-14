package utils;
import java.util.*;
import java.util.function.BiConsumer;
import java.io.*;



public class Corpus {
	
	public static Corpus DEFAULT;
	private Map<String, Class> CorpTotal = new HashMap<>(); 
	private Map<String, Class> Corp = new HashMap<>();
	public Map<String, Class> getCorp() {
		return Corp;
	}
	
	public Map<String, String> getTestData(int len, int offset) throws IOException {
		Map<String, String> test = new HashMap<>();
		HashMap<String, Class> corpus = new HashMap<>();
		Corp.clear();
		CorpTotal.forEach((c, v) -> Corp.put(c, new Class(v.getDocs())));
		
		totalDocs = 0;
		
		for (String k : new HashSet<>(Corp.keySet())) {
			corpus.put(k, Corp.get(k).slice(len, offset));
			totalDocs += CorpTotal.get(k).getDocs().size();
		}
		
		corpus.forEach((k, v) -> v.getTexts().forEach(t -> test.put(t, k)));
		return test;
	}
	

	private int totalDocs = 0;

	
	public Corpus() throws IOException {
		Corpus c;
		if(Corpus.DEFAULT != null) c = Corpus.DEFAULT;
		else c = new Corpus("D:\\Master\\S3\\TextMining\\20_newsgroups");
		
		Corp = new HashMap<>(c.Corp);
		CorpTotal = new HashMap<>(c.CorpTotal);
		totalDocs = c.totalDocs;
	}
	
	
	public Corpus(String path) throws IOException {
				
		File file = new File(path);
		for (File f : file.listFiles()) {
			Class c = new Class(f.getAbsolutePath());
			Corp.put(f.getName(), c);
			CorpTotal.put(f.getName(), new Class(c.getDocs()));
			int docs = c.getDocs().size();
			totalDocs += docs;
		}
		Corpus.DEFAULT = this;
	}
	
	
	public void remove(Set<String> indep) {
		for (String k : Corp.keySet()) {
			Class c = Corp.get(k);
			c.removeWords(indep);
			Corp.put(k, c);
		}
	}
	

	public void forEach(BiConsumer<? super String, ? super Class> callback) {
		Corp.forEach(callback);
	}

	public Collection<Class> classes() {
		return Corp.values();
	}
	

	public Set<String> getWordSet() {
		Set<String> words = new HashSet<>();
		forEach((w, c) -> words.addAll(c.words));
		return words;
	}
	
	public double probability(String c) {
		return Corp.get(c).getDocs().size() / (double) totalDocs;
	}
}
