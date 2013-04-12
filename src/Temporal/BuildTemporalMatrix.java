package Temporal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import PageRank.SparseMatrix;

public class BuildTemporalMatrix {
	int SIZE;
	public SparseMatrix temporalMatrix;
	// delta is set to an hour right now
	public static final Double delta = 60.0 * 60.0;
	
	
	public BuildTemporalMatrix(HashMap<Integer, HashMap<Integer, ArrayList<Double>>> temporalLinks, int size) {
		this.SIZE = size;
		this.temporalMatrix = new SparseMatrix(SIZE);
		createTemporalMatrix(temporalLinks);
		
	}

	public Double GetDelay(ArrayList<Double> lags, Double primaryTime){
		Collections.sort(lags);
		for(int i=0; i < lags.size(); i++){
			if (lags.get(i) > primaryTime){
				return lags.get(i) - primaryTime;
			}
		}
		return null;
	}
	
	public void createTemporalMatrix(HashMap<Integer, HashMap<Integer, ArrayList<Double>>> temporalLinks){
		for(Integer influencee: temporalLinks.keySet()){
			HashMap<Integer, ArrayList<Double>> influencers = temporalLinks.get(influencee);
			for(Integer influencer:influencers.keySet()){
				//temporalMatrix.insert(influencee, influencer, new Double(1.0));
				temporalMatrix.insert(influencer,influencee,influencers.get(influencer).size());
			}
		}
		int linksFound = 0;
		for(Integer influencee: temporalLinks.keySet()){
			Integer currentInfluencee = influencee;
			Double factor = 1.0;
			int currentLevel = 1;
			Queue<Integer> influencees = new LinkedList<Integer>();
			Queue<Integer> influencers = new LinkedList<Integer>();
			HashSet<Integer> visited = new HashSet<Integer>();
			influencees.add(currentInfluencee);
			Double currentdelta = new Double(delta);
			
			for(; currentLevel <= 3; currentLevel++, factor*=0.8){
				while(influencees.size() > 0){
					currentInfluencee = influencees.poll();
					visited.add(currentInfluencee);
					HashMap<Integer, ArrayList<Double>> influencersTotal = temporalLinks.get(currentInfluencee);
					if(influencersTotal!=null){
						//System.out.println(influencersTotal.size());
						for(Integer influencer:influencersTotal.keySet()){
							if (true){ //!visited.contains(influencer)
								influencers.add(influencer);
								ArrayList<Double> timeStamps = influencersTotal.get(influencer);
								for(Double timesofMentionsPrimary: timeStamps){
									// for each time in the mentions labels
									HashMap<Integer, ArrayList<Double>> transitiveInfluencers = temporalLinks.get(influencer);
									if(transitiveInfluencers != null){
										//System.out.println("Size of trans Influencers"+ transitiveInfluencers.size());
										for(Integer transInfluencer:transitiveInfluencers.keySet()){
											ArrayList<Double> transTimeStamps = transitiveInfluencers.get(transInfluencer);
											Double transDelay = GetDelay(transTimeStamps, timesofMentionsPrimary);
											if(transDelay != null){
												//System.out.println("Debug: trans delay = " + transDelay);
												Double val = temporalMatrix.get(influencer, transInfluencer);
												if(val!= null){
													temporalMatrix.insert(transInfluencer,influencer, val + factor/transDelay); // divide by transdelay
												} else {
													temporalMatrix.insert(transInfluencer,influencer, factor/transDelay);
												}
												linksFound++;
											}
										}
									}else {
										//System.out.println("no trans influencers!");
									}
								}
							}
						}
					}
				}
				influencees = influencers;
				influencers = new LinkedList<Integer>();
			}
		}
		System.out.println("Number of links Found: " + linksFound);
	}
}
