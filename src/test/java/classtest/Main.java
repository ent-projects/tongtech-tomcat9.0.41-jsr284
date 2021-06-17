package classtest;

import org.apache.lucene.util.RamUsageEstimator;

public class Main {

	public static void main(String[] args) {
		
		new People();
		People p = new People();
		System.out.println(RamUsageEstimator.sizeOf("111111111111111111111"));
		System.out.println(RamUsageEstimator.sizeOfObject(p));

	}
	
}
