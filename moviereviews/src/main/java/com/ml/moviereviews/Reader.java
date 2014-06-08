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
		String line, phrase;
		Long phraseId, sentenceId, oldSentenceId = null;
		int sentiment;

		try {
			reader = new BufferedReader(new InputStreamReader(
					Reader.class.getResourceAsStream(file)));
			
			reader.readLine(); // Ignore first line
			while ((line = reader.readLine()) != null) {
				String[] lineSplitted = line.split("\t");

				phraseId = Long.parseLong(lineSplitted[0]);
				sentenceId = Long.parseLong(lineSplitted[1]);
				
				if (!sentenceId.equals(oldSentenceId)) {
					phrase = lineSplitted[2];
					sentiment = Integer.valueOf(lineSplitted.length > 3 ? lineSplitted[3] : "-1");
					switch (sentiment) {
						case 0: sentiment = 0; break;
						case 1: sentiment = 0; break;
						case 2: sentiment = 1; break;
						case 3: sentiment = 2; break;
						case 4: sentiment = 2; break;
						default: sentiment = -1;
					}
					
					sentences.add(new Sentence(phraseId, sentenceId, phrase, sentiment));
					oldSentenceId = sentenceId;
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
