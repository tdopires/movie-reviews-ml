package com.ml.moviereviews;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataSet {

	private static final String TRAIN_RESULT_FILE = "/tmp/train_result_file.txt";
	private static final String TEST_RESULT_FILE = "/tmp/test_result_file.txt";

	public static void generateDataSet(List<Sentence> sentences) {
		List<Sentence> trainSentences = new ArrayList<Sentence>();
		List<Sentence> testSentences = new ArrayList<Sentence>();
		
		for (int i = 0; i < sentences.size(); i++) {
			if (i % 5 == 0) {
				testSentences.add(sentences.get(i));
			} else {
				trainSentences.add(sentences.get(i));
			}
		}
		
		Map<String, Long> numberOfDocumentsThatTheTermAppears = calculateNumberOfDocumentsThatTheTermAppears(trainSentences);
		Map<String, Double> idfs = calculateIdfs(numberOfDocumentsThatTheTermAppears, trainSentences.size());

		writeTrainDataSetFile(trainSentences, idfs);
		writeTestDataSetFile(testSentences, idfs);
	}

	private static void writeTestDataSetFile(List<Sentence> sentences, Map<String, Double> idfs) {
		Map<String, Long> frequencyByTerm;
		BufferedWriter writer = null;
		Long tf;
		Double idf;
		
		try {
			writer = new BufferedWriter(new FileWriter(new File(TEST_RESULT_FILE)));
			System.out.println("Sentences: " + sentences.size() + " - Stemmed Terms:" + idfs.keySet().size());
			
			for (Sentence sentence : sentences) {
				frequencyByTerm = sentence.getFrequencyByTerm();
				
				for (String term : idfs.keySet()) {
					tf = frequencyByTerm.get(term);
					idf = idfs.get(term);
					
					if (tf != null) {
						writer.write(String.valueOf(tf * idf) + ",");
//						writer.write("1,");
					} else {
						writer.write("0,");
					}
				}
				
				writer.write(String.valueOf(sentence.getSentiment()) + "\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static void writeTrainDataSetFile(List<Sentence> sentences, Map<String, Double> idfs) {
		Map<String, Long> frequencyByTerm;
		BufferedWriter writer = null;
		Long tf;
		Double idf;
		
		try {
			writer = new BufferedWriter(new FileWriter(new File(TRAIN_RESULT_FILE)));
			System.out.println("Sentences: " + sentences.size() + " - Stemmed Terms:" + idfs.keySet().size());
			
			for (Sentence sentence : sentences) {
				frequencyByTerm = sentence.getFrequencyByTerm();
				
				for (String term : idfs.keySet()) {
					tf = frequencyByTerm.get(term);
					idf = idfs.get(term);
					
					if (tf != null) {
						writer.write(String.valueOf(tf * idf) + ",");
//						writer.write("1,");
					} else {
						writer.write("0,");
					}
				}
				
				writer.write(String.valueOf(sentence.getSentiment()) + "\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static Map<String, Double> calculateIdfs(Map<String, Long> numberOfDocumentsThatTheTermAppears, int n) {
		Map<String, Double> result = new HashMap<String, Double>();
		Long frequency;
		
		for (String term : numberOfDocumentsThatTheTermAppears.keySet()) {
			frequency = numberOfDocumentsThatTheTermAppears.get(term);
			result.put(term, Math.log10(n/frequency));
		}
		
		return result;
	}

	private static Map<String, Long> calculateNumberOfDocumentsThatTheTermAppears(List<Sentence> sentences) {
		Map<String, Long> result = new HashMap<String, Long>();
		Long accumulatedFrequency;
		
		for (Sentence sentence : sentences) {
			Map<String, Long> frequencyByTerm = sentence.getFrequencyByTerm();
			for (String term : frequencyByTerm.keySet()) {
				accumulatedFrequency = result.get(term);
				
				if (accumulatedFrequency != null) {
					result.put(term, accumulatedFrequency + 1);
				} else {
					result.put(term, 1l);
				}
			}
		}
		
		return result;
	}
}
