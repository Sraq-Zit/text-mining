package main;
import java.io.*;
import java.util.*;
import utils.*;
import mlearning.*;


public class ClusteringMain {
	public static void main(String[] args) throws IOException {
		Map<String, String> data = new Corpus().getTestData(100, 0);
		Set<Document> docs = new HashSet<>();
		data.forEach((text, c) -> docs.add(new Document(Arrays.asList(text.split(" ")), c)));
		System.out.println("Clustering ...");
		System.out.println("Kmean:");
		Kmeans clustering = new Kmeans(docs, 20);
		clustering.getClusters().forEach((c, d) -> System.out.println(c + ": " + d));
		System.out.println();
		System.out.println("Hierarchical:");
		Hierarchical clustering2 = new Hierarchical(docs, 20);
		clustering2.getClusters().forEach((c, d) -> System.out.println(c + ": " + d));
		
	}

}
