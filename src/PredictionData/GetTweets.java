package PredictionData;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import LangModel.UserData;

import com.meng.parsing.xml.Opinion;
import com.meng.parsing.xml.Opinions;

public class GetTweets {
	public static final String filepath = "data/bestpic/";
	public static final int NumFiles = 15;
	ArrayList<UserData> topUserData;

	public GetTweets(TreeMap<Integer, String> topUsers) {
		topUserData = new ArrayList<UserData>();
		populateTopUserData(topUsers);
	}

	public double getTime(String timestamp) {
		int year = 0, month = 0, day = 0, hour = 0, minutes = 0;
		String[] tokens = timestamp.split("-");
		year = Integer.parseInt(tokens[0]);
		month = Integer.parseInt(tokens[1]);
		String[] subTokens = tokens[2].split("T");
		day = Integer.parseInt(subTokens[0]);
		String[] subsubTokens = subTokens[1].split(":");
		hour = Integer.parseInt(subsubTokens[0]);
		String[] subsubsubTokens = subsubTokens[1].split("Z");
		minutes = Integer.parseInt(subsubsubTokens[0]);
		return timeSinceEpoch(year, month, day, hour, minutes);
	}

	public double timeSinceEpoch(int year, int month, int day, int hour,
			int minutes) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		calendar.clear();
		switch (month) {
		case 1: {
			calendar.set(2011, Calendar.OCTOBER, day);
			break;
		}
		case 2: {
			calendar.set(2011, Calendar.FEBRUARY, day);
			break;
		}
		case 3: {
			calendar.set(2011, Calendar.MARCH, day);
			break;
		}
		case 4: {
			calendar.set(2011, Calendar.APRIL, day);
			break;
		}
		case 5: {
			calendar.set(2011, Calendar.MAY, day);
			break;
		}
		case 6: {
			calendar.set(2011, Calendar.JUNE, day);
			break;
		}
		case 7: {
			calendar.set(2011, Calendar.JULY, day);
			break;
		}
		case 8: {
			calendar.set(2011, Calendar.AUGUST, day);
			break;
		}
		case 9: {
			calendar.set(2011, Calendar.SEPTEMBER, day);
			break;
		}
		case 10: {
			calendar.set(2011, Calendar.OCTOBER, day);
			break;
		}
		case 11: {
			calendar.set(2011, Calendar.NOVEMBER, day);
			break;
		}
		case 12: {
			calendar.set(2011, Calendar.DECEMBER, day);
			break;
		}
		}
		long secondsSinceEpoch = calendar.getTimeInMillis() / 1000L;
		secondsSinceEpoch = secondsSinceEpoch + hour * 60 * 60 + minutes * 60;
		return (double) (secondsSinceEpoch);
	}

	public void getOpinions(String filename,
			TreeMap<String, UserData> topUserMap) {
		try {
			JAXBContext jc = JAXBContext.newInstance("com.meng.parsing.xml");
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			Opinions ops = (Opinions) unmarshaller
					.unmarshal(new File(filename));
			List<Opinion> opn = ops.getOpinion();
			for (int i = 0; i < opn.size(); i++) {
				String o = opn.get(i).getOpholder().getId();
				Opinion opinion = opn.get(i);
				if (topUserMap.containsKey(o)) {
					UserData user = topUserMap.get(o);
					String timestamp = opinion.getTimestamp();
					String tweet = opinion.getSent();
					user.AddTweet(getTime(timestamp), tweet);
					topUserMap.put(o, user);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void populateTopUserData(TreeMap<Integer, String> topUsers) {
		TreeMap<String, UserData> topUserMap = new TreeMap<String, UserData>();
		for (Integer rank : topUsers.keySet()) {
			UserData user = new UserData(topUsers.get(rank), rank);
			topUserMap.put(topUsers.get(rank), user);
		}
		for (int i = 1; i <= NumFiles; i++) {
			String filename = filepath + i + ".xml";
			getOpinions(filename, topUserMap);
		}
		for (String username : topUserMap.keySet()) {
			topUserData.add(topUserMap.get(username));
		}
	}

	public ArrayList<UserData> getTopUserData() {
		return topUserData;
	}
}
