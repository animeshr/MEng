package LangModel;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class LanguageModel {
	public HashMap<String, Integer> UnigramFreqs;
	public HashSet<String> stoplist;
	public int totalWords;
	public int rankLimit;

	public static String STOPLIST = "stoplist";

	public LanguageModel(ArrayList<UserData> data) {
		rankLimit = data.size();
		totalWords = 0;
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
	
	public void GetUnigrams(ArrayList<UserData> arr){
		for(UserData x:arr){
			GetUnigrams(x);
		}
	}

	public void GetUnigrams(UserData data) {
		TreeMap<Double, String> userData = data.tweets;
		int factor = rankLimit - data.rank;

		for (Double key : userData.keySet()) {
			String line = userData.get(key);
			StringTokenizer st = new StringTokenizer(line, " ");// " ;':*&^-!(),/$\""
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				token = token.trim();
				token = token.toLowerCase();

				// Stoplist check
				if (stoplist.contains(token))
					continue;

				if (UnigramFreqs.containsKey(token)) {
					UnigramFreqs.put(token, UnigramFreqs.get(token) + (factor));
				} else {
					UnigramFreqs.put(token, new Integer(factor));
				}
				totalWords++;
			}
		}
	}

	public HashMap<String, Integer> GetSelectedWords(int numWords) {
		HashMap<String, Integer> toRet = new HashMap<String, Integer>();
		int i =0;
		while (i < numWords) {
			int freq = 0, maxRank =0;
			for(String key:UnigramFreqs.keySet()){
				Integer wordRank= UnigramFreqs.get(key);
				if (maxRank < wordRank){
					freq = 1;
					maxRank = wordRank;
				} else if (maxRank == wordRank) {
					freq++;
				}
			}
			ArrayList<String> toDel = new ArrayList<String>();
			for(String key:UnigramFreqs.keySet()){
				Integer wordRank= UnigramFreqs.get(key);
				if (maxRank == wordRank){
					freq--; i--;
					toRet.put(key, wordRank);
					toDel.add(key);
					if(freq == 0) break;
					if(i == 0) break;
				}
			}
			for(String delOb:toDel){
				UnigramFreqs.remove(delOb);
			}
			
		}

		return toRet;
	}
	
	/*
	 * This version of accuracy just matches if the 
	 * predicted elements occurs in the top selected
	 * elements from the learn-data.
	 */
	public double GetAccuracy(HashMap<String, Integer> first, HashMap<String, Integer> second){
		double accuracy = 0.0;
		// second one is the predicted one
		for(String key:second.keySet()){
			if(first.containsKey(key)){
				accuracy++;
			}
		}
		accuracy /= first.size();
		return accuracy;
	}
}
