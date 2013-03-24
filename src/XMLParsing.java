import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import PageRank.BuildMentionsMatrix;
import PageRank.PageRanks;
import PageRank.PageRanksSparse;

import com.meng.parsing.xml.OhRefs;
import com.meng.parsing.xml.Opinion;
import com.meng.parsing.xml.Opinions;

public class XMLParsing {

	static HashMap<String, Integer> users = new HashMap<String, Integer>();
	// static HashMap<String, Integer> userToID = new HashMap<String,
	// Integer>();
	// static HashMap<Integer, String> IDToUser = new HashMap<Integer,
	// String>();
	static HashMap<String, ArrayList<String>> associations = new HashMap<String, ArrayList<String>>();

	static HashMap<String, Integer> userToIDFiltered = new HashMap<String, Integer>();
	static HashMap<Integer, String> IDToUserFiltered = new HashMap<Integer, String>();
	static HashMap<String, ArrayList<String>> associationsFiltered = new HashMap<String, ArrayList<String>>();
	static int userIDs = 0;
	static int countAssociations = 0;

	XMLParsing() {

	}

	public static void filter() {
		HashSet<String> usersFiltered = new HashSet<String>();
		for (String userID : users.keySet()) {
			if (/* users.get(userID) > 1 */true) {
				usersFiltered.add(userID);
				userToIDFiltered.put(userID, userIDs);
				IDToUserFiltered.put(userIDs, userID);
				userIDs = userIDs + 1;
				associationsFiltered.put(userID, new ArrayList<String>());
			}
		}

		for (String userID : usersFiltered) {
			if (associations.containsKey(userID)) {
				ArrayList<String> referencees = associations.get(userID);
				for (String referenceesID : referencees) {
					if (usersFiltered.contains(referenceesID)) {
						ArrayList<String> r = associationsFiltered
								.get(userID);
						r.add(referenceesID);
						associationsFiltered.put(userID, r);
						countAssociations++;
					}

				}
			}
		}
	}

	public static void parseOpinion(Opinion opinion) {
		String opinionHolderID = opinion.getOpId();
		if (!users.containsKey(opinionHolderID)) {
			users.put(opinionHolderID, 1);
			ArrayList<String> referencees = new ArrayList<String>();
			associations.put(opinionHolderID, referencees);

		} else {
			users.put(opinionHolderID, users.get(opinionHolderID) + 1);
		}
		OhRefs references = opinion.getOhRefs();
		List<String> referencees = references.getOhRef();
		for (String referenceeID : referencees) {
			if (!users.containsKey(referenceeID)) {
				users.put(referenceeID, 1);
			} else {
				users.put(referenceeID, users.get(referenceeID) + 1);
			}

			ArrayList<String> referenceesList = associations
					.get(opinionHolderID);
			referenceesList.add(referenceeID);
			associations.put(opinionHolderID, referenceesList);
		}
	}

	public static void getOpinions(String filename) {
		try {
			JAXBContext jc = JAXBContext.newInstance("com.meng.parsing.xml");
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			Opinions ops = (Opinions) unmarshaller
					.unmarshal(new File(filename));
			List<Opinion> opn = ops.getOpinion();
			for (int i = 0; i < opn.size(); i++) {
				parseOpinion(opn.get(i));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readFiles() {
		int Numfiles = 63;
		for (int i = 1; i <= Numfiles; i++) {
			String filename = "data/googleAndroid/" + i + ".xml";
			getOpinions(filename);
		}
	}

	public static void test() {
		try {
			JAXBContext jc = JAXBContext.newInstance("com.meng.parsing.xml");
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			Opinions ops = (Opinions) unmarshaller.unmarshal(new File(
					"data/test/1" + ".xml"));
			List<Opinion> opn = ops.getOpinion();
			for (int i = 0; i < opn.size(); i++) {
				parseOpinion(opn.get(i));
				System.out.println(opn.get(i).getOhRefs() + " : "
						+ opn.get(i).getOpId());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void countUsersOccuringMoreThanOnce() {
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

	public static void main(String[] args) {
		readFiles();
		System.out.println(users.size());
		countUsersOccuringMoreThanOnce();
		filter();
		System.out.println(countAssociations);
		BuildMentionsMatrix mentionsmatrix = new BuildMentionsMatrix(
				associationsFiltered, userToIDFiltered, IDToUserFiltered);
		System.out.println("Debug: Size of associations:"
				+ associationsFiltered.size());
		
		
		
		
		PageRanksSparse pr = new PageRanksSparse(mentionsmatrix.mentions);
		pr.CalculateRanks();
		HashMap<String, Integer> ranksOb = pr.GetRanks(userToIDFiltered,
				IDToUserFiltered);
		System.out.println("Success!!");
		pr.DisplayRanks(ranksOb);
		pr.DisplayRankStatistics(ranksOb);
	}
}
