import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import PageRank.BuildMentionsMatrix;
import PageRank.PageRanksSparse;
import Temporal.BuildTemporalMatrix;

import com.meng.parsing.xml.OhRefs;
import com.meng.parsing.xml.Opinion;
import com.meng.parsing.xml.Opinions;

public class XMLParsing {

	HashMap<String, Integer> users;
	HashMap<String, ArrayList<String>> associations;
	HashMap<String, Integer> userToIDFiltered;
	HashMap<Integer, String> IDToUserFiltered;
	int userIDs;
	int countAssociations;
	static boolean TEMPORAL = false;

	HashMap<Integer, HashMap<Integer, ArrayList<Double>>> associationsTemporal;

	XMLParsing(Boolean isTemporal) {
		users = new HashMap<String, Integer>();
		userToIDFiltered = new HashMap<String, Integer>();
		IDToUserFiltered = new HashMap<Integer, String>();
		userIDs = 0;
		countAssociations = 0;
		if (isTemporal) {
			associationsTemporal = new HashMap<Integer, HashMap<Integer, ArrayList<Double>>>();
		} else {
			associations = new HashMap<String, ArrayList<String>>();
		}
	}

	public double getTime(String timestamp) {
		// 2013-02-26T03:04Z
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
		// System.out.println(year + " " + month + " " + day + " " + hour + " "
		// + minutes);
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

	public void addUsersWithoutAnyMentions() {
		for (String userID : users.keySet()) {
			if (!associations.containsKey(userID)) {
				associations.put(userID, new ArrayList<String>());
			}
		}
	}

	public void parseOpinion(Opinion opinion, String opinionHolderID) {
		// String opinionHolderID = opinion.getOpId();
		String timestamp = opinion.getTimestamp();
		if (!users.containsKey(opinionHolderID)) {
			users.put(opinionHolderID, 1);
			userToIDFiltered.put(opinionHolderID, userIDs);
			IDToUserFiltered.put(userIDs, opinionHolderID);
			userIDs = userIDs + 1;
			if (!TEMPORAL) {
				ArrayList<String> referencees = new ArrayList<String>();
				associations.put(opinionHolderID, referencees);
			}

			/*if (!associationsTemporal.containsKey(userToIDFiltered
					.get(opinionHolderID))) {
				HashMap<Integer, ArrayList<Double>> refs = new HashMap<Integer, ArrayList<Double>>();
				associationsTemporal.put(userToIDFiltered.get(opinionHolderID),
						refs);
			}*/
		} else {
			users.put(opinionHolderID, users.get(opinionHolderID) + 1);
		}
		OhRefs references = opinion.getOhRefs();
		List<String> referencees = references.getOhRef();
		for (String referenceeID : referencees) {
			countAssociations++;
			if (!users.containsKey(referenceeID)) {
				users.put(referenceeID, 1);
				userToIDFiltered.put(referenceeID, userIDs);
				IDToUserFiltered.put(userIDs, referenceeID);
				userIDs = userIDs + 1;
				if (!TEMPORAL) {
					associations.put(referenceeID, new ArrayList<String>());
				}
			} else {
				users.put(referenceeID, users.get(referenceeID) + 1);
			}

			if (TEMPORAL) {

				if (!associationsTemporal.containsKey(userToIDFiltered
						.get(referenceeID))) {
					HashMap<Integer, ArrayList<Double>> refs = new HashMap<Integer, ArrayList<Double>>();
					associationsTemporal.put(
							userToIDFiltered.get(referenceeID), refs);
				}

				if (!associationsTemporal.containsKey(userToIDFiltered
						.get(opinionHolderID))) {
					HashMap<Integer, ArrayList<Double>> refs = new HashMap<Integer, ArrayList<Double>>();
					associationsTemporal.put(
							userToIDFiltered.get(opinionHolderID), refs);
				}

				HashMap<Integer, ArrayList<Double>> refs = associationsTemporal
						.get(userToIDFiltered.get(opinionHolderID));

				if (!refs.containsKey(userToIDFiltered.get(referenceeID))) {
					refs.put(userToIDFiltered.get(referenceeID),
							new ArrayList<Double>());
				}

				ArrayList<Double> timestamps = refs.get(userToIDFiltered
						.get(referenceeID));

				timestamps.add(getTime(timestamp));

				refs.put(userToIDFiltered.get(referenceeID), timestamps);
				associationsTemporal.put(userToIDFiltered.get(opinionHolderID),
						refs);
			} else {

				ArrayList<String> referenceesList = associations
						.get(opinionHolderID);
				referenceesList.add(referenceeID);
				associations.put(opinionHolderID, referenceesList);
			}
		}
	}

	public void getOpinions(String filename) {
		try {
			JAXBContext jc = JAXBContext.newInstance("com.meng.parsing.xml");
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			Opinions ops = (Opinions) unmarshaller
					.unmarshal(new File(filename));
			List<Opinion> opn = ops.getOpinion();
			for (int i = 0; i < opn.size(); i++) {
				String o = opn.get(i).getOpholder().getId();
				parseOpinion(opn.get(i), o);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readFiles() {
		int Numfiles = 15;
		for (int i = 1; i <= Numfiles; i++) {
			String filename = "data/bestpic/" + i + ".xml";
			getOpinions(filename);
		}
	}

	public void test() {
		try {
			JAXBContext jc = JAXBContext.newInstance("com.meng.parsing.xml");
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			Opinions ops = (Opinions) unmarshaller.unmarshal(new File(
					"data/test/1" + ".xml"));
			List<Opinion> opn = ops.getOpinion();
			for (int i = 0; i < opn.size(); i++) {
				parseOpinion(opn.get(i), "");
				System.out.println(opn.get(i).getOhRefs() + " : "
						+ opn.get(i).getOpId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void countUsersOccuringMoreThanOnce() {
		int count = 0, max = Integer.MIN_VALUE;
		String newId = null;
		for (String userID : users.keySet()) {
			if (users.get(userID) > 1) {
				count++;
				if (users.get(userID) > max) {
					max = users.get(userID);
					newId = userID;
				}
			}
		}
		System.out.println(count);
		System.out.println("Maximum Occurances of User with ID " + newId
				+ " is " + max);
	}

	int a[] = new int[1000000];

	public void printPaths(Integer node, int index, int visited[]) {
		// System.out.println("Index is " + index + " and node is " + node);
		HashMap<Integer, ArrayList<Double>> rr = associationsTemporal.get(node);
		if (!associationsTemporal.containsKey(node) || visited[node] == 1
				|| rr.size() == 0) {
			if (index == 0) {
				return;
			}
			a[index] = node;
			for (int i = 0; i <= index; i++) {
				System.out.print(a[i] + " -> ");
			}
			System.out.println();
			return;
		}
		// System.out.print("here" );
		a[index] = node;
		visited[node] = 1;

		for (Integer nn : rr.keySet()) {
			printPaths(nn, index + 1, visited);
		}
	}

	public static void main(String[] args) {

		TEMPORAL = true;
		XMLParsing temporal = new XMLParsing(TEMPORAL);
		temporal.readFiles();
		temporal.countUsersOccuringMoreThanOnce();
		System.out.println(temporal.countAssociations);
		System.out.println(temporal.associationsTemporal.size());

		// Printing Paths
		/*int visited[] = new int[1000000];
		for (int i = 0; i < 100000; i++) {
			visited[i] = 0;
		}
		for (Integer ii : temporal.associationsTemporal.keySet()) {
			for (int i = 0; i < 100000; i++) {
				visited[i] = 0;
			}
			temporal.printPaths(ii, 0, visited);
		}*/

		if (!TEMPORAL) {
			BuildMentionsMatrix mentionsmatrix = new BuildMentionsMatrix(
					temporal.associations, temporal.userToIDFiltered,
					temporal.IDToUserFiltered);
			System.out.println("Debug: Size of associations:"
					+ temporal.associations.size());

			PageRanksSparse pr = new PageRanksSparse(mentionsmatrix.mentions);
			pr.CalculateRanks();
			HashMap<String, Integer> ranksOb = pr.GetRanks(
					temporal.userToIDFiltered, temporal.IDToUserFiltered);
			System.out.println("Success!!");
			pr.DisplayRanks(ranksOb);
			pr.DisplayRankStatistics(ranksOb);
		} else {
			BuildTemporalMatrix temporalMatrix = new BuildTemporalMatrix(
					temporal.associationsTemporal, temporal.users.size());
			System.out.println("Debug: Size of associations:"
					+ temporal.associationsTemporal.size());

			PageRanksSparse pr = new PageRanksSparse(
					temporalMatrix.temporalMatrix);
			pr.CalculateRanks();
			HashMap<String, Integer> ranksOb = pr.GetRanks(
					temporal.userToIDFiltered, temporal.IDToUserFiltered);
			System.out.println("Success!!");
			//pr.DisplayRanks(ranksOb);
			pr.DisplayRankStatistics(ranksOb);
		}

	}
}
