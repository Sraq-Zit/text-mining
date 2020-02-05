package mlearning;
import java.util.*;
import utils.*;


public class Hierarchical {
	private int k = 20;
	Map<Document, Integer> Docs = new HashMap<>();
	
	public Map<Integer, Set<Document>> getClusters() {
		Map<Integer, Set<Document>> clusters = new HashMap<>();
		for (int i = 0; i < k; i++) clusters.put(i, new HashSet<>());
		Docs.forEach((d, c) -> clusters.get(c).add(d));
		
		return clusters;
	}
	

	int i = 0;
	public Hierarchical(Collection<Document> data, int k) {
		List<Document> docs = new ArrayList<>(data);
		docs.forEach(d -> Docs.put(d, i++));
		this.k = data.size();
		while(this.k > k) {
			Set<String> clusteringMap = new HashSet<>();
			Map<Integer, Document> cGrav = getCentroids();
			PriorityQueue<Pair> ds = new PriorityQueue<>();
			new HashMap<>(cGrav).forEach((c, m) -> {
				cGrav.forEach((cls, mean) -> {
					int min = Integer.min(cls, c), max = Integer.max(cls, c);
					String key = min+","+max;
					if(clusteringMap.contains(key) || c == cls) return;
					ds.add(new Pair(key, dist2(m, mean)));
					clusteringMap.add(key);
				});
			});
			if(ds.size() == 0) break;
			String[] values = ds.poll().key.split(",");
			int replace = Integer.parseInt(values[0]),
				by = Integer.parseInt(values[1]);
			Docs.replaceAll((d, c) -> c == replace ? by : c);
			this.k--;
		}
		
		Map<Integer, Integer> indexing = new HashMap<>();
		i = 0;
		Docs.replaceAll((d, c) -> {
			if(!indexing.containsKey(c)) indexing.put(c, i++);
			return indexing.get(c);
		});
		
		System.out.println("Done!");
		
	}
	
	
	private static int d;
	private static double dist2(Document mean1, Document mean2) {
		d = 0;
		Set<String> words = new HashSet<>(mean1.wordSet);
		words.addAll(mean2.mean.keySet());
		
		words.forEach(w -> {
			double oc1 = mean1.mean.containsKey(w) ? mean1.mean.get(w) : 0,
				oc2 = mean2.mean.containsKey(w) ? mean2.mean.get(w) : 0;
			
			d += Math.pow(oc1 - oc2, 2);
		});
		return Math.sqrt(d);
	}
	
	private Map<Integer, Document> getCentroids() {
		Map<Integer, Document> centroids = new HashMap<>();
		Map<Integer, Integer> sizes = new HashMap<>();
		
		
		Docs.forEach((d, c) -> {
			sizes.put(c, sizes.containsKey(c) ? sizes.get(c) + 1 : 1);
			d.words.forEach((w, occ) -> {
				if(c == -1) return;
				if(!centroids.containsKey(c)) centroids.put(c, new Document());
				
				Document g = centroids.get(c);
				g.mean.put(w, g.mean.containsKey(w) ? g.mean.get(w) + occ : occ);
			});
		});
		
		centroids.forEach((c, d) -> d.mean.forEach((w, occ) -> d.mean.put(w, occ / (double) sizes.get(c))));
		
		return centroids;
	}
	
}
