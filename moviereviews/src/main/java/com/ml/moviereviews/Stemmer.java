package com.ml.moviereviews;

import org.tartarus.snowball.ext.EnglishStemmer;

public class Stemmer {
	
	public static String stem(String word) {
		
		EnglishStemmer stemmer = new EnglishStemmer();
		
		stemmer.setCurrent(word);
		boolean result = stemmer.stem();
		if (!result) {
			return word;
		}
		
		return stemmer.getCurrent();
	}
}
