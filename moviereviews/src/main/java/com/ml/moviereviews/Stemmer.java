package com.ml.moviereviews;

import org.tartarus.snowball.ext.EnglishStemmer;

public class Stemmer {
	
	static EnglishStemmer stemmer = new EnglishStemmer();
	
	public static String stem(String word) {
		
		stemmer.setCurrent(word);
		boolean result = stemmer.stem();
		if (!result) {
			return word;
		}
		
		return stemmer.getCurrent();
	}
}
