package main;
import java.io.*;
import java.util.*;
import utils.*;
import mlearning.*;


public class ClusteringMain {
	public static Clustering.Algorithm ALGO = Clustering.Algorithm.KMEANS;
	public static void main(String[] args) throws IOException {
		Map<String, String> data = new Corpus().getTestData(100, 0);
		Set<Document> docs = new HashSet<>();
		data.forEach((text, c) -> docs.add(new Document(Arrays.asList(text.split(" ")), c)));
		System.out.println("Clustering ...");
		
		Clustering clustering = new Clustering(ALGO, docs, 20);
		clustering.getClusters().forEach((c, d) -> System.out.println(c + ": " + d));
		
	}

}
