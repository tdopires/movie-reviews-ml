package com.ml.moviereviews;

import java.util.List;


public class Main {

	private static final String TRAIN_FILE = "/files/train.tsv";

	public static void main(String[] args) {
		List<Sentence> trainSentences = Reader.readSentences(TRAIN_FILE);
		DataSetBuckets.generateDataSet(trainSentences);
	}
}
