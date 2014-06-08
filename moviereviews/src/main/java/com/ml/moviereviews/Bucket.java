package com.ml.moviereviews;

import java.util.HashMap;
import java.util.Map;

public class Bucket {

	private Map<String, Long> frequencyByTerm = new HashMap<String, Long>();

	public Bucket() {
		super();
	}

	public Map<String, Long> getFrequencyByTerm() {
		return frequencyByTerm;
	}
	
	public void addTerm(String term, Long q) {
		Long frequency = frequencyByTerm.get(term);
		
		if (frequency != null) {
			frequencyByTerm.put(term, frequency + q);
		} else {
			frequencyByTerm.put(term, q);
		}
	}
}
