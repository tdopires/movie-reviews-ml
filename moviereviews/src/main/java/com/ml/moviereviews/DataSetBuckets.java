package com.ml.moviereviews;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DataSetBuckets {
	
	private static final String TRAIN_RESULT_FILE = "/tmp/train_result_file.txt";
	private static final int BUCKETS = 5;

	public static void generateDataSet(List<Sentence> sentences) {
		Map<Integer, Bucket> buckets = new HashMap<Integer, Bucket>();
		
		for (int i = 0; i < BUCKETS; i++) {
			buckets.put(i, new Bucket());
		}
		
		fillBuckets(buckets, sentences);
		writeDataSetFile(buckets, sentences);
	}

	private static void fillBuckets(Map<Integer, Bucket> buckets, List<Sentence> sentences) {
		Bucket bucket;
		
		for (Sentence sentence : sentences) {
			bucket = buckets.get(sentence.getSentiment());
			
			for (Entry<String, Long> entry : sentence.getFrequencyByTerm().entrySet()) {
				bucket.addTerm(entry.getKey(), entry.getValue());
			}
		}
	}

	private static void writeDataSetFile(Map<Integer, Bucket> buckets, List<Sentence> sentences) {
		Map<String, Long> frequencyByTerm;
		BufferedWriter writer = null;
		Long tf, n, bucketFrequency, zeros = 0l;
		Double idf;
		Bucket bucket;
		Double[] line = new Double[BUCKETS + 1];
		
		try {
			writer = new BufferedWriter(new FileWriter(new File(TRAIN_RESULT_FILE)));
			System.out.println("Sentences: " + sentences.size());
			
			for (Sentence sentence : sentences) {
				frequencyByTerm = sentence.getFrequencyByTerm();
				line[BUCKETS] = 0d;

				for (int i = 0; i < BUCKETS; i++) {
					bucket = buckets.get(i);
					line[i] = 0d;
					
					for (String term : frequencyByTerm.keySet()) {
						tf = frequencyByTerm.get(term);
						
						n = calculateN(bucket);
						bucketFrequency = calculateBucketFrequency(bucket, term);
						
						if (bucketFrequency > 0) {
							idf = Math.log10(n/bucketFrequency);
							line[i] += tf * idf;
						}
					}
					
					if (line[i] > 0) {
						line[BUCKETS] = 1d;
					}
				}

				if (line[BUCKETS] > 0) {
					for (int i = 0; i < BUCKETS; i++) {
						writer.write(line[i] + ",");
					}
					
					writer.write(String.valueOf(sentence.getSentiment()) + "\n");
					zeros++;
				}
			}
			
			System.out.println("Zeros: " + zeros);
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

	private static Long calculateBucketFrequency(Bucket bucket, String term) {
		Long n = 0l;
		
		for (Entry<String, Long> entry : bucket.getFrequencyByTerm().entrySet()) {
			if (entry.getKey().equals(term)) {
				n += entry.getValue();
			}
		}
		
		return n;
	}

	private static Long calculateN(Bucket bucket) {
		Long n = 0l;
		
		for (Entry<String, Long> entry : bucket.getFrequencyByTerm().entrySet()) {
			n += entry.getValue();
		}
		
		return n;
	}
}
