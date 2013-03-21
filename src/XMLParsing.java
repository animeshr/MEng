import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.meng.parsing.xml.OhRefs;
import com.meng.parsing.xml.Opinion;
import com.meng.parsing.xml.Opinions;

public class XMLParsing {

	static HashMap<String, Integer> users = new HashMap<String, Integer>();
	static HashMap<String, Integer> userToID = new HashMap<String, Integer>();
	static HashMap<Integer, String> IDToUser = new HashMap<Integer, String>();
	static HashMap<String, ArrayList<String>> associations = new HashMap<String, ArrayList<String>>();
	static int userIDs = 1;

	XMLParsing() {

	}

	public static void countUsersOccuringMoreThanOnce() {
		int count = 0;
		for (String userID : users.keySet()) {
			if (users.get(userID) > 1) {
				count++;
			}
		}
		System.out.println(count);
	}

	public static void parseOpinion(Opinion opinion) {
		String opinionHolderID = opinion.getOpId();
		if (!users.containsKey(opinionHolderID)) {
			userToID.put(opinionHolderID, userIDs);
			IDToUser.put(userIDs, opinionHolderID);
			userIDs = userIDs + 1;
			users.put(opinionHolderID, 1);			
			ArrayList <String> referencees = new ArrayList<String>();
			associations.put(opinionHolderID, referencees);
			
		} else {
			users.put(opinionHolderID, users.get(opinionHolderID) + 1);
		}
		OhRefs references = opinion.getOhRefs();
		List<String> referencees = references.getOhRef();
		for (String referenceeID : referencees) {
			if (!users.containsKey(referenceeID)) {
				users.put(referenceeID, 1);				
				userToID.put(referenceeID, userIDs);
				IDToUser.put(userIDs, referenceeID);
				userIDs = userIDs + 1;
			
			} else {
				users.put(referenceeID, users.get(referenceeID) + 1);
			}
			
			ArrayList <String> referenceesList = associations.get(opinionHolderID);
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

	public static void readFile() {
		String filename = "";

		filename = "1.xml";
		getOpinions(filename);

		filename = "2.xml";
		getOpinions(filename);
	}

	public static void test() {
		try {
			JAXBContext jc = JAXBContext.newInstance("com.meng.parsing.xml");
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setProperty("jaxb.encoding", "ASCII");
			Opinions ops = (Opinions) unmarshaller.unmarshal(new File(
					"andersonCooper1.xml"));
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

	public static void main(String[] args) {
		// readFile();
		test();
		System.out.println(users.size());
	}
}
