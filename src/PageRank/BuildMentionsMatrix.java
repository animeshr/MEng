package PageRank;

import java.util.ArrayList;
import java.util.HashMap;

public class BuildMentionsMatrix {
	HashMap<String, Integer> userToID;
	HashMap<Integer, String> IDToUser;
	int SIZE;
	public double mentionsArr[][];
	public SparseMatrix mentions;

	
	public BuildMentionsMatrix(HashMap<String, ArrayList<String>> associations,
			HashMap<String, Integer> userToID, HashMap<Integer, String> IDToUser) {

		this.userToID = userToID;
		this.IDToUser = IDToUser;
		this.SIZE = associations.size();
		//this.mentionsArr = new double[SIZE][SIZE];
		this.mentions = new SparseMatrix(SIZE);
		constructLinkMatrix(associations);
	}

	public void constructLinkMatrix(
			HashMap<String, ArrayList<String>> associations) {

		//initializeLinkMatrix();
		double count=0;
		for (String userID : associations.keySet()) {
			ArrayList<String> mentionedIDs = associations.get(userID);
			for (String mentionedID : mentionedIDs) {
				
				mentions.increment(userToID.get(userID), userToID.get(mentionedID));
				
				//mentionsArr[userToID.get(userID)][userToID.get(mentionedID)]++;
				
				
				//double temp = mentions.get(userToID.get(userID), userToID.get(mentionedID));
				//System.out.println(temp);
				//double outp = mentions.set(userToID.get(userID), userToID.get(mentionedID), temp+1);
				//System.out.println(temp + " " + mentions.get(userToID.get(userID), userToID.get(mentionedID)));
				//System.out.println(count++ + " " + mentions.get(userToID.get(userID), userToID.get(mentionedID)));
			}
		}
		
		
		/*for(int i=0; i<SIZE; i++){
			for(int j=0; j<SIZE; j++){
				count+=mentionsArr[i][j];
			}
		}*/
		//this.mentions = new SparseMatrix(mentionsArr);
		
	}

	/*public void initializeLinkMatrix() {
		for (int i = 0; i < this.SIZE; i++) {
			SparseVector vec = new SparseVector(SIZE);
			mentions.set(i, vec);
		}
	}*/
}
