import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import PageRank.BuildMentionsMatrix;
import PageRank.PageRanksSparse;
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

	public void addUsersWithoutAnyMentions() {
		for (String userID : users.keySet()) {
			if (!associations.containsKey(userID)) {
				associations.put(userID, new ArrayList<String>());
			}
		}
	}

	public void parseOpinion(Opinion opinion) {
		String opinionHolderID = opinion.getOpId();
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

				/* TODO: Convert string timestamp to epoch time */
				timestamps.add(1.0);

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
				parseOpinion(opn.get(i));
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
				parseOpinion(opn.get(i));
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

	public static void main(String[] args) {

		TEMPORAL = false;
		XMLParsing temporal = new XMLParsing(TEMPORAL);
		temporal.readFiles();
		temporal.countUsersOccuringMoreThanOnce();
		System.out.println(temporal.countAssociations);

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
	}
}
