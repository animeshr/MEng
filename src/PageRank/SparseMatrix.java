package PageRank;

import java.util.ArrayList;
import java.util.HashMap;

public class SparseMatrix {
	HashMap<Integer, HashMap<Integer, Double>> matrix;
	int size;
	double normalizedValue;
	
	public SparseMatrix(int size){
		matrix = new HashMap<Integer, HashMap<Integer, Double>>();
		this.size = size;
		normalizedValue = 0.0;
	}
	
	public void NormalizeMatrix() {
		HashMap<Integer, Double> freqs= new HashMap<Integer, Double>();
		for (Integer i = 0; i < size; i++){
			Double sum= 0.0;
			for(Integer k1: matrix.keySet()){
				HashMap<Integer, Double> inner = matrix.get(k1);
				if(inner.containsKey(i)){
					sum += inner.get(i);
				}
			}
			if(sum != 0.0){
				for(Integer k1: matrix.keySet()){
					HashMap<Integer, Double> inner = matrix.get(k1);
					if(inner.containsKey(i)){
						Double newS = inner.get(i);
						inner.put(i, newS/sum);
					}
				}
			}
		}
		
	}
	
	public int getSize() {
		return size;
	}
	public void insert(int x, int y, double value){
		Integer i= new Integer(x), j = new Integer(y);
		if(matrix.containsKey(i)){
			HashMap<Integer, Double> row = matrix.get(i);
			row.put(j, new Double(value));
			matrix.put(i, row);
		} else {
			HashMap<Integer, Double> row = new HashMap<Integer, Double>();
			row.put(j, new Double(value));
			matrix.put(i, row);
		}
	}
	
	public Double get(int x, int y){
		Integer i= new Integer(x), j = new Integer(y);
		if(matrix.containsKey(i)){
			HashMap<Integer, Double> row = matrix.get(i);
			return row.get(j);
		}
		return null;
	}
	
	public void increment(int x, int y){
		Integer i= new Integer(x), j = new Integer(y);
		if(matrix.containsKey(i)){
			HashMap<Integer, Double> row = matrix.get(i);
			if (row.containsKey(j)){
				Double old = row.get(j);
				row.put(j, old+1);
			} else {
				row.put(j, new Double(1.0));
			}
			matrix.put(i, row);
		} else {
			HashMap<Integer, Double> row = new HashMap<Integer, Double>();
			row.put(j, new Double(1.0));
			matrix.put(i, row);
		}
	}
	
	public void matrixAdd(double factor){
		for(Integer rows:matrix.keySet()){
			HashMap<Integer, Double> row = matrix.get(rows);
			for(Integer rowCols:row.keySet()){
				Double old = row.get(rowCols);
				row.put(rowCols, old+factor);
			}
			matrix.put(rows, row);
		}
		normalizedValue += factor;
	}
	
	public void matrixtimes(double factor){
		for(Integer rows:matrix.keySet()){
			HashMap<Integer, Double> row = matrix.get(rows);
			for(Integer rowCols:row.keySet()){
				Double old = row.get(rowCols);
				row.put(rowCols, old*factor);
			}
			matrix.put(rows, row);
		}
		if(normalizedValue > 0.0){
			normalizedValue *= factor;
		} else {
			normalizedValue = factor;
		}
	}
	
	public ArrayList<Double> times(ArrayList<Double> w) {

		ArrayList<Double> w_new = new ArrayList<Double>();
		for (int i = 0; i < w.size(); i++) {
			w_new.add(0.0);
		}
		double entry_sum = 0.0;
		for (Integer references : matrix.keySet()) {
			HashMap<Integer, Double> referencees = matrix.get(references);
			entry_sum=0.0;
			for (int i = 0; i < w.size(); i++) {
				if (referencees.containsKey(i)) {
					double frequency = referencees.get(i);
					entry_sum = entry_sum + (frequency / (double) (w.size())) * w.get(i);
				} else {
					entry_sum = entry_sum + ((double) (1.0)
							/ (double) (w.size())) * w.get(i);
				}
			}
			//System.out.println("Debug: Entry sum = " + entry_sum);
			w_new.set(references, entry_sum);
		}
		double sum_w = 0.0;
		for (int i = 0; i < w.size(); i++) {
			sum_w = sum_w + w.get(i);
		}
		for (int i = 0; i < w.size(); i++) {
			entry_sum=0.0;
			if (!matrix.containsKey(i)) {
				entry_sum = entry_sum + ((double) (1.0) / (double) (w.size()))
						* sum_w;
				w_new.set(i, entry_sum);
			}
		}
		return w_new;
	}
	
	public Double count(){
		Double ret = 0.0;
		for(Integer rows:matrix.keySet()){
			HashMap<Integer, Double> row = matrix.get(rows);
			for(Integer rowCols:row.keySet()){
				ret+= row.get(rowCols);
			}
		}
		return ret;
	}
}
