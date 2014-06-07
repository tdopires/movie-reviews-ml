package com.ml.moviereviews;

public class Sentence {
	
	private String sentence;
	
	private int sentiment;

	public Sentence(String sentence, int sentiment) {
		super();
		this.sentence = sentence;
		this.sentiment = sentiment;
	}

	public String getSentence() {
		return sentence;
	}

	public int getSentiment() {
		return sentiment;
	}
}
