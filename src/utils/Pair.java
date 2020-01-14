package utils;

public class Pair implements Comparable<Pair> {
	public String key;
	public double Value;
	public Pair(Object k, double v) {
		key = k.toString(); Value = v;
	}
	@Override
	public int compareTo(Pair o) {
		return Double.compare(Value, o.Value);
	}
	
	@Override
	public String toString() {
		return key+"="+Value;
	}
	
}
