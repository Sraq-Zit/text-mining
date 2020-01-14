package main;
import java.io.*;
import java.util.*;

import utils.*;
import utils.Class;

public class SearchEngine {

	public static void main(String[] args) throws IOException {
		
		System.out.println("Type query: ");
		String query = new BufferedReader(new InputStreamReader(System.in)).readLine();
		
		Class corpus = new Class();
		PriorityQueue<Pair> similarities = corpus.getCosineSimilarity(query);
		String key = "";
		double max = -1;
		Pair p;
		while ((p = similarities.poll()) != null) {
			System.out.println("doc:                " + p.key);
			System.out.println("cosine similarity:  " + p.Value);
			System.out.println("==============================================");
			
			if(p.Value > max) {
				key = p.key;
				max = p.Value;
			}
		}
		
		System.out.println("Most relevant document for the query is : " + key);
		System.out.println("with cosine similarity of               : " + max);
	}

}
