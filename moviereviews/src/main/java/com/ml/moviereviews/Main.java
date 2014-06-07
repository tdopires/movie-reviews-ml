package com.ml.moviereviews;

import java.util.List;


public class Main {

	private static final String TRAIN_FILE = "/files/train.tsv";
	private static final String TEST_FILE = "/files/test.tsv";

	public static void main(String[] args) {
		List<Sentence> trainSentences = Reader.readSentences(TRAIN_FILE);
//		List<Sentence> testSentences = Reader.readSentences(TEST_FILE);
//
//		for (String term : trainSentences.get(0).getFrequencyByTerm().keySet()) {
//			System.out.println(term + ": " + trainSentences.get(0).getFrequencyByTerm().get(term));
//		}
//
//		for (Sentence sentence : trainSentences) {
//			System.out.println(sentence.getPhraseId() + "\t" + sentence.getSentenceId() + "\t" + sentence.getPhrase() + "\t" + sentence.getSentiment() + "\n");
//		}
//
//		for (Sentence sentence : testSentences) {
//			System.out.println(sentence.getPhraseId() + "\t" + sentence.getSentenceId() + "\t" + sentence.getPhrase() + "\t" + sentence.getSentiment() + "\n");
//		}

		DataSet.generateDataSet(trainSentences);
//		DataSet.generateDataSet(testSentences);
	}
}
