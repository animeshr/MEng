package LangModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class LanguageModel {
	public int MODE = 0;
	public HashMap<String, Integer> UnigramFreqs;
	public HashSet<String> stoplist;
	public int totalWords;
	public int rankLimit;
	public String tokenParams = " ;':*&^-!(),/$\".?";

	public static String STOPLIST = "stoplist";

	public LanguageModel(ArrayList<UserData> data) {
		rankLimit = data.size();
		totalWords = 0;
		UnigramFreqs = new HashMap<String, Integer>();
		stoplist = new HashSet<String>();
		GetStopWords();
		MODE = 1;
		GetUnigrams(data);
		System.out.println("Language Model Created");
	}

	public LanguageModel(HashMap<String, ArrayList<String>> tweets) {
		rankLimit = tweets.size();
		totalWords = 0;
		UnigramFreqs = new HashMap<String, Integer>();
		stoplist = new HashSet<String>();
		GetStopWords();
		MODE = 2;
		GetUnigrams(tweets);
	}

	public LanguageModel(ArrayList<String> tweets, boolean validation) {
		rankLimit = tweets.size();
		totalWords = 0;
		UnigramFreqs = new HashMap<String, Integer>();
		stoplist = new HashSet<String>();
		GetStopWords();
		MODE = 2;
		GetUnigrams(tweets, true);
	}

	public void GetStopWords() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(STOPLIST));
		} catch (FileNotFoundException e) {
			System.out.println("Error in reading Stoplist file");
			return;
		}
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				line = line.trim();
				stoplist.add(line);
			}
		} catch (IOException e) {
			System.out.println("Error: In reading File " + STOPLIST);
		}
		try {
			br.close();
		} catch (IOException e) {
			System.out.println("Error: In closing file" + STOPLIST);
		}
	}

	public void GetUnigrams(HashMap<String, ArrayList<String>> arr) {
		Object[] entries = arr.keySet().toArray();
		Random generator = new Random();
		int count = 0;
		while (count < PageRank.PageRanksSparse.NumUsersToCompare
				|| UnigramFreqs.size() < PageRank.PageRanksSparse.NumWordsToCompare) {
			String randomValue = (String) entries[generator
					.nextInt(entries.length)];
			ArrayList<String> obj = arr.get(randomValue);
			GetUnigrams(obj, true);
			System.out.print("."); count++;
		}
	}

	public void GetUnigrams(ArrayList<UserData> arr) {
		for (UserData x : arr) {
			GetUnigrams(x);
			System.out.print(".");
		}
	}

	public int GetFactor(int dataRank) {
		if (MODE == 1) {
			return rankLimit - dataRank;
		} else if (MODE == 2) {
			return 1;
		} else {
			return (1+(int) (Math.log(rankLimit - dataRank)));
		}
	}

	public void GetUnigrams(ArrayList<String> arr, boolean validation) {
		for (String x : arr) {
			GetUnigrams(x);
		}
	}

	public void GetUnigrams(String data) {
		String line = data;
		int factor = GetFactor(0);
		StringTokenizer st = new StringTokenizer(line, tokenParams);// " ;':*&^-!(),/$\"."
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			token = token.trim();
			token = token.toLowerCase();

			// Stoplist check
			if (stoplist.contains(token) || token.length() <= 2
					|| Filter(token))
				continue;
			if (UnigramFreqs.containsKey(token)) {
				UnigramFreqs.put(token, UnigramFreqs.get(token) + (factor));
			} else {
				UnigramFreqs.put(token, new Integer(factor));
			}
			totalWords++;
		}
	}

	public boolean Filter(String word) {
		for (char i = '0'; i <= '9'; i++) {
			if (word.contains("" + i))
				return true;
		}
		if (word.contains("#") || word.contains("@"))
			return true;
		return false;
	}

	public void GetUnigrams(UserData data) {
		TreeMap<Double, String> userData = data.tweets;
		int factor = GetFactor(data.rank);

		for (Double key : userData.keySet()) {
			String line = userData.get(key);
			System.out.println(line);
			StringTokenizer st = new StringTokenizer(line, tokenParams);// " ;':*&^-!(),/$\"."
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				token = token.trim();
				token = token.toLowerCase();

				// Stoplist check
				if (stoplist.contains(token) || token.length() <= 2
						|| Filter(token))
					continue;
				// System.out.println(data.userID + " " + token + " ");

				if (UnigramFreqs.containsKey(token)) {
					UnigramFreqs.put(token, UnigramFreqs.get(token) + (factor));
				} else {
					UnigramFreqs.put(token, new Integer(factor));
				}
				totalWords++;
			}
		}
	}

	public HashMap<String, Integer> GetRandomWords(int numWords) {
		HashMap<String, Integer> toRet = new HashMap<String, Integer>();
		Random generator = new Random();
		Object[] entries = UnigramFreqs.keySet().toArray();
		System.out.println("Inside Random");
		while (toRet.size() != (numWords - 1)) {
			String randomValue = (String) entries[generator
					.nextInt(entries.length)];
			Integer val = UnigramFreqs.get(randomValue);
			toRet.put(randomValue, val);
			System.out.print("-");
		}
		return toRet;
	}

	public HashMap<String, Integer> GetSelectedWords(int numWords) {
		HashMap<String, Integer> toRet = new HashMap<String, Integer>();
		int i = 0;
		while (i < numWords) {
			int freq = 0, maxRank = 0;
			for (String key : UnigramFreqs.keySet()) {
				Integer wordRank = UnigramFreqs.get(key);
				if (maxRank < wordRank) {
					freq = 1;
					maxRank = wordRank;
				} else if (maxRank == wordRank) {
					freq++;
				}
			}
			ArrayList<String> toDel = new ArrayList<String>();
			for (String key : UnigramFreqs.keySet()) {
				Integer wordRank = UnigramFreqs.get(key);
				if (maxRank == wordRank) {
					freq--;
					i++;
					System.out.print(i + " ");
					toRet.put(key, wordRank);
					toDel.add(key);
					if (freq == 0)
						break;
					if (i == numWords)
						break;
				}
			}
			for (String delOb : toDel) {
				UnigramFreqs.remove(delOb);
			}

		}

		return toRet;
	}

	/*
	 * This version of accuracy just matches if the predicted elements occurs in
	 * the top selected elements from the learn-data.
	 */
	public double GetAccuracy(HashMap<String, Integer> first,
			HashMap<String, Integer> second) {
		double accuracy = 0.0;
		// second one is the predicted one
		for (String key : second.keySet()) {
			if (first.containsKey(key)) {
				accuracy++;
			}
		}
		accuracy /= first.size();
		return accuracy;
	}
}
