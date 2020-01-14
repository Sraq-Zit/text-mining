package utils;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.nio.charset.*;

import org.apache.lucene.analysis.StopAnalyzer;
import org.tartarus.snowball.ext.porterStemmer;

import safar.basic.morphology.stemmer.factory.StemmerFactory;
import safar.basic.morphology.stemmer.interfaces.IStemmer;
import safar.basic.morphology.stemmer.model.*;


public class Document {
	public static Charset CHARSET = StandardCharsets.UTF_8;
	public enum TYPE {
		DEFAULT,
		MEAN
	}
	public static final int DEFAULT = 0;
	public static final IStemmer stemmer = StemmerFactory.getKhojaImplementation();
	private String path;
	private TYPE type = TYPE.DEFAULT;
	public Map<String, Integer> words = new HashMap<>();
	public Map<String, Double> TF = new HashMap<>();
	public Map<String, Double> TF_IDF = new HashMap<>();
	public Set<String> wordSet = new HashSet<>();
	
	// For clustering
	public Map<String, Double> mean = new HashMap<>();

	public Document() {
		type = TYPE.MEAN;
	}
	
	public Document(List<String> words) {
		prepareDoc(String.join(" ", words));
	}
	
	public Document(List<String> words, String path) {
		this.path = path;
		prepareDoc(String.join(" ", words));
	}
	
	public Document(String path) throws IOException{
		this.path = path;
		File f = new File(path);
		String text;
		if(f.exists()){
			List<String> lines = Files.readAllLines(Paths.get(f.getAbsolutePath()), CHARSET);
			text = String.join(" ", lines);
		} else 
			text = path;

		prepareDoc(text);
	}
	
	public void eleminateWord(String w) {
		eliminateWords(Arrays.asList(new String[]{ w }));
	}
	
	public void eliminateWords(Collection<String> ws) {
		wordSet.removeAll(ws);
		ws.forEach(w -> words.remove(w));
		prepareDoc();
	}
	
	private void prepareDoc() {
		prepareDoc(String.join(" ", wordSet));
	}
	
	private void prepareDoc(String text) {
		text = text.replaceAll("[@)('\"\\/.,;:_-]", " ");
		text = text.replaceAll("[^\\p{L}\\p{Nd} ]", "");
		text = text.replaceAll("\\s{2,}", " ");
		text = text.toLowerCase();
		wordSet.clear();
		TF.clear();
		TF_IDF.clear();
		boolean countOccurences = words.size() == 0;
		List<WordStemmerAnalysis> analysis = stemmer.stem(text);
        for (WordStemmerAnalysis wordAnalysis: analysis) {
            List<StemmerAnalysis> listOfStems = wordAnalysis.getListStemmerAnalysis();
            for (StemmerAnalysis stem: listOfStems) {
            	String stm = stem.getMorpheme(); // word origin
            	Set<String> sw = new HashSet<>(Arrays.asList(StopAnalyzer.ENGLISH_STOP_WORDS));
                if(!stem.getType().equals("STOPWORD") && stm != null && !sw.contains(stm)){
                	if(!stem.getType().equals("NOT STEMMED")){
	                	porterStemmer pStemmer = new porterStemmer();
	                	pStemmer.setCurrent(stm);
	                	pStemmer.stem();
	                	stm = pStemmer.getCurrent();
                	}
                	if(stm.length() < 2) continue;
                	double count = TF.containsKey(stm) ? TF.get(stm) : 0;
                	TF.put(stm, count+1);
                	if(countOccurences) words.put(stm, (int) count+1);
                	wordSet.add(stm);
                }
                break;
            }
            
        }
		TF.replaceAll((k, v) -> v / (double) wordSet.size());
	}
	
	public double getTf(String word) {
		if(!TF.containsKey(word)) return 0;
		return TF.get(word);
	}
	
	public String getPath(){
		return path;
	}
	
	public boolean has(String w) {
		return wordSet.contains(w);
	}
	
	public List<String> getWords(){
		List<String> wList = new ArrayList<>();
		words.forEach((w, occ) -> wList.addAll(Collections.nCopies(occ, w)));
		return wList;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return type == TYPE.DEFAULT ? path : mean.toString();
	}
}
