package LangModel;

import java.util.TreeMap;

public class UserData {
	public String userID;
	public Integer rank;
	public TreeMap<Double, String> tweets;

	public UserData(String userID, Integer rank) {
		this.userID = userID;
		this.rank = rank;
		this.tweets = new TreeMap<Double, String>();
	}

	public void AddTweet(Double timestamp, String tweet) {
		tweets.put(timestamp, tweet);
	}
}
