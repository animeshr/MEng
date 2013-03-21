package PageRank;

import java.util.HashMap;

import Jama.Matrix;

public class PageRanks {
	double[][] linkMatrix;
	Matrix R, G, B, w;
	int linkMatrixSize;
	final double dampingFactor = 0.85;

	public PageRanks(double[][] linksMatrix) {
		linkMatrix = linksMatrix;
		linkMatrixSize = linksMatrix.length;
		System.out.println("Size of links matrix: " + linkMatrixSize);
	}

	/*
	 * Normalizes the link Matrix
	 */
	public void NormalizeMatrix() {
		for (int i = 0; i < linkMatrixSize; i++) {
			double c = 0.0;
			for (int j = 0; j < linkMatrixSize; j++) {
				c = c + linkMatrix[j][i];
			}
			if (c != 0.0) {
				for (int j = 0; j < linkMatrixSize; j++) {
					linkMatrix[j][i] = linkMatrix[j][i] / c;
				}
			} else {
				for (int j = 0; j < linkMatrixSize; j++) {
					linkMatrix[j][i] = (double)1.0 / linkMatrixSize;
				}
			}
		}
	}

	/*
	 * Creating the matrices for the page rank algorithm:
	 * 
	 * B is the normalized links matrix from NormalizeMatrix(). R is the matrix
	 * with every element equal to 1/n. G is also called Google Matrix
	 */
	public void CreateMatrices() {
		R = new Matrix(linkMatrixSize, linkMatrixSize, 1.0 / linkMatrixSize);
		B = new Matrix(linkMatrix);
		G = B.times(dampingFactor).plus(R.times(1 - dampingFactor));
		w = new Matrix(linkMatrixSize, 1, 1.0);
	}

	/*
	 * This is to compare the Matrices. The precision used is 10^-6 for the cell
	 * values.
	 */
	public boolean MatrixEquals(Matrix A, Matrix B) {
		for (int i = 0; i < A.getRowDimension(); i++) {
			if (Math.abs(A.get(i, 0) - B.get(i, 0)) > 0.001) {
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
		Matrix wi = new Matrix(w.getArrayCopy());
		w = G.times(wi);
		while (!MatrixEquals(w, wi)) {
			wi = w;
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
				if (!(i == j) && Double.compare(w.get(i, 0), w.get(j, 0)) < 0) {
					rank++;
				}
			}
			usertoRanks.put(iDtoUser.get(i), rank + 1);
		}

		return usertoRanks;
	}
	
	public void DisplayRanks(HashMap<String, Integer> UserToRanks){
		System.out.println("Ranks");
		for(String userID: UserToRanks.keySet()){
			System.out.println(userID + " : " + UserToRanks.get(userID));
		}
	}

	/*
	 * Calculates the ranks based on the convergence criteria and populates w.
	 */
	public void CalculateRanks() {
		DisplayLinkMatrix();
		NormalizeMatrix();
		CreateMatrices();
		UpdateWTillConvergence();
	}

	public void DisplayLinkMatrix() {
		for(int i = 0; i < linkMatrixSize; i++) {
			for(int j =0; j < linkMatrixSize; j++) {
				System.out.print(linkMatrix[i][j] + " ");
			}
			System.out.println();
		}
	}
}
