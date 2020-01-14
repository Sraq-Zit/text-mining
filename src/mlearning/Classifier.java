package mlearning;
import java.io.IOException;
import java.util.Map;


public abstract interface Classifier <X, y> {

	public abstract y predict(X x);
	
	public abstract double test(Map<X, y> testData);
	
	public abstract Map<X, y> getTestData(int len, int offset) throws IOException;
	
}
