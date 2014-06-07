package com.ml.moviereviews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Reader {

	public static List<Sentence> readSentences(String file) {
		List<Sentence> sentences = new ArrayList<Sentence>();
		BufferedReader reader = null;
		String line, previousSentenceId = null;
		String sentenceId, sentence, sentiment;

		try {
			reader = new BufferedReader(new InputStreamReader(
					Reader.class.getResourceAsStream(file)));
			
			reader.readLine(); // Ignore first line
			while ((line = reader.readLine()) != null) {
				String[] lineSplitted = line.split("\t");

				sentenceId = lineSplitted[1];

				if (!sentenceId.equals(previousSentenceId)) {
					sentence = lineSplitted[2];
					sentiment = lineSplitted.length > 2 ? lineSplitted[3] : "0";
					
					sentences.add(new Sentence(sentence, Integer.valueOf(sentiment)));
					
					previousSentenceId = sentenceId;
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		return sentences;
	}
}
