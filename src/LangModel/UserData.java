package LangModel;

import java.util.ArrayList;

public class UserData {
	String userID;
	Integer rank;
	ArrayList<String> tweets;
	
	public UserData(String userID, Integer rank) {
		this.userID = userID;
		this.rank = rank;
		this.tweets = new ArrayList<String>();
	}
	
	public void AddTweet(String tweet){
		tweets.add(tweet);
	}
}
