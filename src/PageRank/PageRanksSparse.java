package PageRank;

import java.util.HashMap;

import net.sf.javaml.clustering.mcl.SparseMatrix;
import net.sf.javaml.clustering.mcl.SparseVector;

public class PageRanksSparse {
	SparseMatrix linkMatrix;
	SparseMatrix G, B;
	SparseVector w;
	int linkMatrixSize;
	final double dampingFactor = 0.85;

	public PageRanksSparse(SparseMatrix linksMatrix) {
		linkMatrix = linksMatrix;
		linkMatrixSize = linksMatrix.size();
		System.out.println("Size of links matrix-number of rows: "
				+ linkMatrixSize);
	}

	SparseMatrix times(SparseMatrix A, double factor) {

		/*
		 * int c = 0; for (int i = 0; i < A.size(); i++) { for (int j = 0; j <
		 * A.size(); j++) { if (A.get(i, j) > 0.0) System.out.print(A.get(i, j)
		 * + " "); c++; } }
		 */
int co = 1;
		System.out.println("Size of A:" + A.size());
		for (int i = 0; i < A.size(); i++) {
			for (int j = 0; j < A.size(); j++) {
				co++;
				if(co ==  A.size()* A.size()) break;
				if (A.get(i, j) > 0.0) {
					
					//System.out.println(A.get(i, j));
					A.set(i, j, (A.get(i, j) * factor));
					//System.out.println(A.get(i, j));
				}
			}
			if(co ==  A.size()* A.size()) break;
		}

		//
		// SparseMatrix toRet = A;
		// for (int i = 0; i < A.size(); i++) {
		// for (int j = 0; j < A.size(); j++) {
		// double old = A.get(i, j);
		// if (old > 0.0) {
		// toRet.set(i, j, (old * factor));
		// System.out.println(old + " => " + toRet.get(i, j));
		// }
		// }
		// }
		return A;
	}

	SparseMatrix plus(SparseMatrix A, double factor) {
		SparseMatrix toRet = A;
		for (int i = 0; i < A.size(); i++) {
			for (int j = 0; j < A.size(); j++) {
				if (Double.compare(A.get(i, j), 0.0) != 0.0) {
					toRet.add(i, j, A.get(i, j) + factor);
				} else {
					toRet.add(i, j, factor);
				}
			}
		}
		return toRet;
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
		B = linkMatrix;
		B = times(B, dampingFactor);
		G = plus(B, (1 - dampingFactor) / linkMatrix.size());
		/*
		 * Create w
		 */
		w = new SparseVector();
		for (int i = 0; i < linkMatrix.size(); i++) {
			w.add(i, 1.0);
		}
	}

	/*
	 * This is to compare the Matrices. The precision used is 10^-6 for the cell
	 * values.
	 */
	public boolean MatrixEquals(SparseVector A, SparseVector B) {
		for (int i = 0; i < A.getLength(); i++) {
			// System.out.println(A.get(i) + " " + B.get(i));
			if (Math.abs(A.get(i) - B.get(i)) > 0.001) {
				return false;
			}
		}
		return true;
	}

	/*
	 * Run the algorithm till convergence
	 */
	public void UpdateWTillConvergence() {
		int iterations = 0;
		SparseVector wi = w.copy();
		w = G.times(wi);
		/*
		 * for (int i = 0; i < w.getLength(); i++) { System.out.println(w.get(i)
		 * + " " + wi.get(i)); }
		 */
		while (!MatrixEquals(w, wi)) {
			wi = w.copy();
			w = G.times(wi);
			iterations++;
		}
		System.out.println("Debug: Number of iterations run by Page Rank: "
				+ iterations);
		// PrintMatrix(w);
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

	/*
	 * Calculates the ranks based on the convergence criteria and populates w.
	 */
	public void CalculateRanks() {

		 //System.out.println("Number of links found:" + CountLinkMatrix());
		// NormalizeMatrix();
		CreateMatrices();
		//UpdateWTillConvergence();
	}

	public void DisplayLinkMatrix() {
		for (int i = 0; i < linkMatrixSize; i++) {
			for (int j = 0; j < linkMatrixSize; j++) {
				System.out.print(linkMatrix.get(i, j) + " ");
			}
			System.out.println();
		}
	}

	public int CountLinkMatrix() {
		int c = 0;
		for (int i = 0; i < linkMatrixSize; i++) {
			for (int j = 0; j < linkMatrixSize; j++) {
				if (linkMatrix.get(i, j) > 0.0)
					System.out.println(linkMatrix.get(i, j) + " @ " + i + " " + j);
				c++;
			}
		}
		return c;
	}
}
