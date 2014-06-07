package com.ml.moviereviews;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class WordIdentifier {

	private static final String[] relevantPOSTypes = new String[] { "n", "prop" };

	// TODO colocar isso pra tirar palavras que sao numeros
	// private static final Pattern onlyNumbers = Pattern.compile("\\d*");

	private static POSModel model;
	private POSTaggerME tagger;
	private String[] tags;

	static {
		InputStream stream = WordIdentifier.class.getClassLoader()
				.getResourceAsStream("pt-pos-perceptron.bin");
		try {
			model = new POSModel(stream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public WordIdentifier(String[] sentenceWords) {
		tagger = new POSTaggerME(model);
		tags = tagger.tag(sentenceWords);
	}

	public boolean isRelevantWord(int wordIndexOnSentence) {
		for (String posType : relevantPOSTypes) {
			if (posType.equals(tags[wordIndexOnSentence]))
				return true;
		}
		return false;
	}
}
