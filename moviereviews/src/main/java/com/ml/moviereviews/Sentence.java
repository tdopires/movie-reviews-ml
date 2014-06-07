package com.ml.moviereviews;

public class Sentence {
	
	private Long phraseId;
	
	private Long sentenceId;
	
	private String phrase;
	
	private int sentiment;

	public Sentence(Long phraseId, Long sentenceId, String phrase, int sentiment) {
		super();
		this.phraseId = phraseId;
		this.sentenceId = sentenceId;
		this.phrase = phrase;
		this.sentiment = sentiment;
	}

	public Long getPhraseId() {
		return phraseId;
	}

	public Long getSentenceId() {
		return sentenceId;
	}

	public String getPhrase() {
		return phrase;
	}

	public int getSentiment() {
		return sentiment;
	}
	
	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}
}
