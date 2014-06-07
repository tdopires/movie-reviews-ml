package com.ml.moviereviews;

import java.util.List;


public class Main {

	private static final String TRAIN_FILE = "/files/train.tsv";
	private static final String TEST_FILE = "/files/test.tsv";
	
	public static void main(String[] args) {
		List<Sentence> trainSentences = Reader.readSentences(TRAIN_FILE);
		List<Sentence> testSentences = Reader.readSentences(TEST_FILE);
		
		for (Sentence sentence : testSentences) {
			System.out.println(sentence.getSentence() + '\n');
		}
	}
}
