package PageRank;

import java.util.ArrayList;
import java.util.HashMap;

import net.sf.javaml.clustering.mcl.SparseMatrix;

public class BuildMentionsMatrix {
	HashMap<String, Integer> userToID;
	HashMap<Integer, String> IDToUser;
	int SIZE;
	//public double mentions[][];
	public SparseMatrix mentions;

	
	public BuildMentionsMatrix(HashMap<String, ArrayList<String>> associations,
			HashMap<String, Integer> userToID, HashMap<Integer, String> IDToUser) {

		this.userToID = userToID;
		this.IDToUser = IDToUser;
		this.SIZE = associations.size();

		this.mentions = new SparseMatrix(this.SIZE, this.SIZE);
		constructLinkMatrix(associations);
	}

	public void constructLinkMatrix(
			HashMap<String, ArrayList<String>> associations) {

		//initializeLinkMatrix();

		for (String userID : associations.keySet()) {
			ArrayList<String> mentionedIDs = associations.get(userID);
			System.out.println(mentionedIDs.size());
			for (String mentionedID : mentionedIDs) {
				mentions.set(userToID.get(userID), userToID.get(mentionedID), mentions.get(userToID.get(userID), userToID.get(mentionedID))+1);
				//System.out.println(mentions.get(userToID.get(userID), userToID.get(mentionedID)));
			}
		}

	}

	/*public void initializeLinkMatrix() {
		for (int i = 0; i < this.SIZE; i++) {
			for (int j = 0; j < this.SIZE; j++) {
				mentions[i][j] = 0.0;
			}
		}
	}*/
}
