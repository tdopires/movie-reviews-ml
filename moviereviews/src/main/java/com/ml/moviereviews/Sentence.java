package com.ml.moviereviews;

import java.util.HashMap;
import java.util.Map;

import opennlp.tools.postag.POSTaggerME;

public class Sentence {

	private Long phraseId;

	private Long sentenceId;

	private String phrase;

	private int sentiment;

	private Map<String, Long> frequencyByTerm;

	public Sentence(Long phraseId, Long sentenceId, String phrase, int sentiment) {
		super();
		this.phraseId = phraseId;
		this.sentenceId = sentenceId;
		this.phrase = phrase;
		this.sentiment = sentiment;
		this.frequencyByTerm = calculateFrequencyByTerm();
	}

	private Map<String, Long> calculateFrequencyByTerm() {
		Map<String, Long> frequencyByTerm = new HashMap<String, Long>();
		String[] words = getPhrase().split(" ");

		String term;
		Long frequency;

		for (String word : words) {
			term = Stemmer.stem(word);
			frequency = frequencyByTerm.get(term);

			if (frequency != null) {
				frequencyByTerm.put(term, ++frequency);
			} else {
				frequencyByTerm.put(term, 1l);
			}
		}

		return frequencyByTerm;
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

	public Map<String, Long> getFrequencyByTerm() {
		return frequencyByTerm;
	}

	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}
}
