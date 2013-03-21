package PageRank;

import java.util.ArrayList;
import java.util.HashMap;

public class BuildMentionsMatrix {
	HashMap<String, Integer> userToID;
	HashMap<Integer, String> IDToUser;
	int SIZE;
	public double mentions[][];

	public BuildMentionsMatrix(HashMap<String, ArrayList<String>> associations,
			HashMap<String, Integer> userToID, HashMap<Integer, String> IDToUser) {

		this.userToID = userToID;
		this.IDToUser = IDToUser;
		this.SIZE = associations.size();

		this.mentions = new double[SIZE][SIZE];
		constructLinkMatrix(associations);
	}

	public void constructLinkMatrix(
			HashMap<String, ArrayList<String>> associations) {

		initializeLinkMatrix();

		for (String userID : associations.keySet()) {
			ArrayList<String> mentionedIDs = associations.get(userID);
			for (String mentionedID : mentionedIDs) {
				mentions[userToID.get(userID)][userToID.get(mentionedID)] = mentions[userToID
						.get(userID)][userToID.get(mentionedID)] + 1.0;
			}
		}

	}

	public void initializeLinkMatrix() {
		for (int i = 0; i < this.SIZE; i++) {
			for (int j = 0; j < this.SIZE; j++) {
				mentions[i][j] = 0.0;
			}
		}
	}
}
