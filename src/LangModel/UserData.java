package LangModel;

import java.util.TreeMap;

public class UserData {
	String userID;
	Integer rank;
	TreeMap<Long, String> tweets;

	public UserData(String userID, Integer rank) {
		this.userID = userID;
		this.rank = rank;
		this.tweets = new TreeMap<Long, String>();
	}

	public void AddTweet(Long timestamp, String tweet) {
		tweets.put(timestamp, tweet);
	}
}
