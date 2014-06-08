package com.ml.moviereviews;

import org.junit.Test;

public class WordIdentifierTest {

	@Test
	public void test() {
		String phrase = "Best indie of the year , so far .";
		String[] sentence = phrase.split(" ");

		WordIdentifier wordIdentifier = new WordIdentifier(sentence);
		for (int i = 0; i < sentence.length; i++) {
			System.out.println(sentence[i] + "_" + wordIdentifier.getWordTag(i)
					+ ", " + wordIdentifier.isRelevantWord(i));
		}
		System.out.println("----");
	}

	@Test
	public void test2() {
		String[] sentence = new String[] { "I", "just", "love", "this",
				"movie", "however", "it", "makes", "me", "cry", "a", "lot",
				"the", "dogs", "are", "awesome", "so", "beautiful" };

		WordIdentifier wordIdentifier = new WordIdentifier(sentence);
		for (int i = 0; i < sentence.length; i++) {
			System.out.println(sentence[i] + "_" + wordIdentifier.getWordTag(i)
					+ ", " + wordIdentifier.isRelevantWord(i));
		}
		System.out.println("----");
	}

}
