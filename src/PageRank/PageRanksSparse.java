package PageRank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;


public class PageRanksSparse {
	SparseMatrix linkMatrix;
	ArrayList<Double> w;
	int linkMatrixSize;
	final double dampingFactor = 0.85;
	public static final int Iterations = 5;

	public PageRanksSparse(SparseMatrix linksMatrix) {
		linkMatrix = linksMatrix;
		linkMatrixSize = linksMatrix.getSize();
		System.out.println("Size of links matrix-number of rows: "
				+ linkMatrixSize);
	}


	public void NormalizeMatrix(){
		linkMatrix.NormalizeMatrix();
	}
	/*
	 * Creating the matrices for the page rank algorithm:
	 * 
	 * B is the normalized links matrix from NormalizeMatrix(). R is the matrix
	 * with every element equal to 1/n. G is also called Google Matrix
	 */
	public void CreateMatrices() {
		/*
		 * Create B
		 */
		linkMatrix.matrixtimes(dampingFactor);
		linkMatrix.matrixAdd((1 - dampingFactor) / linkMatrix.getSize());
		/*
		 * Create w
		 */
		w = new ArrayList<Double>();
		for (int i = 0; i < linkMatrix.getSize(); i++) {
			w.add(i, 1.0);
		}
	}

	/*
	 * This is to compare the Matrices. The precision used is 10^-6 for the cell
	 * values.
	 */
	public boolean MatrixEquals(ArrayList<Double> A, ArrayList<Double> B) {
		double count = 0, total = 0;
		for (int i = 0; i < A.size(); i++) {
			total++;
			//System.out.println(A.get(i) + " " + B.get(i));
			if (Math.abs(A.get(i) - B.get(i)) > 0.000001) {
				count++;
			}
		}
		if(count/total > 0.1) { System.out.println("disp " + (count/total)); return false; }
		return true;
	}

	/*
	 * Run the algorithm till convergence
	 */
	public void UpdateWTillConvergence() {
		int iterations = 0;
		ArrayList<Double> wi = new ArrayList<Double>(w);
		w = linkMatrix.times(wi);
		while (!MatrixEquals(w, wi) && iterations < Iterations) {
			wi = new ArrayList<Double>(w);
			w = linkMatrix.times(wi);
			iterations++;
			System.out.println("Iteration.." + iterations + "done");
		}
		System.out.println("Debug: Number of iterations run by Page Rank: "
				+ iterations);
		/*for(int i=0;i<w.size(); i++){
			System.out.println(w.get(i));
		}*/
	}

	/*
	 * Getting the ranks for each of the user IDs Assumptions: Both the userToID
	 * and idTouser are made from the same underlying data. hence, if there is
	 * some user present in the first structure same user's information would be
	 * there in the other data structure as well.
	 */
	public HashMap<String, Integer> GetRanks(HashMap<String, Integer> usertoID,
			HashMap<Integer, String> iDtoUser) {
		HashMap<String, Integer> usertoRanks = new HashMap<String, Integer>();
		for (int i = 0; i < usertoID.size(); i++) {
			int rank = 0;
			for (int j = 0; j < usertoID.size(); j++) {
				if (!(i == j) && Double.compare(w.get(i), w.get(j)) < 0) {
					rank++;
				}
			}
			usertoRanks.put(iDtoUser.get(i), rank + 1);
		}

		return usertoRanks;
	}

	public void DisplayRanks(HashMap<String, Integer> UserToRanks) {
		System.out.println("Ranks");
		for (String userID : UserToRanks.keySet()) {
			System.out.println(userID + " : " + UserToRanks.get(userID));
		}
	}

	public void DisplayRankStatistics(HashMap<String, Integer> UserToRanks) {
		System.out.println("Stats about Ranks");
		TreeMap<Integer, Integer> freqs = new TreeMap<Integer, Integer>();
		for(String id:UserToRanks.keySet()){
			Integer rank = UserToRanks.get(id);
			if(freqs.containsKey(rank)){
				Integer old = freqs.get(rank);
				freqs.put(rank, old+1);
			} else {
				freqs.put(rank, new Integer(1));
			}
		}
		for(Integer rank: freqs.keySet()){
			System.out.println("Rank: " + rank + " => " + freqs.get(rank));
		}
	}
	
	/*
	 * Calculates the ranks based on the convergence criteria and populates w.
	 */
	public void CalculateRanks() {
		NormalizeMatrix();
		System.out.println("Number of links found:" + linkMatrix.count());
		CreateMatrices();
		UpdateWTillConvergence();
	}

	public void DisplayLinkMatrix() {
		for (int i = 0; i < linkMatrixSize; i++) {
			for (int j = 0; j < linkMatrixSize; j++) {
				System.out.print(linkMatrix.get(i, j) + " ");
			}
			System.out.println();
		}
	}

}
