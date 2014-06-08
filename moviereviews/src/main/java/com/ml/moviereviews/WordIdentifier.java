package com.ml.moviereviews;

import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class WordIdentifier {

	private static final String[] relevantPOSTypes = new String[] { "JJ" };

	private static POSModel model;
	private POSTaggerME tagger;
	private String[] tags;

	static {
		ClassLoader classLoader = WordIdentifier.class.getClassLoader();
		try (InputStream stream = classLoader
				.getResourceAsStream("en-pos-perceptron.bin")) {
			model = new POSModel(stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public WordIdentifier(String[] sentenceWords) {
		tagger = new POSTaggerME(model);
		tags = tagger.tag(sentenceWords);
	}

	public String getWordTag(int wordIndexOnSentence) {
		return tags[wordIndexOnSentence];
	}

	public boolean isRelevantWord(int wordIndexOnSentence) {
		for (String posType : relevantPOSTypes) {
			if (posType.equals(tags[wordIndexOnSentence]))
				return true;
		}
		return false;
	}
}
