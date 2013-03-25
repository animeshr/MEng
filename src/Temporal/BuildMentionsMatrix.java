package Temporal;

import java.util.ArrayList;
import java.util.HashMap;

import PageRank.SparseMatrix;

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
		this.mentions = new SparseMatrix(SIZE);
		constructLinkMatrix(associations);
	}

	public void constructLinkMatrix(
			HashMap<String, ArrayList<String>> associations) {		
	}
}
