package fi.smaa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SMAAResults {

	private int[][] rankHits;
	private double[][] centralWeightAdds;
	private Map<Integer, List<Double>> centralWeightVectors;
	private Map<Integer, List<Double>> rankAcceptabilities;
	private static final int FIRSTRANK = 0;
	private List<SMAAResultsListener> listeners = new ArrayList<SMAAResultsListener>();
	private List<Criterion> criteria;
	private List<Alternative> alternatives;
	private int updateInterval;
	
	public SMAAResults(List<Criterion> criteria, List<Alternative> alternatives, int updateInterval) {
		this.updateInterval = updateInterval;
		this.criteria = criteria;
		this.alternatives = alternatives;
		initializeCentralWeightVectors();
		initializeRankAcceptabilities();
		initializeArrays();
	}

	public void reset() {
		initializeArrays();
		calculateIndices();
	}
	
	public void addResultsListener(SMAAResultsListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void removeResultsListener(SMAAResultsListener listener) {
		listeners.remove(listener);
	}
	
	
	/**
	 * 
	 * @param ranks an array, where [0] = altIndex of alternative in rank 0
	 * @param weights
	 */
	public void update(Integer[] ranks, double[] weights) {
		assert(ranks.length == rankHits.length);
		assert(weights.length == centralWeightAdds[0].length);

		for (int altIndex=0;altIndex<ranks.length;altIndex++) {
			rankHit(altIndex, ranks[altIndex]);
			if(ranks[altIndex] == FIRSTRANK) {
				addCentralWeight(altIndex, weights);
			}
		}
		if (getIteration()  % updateInterval == 0) {
			fireResultsChanged();
		}
		calculateIndices();
	}
	
	private void fireResultsChanged() {
		for (SMAAResultsListener listener : listeners) {
			listener.resultsChanged();
		}
	}

	public Integer getIteration() {
		int num = 1;
		for (int i=0;i<rankHits[0].length;i++) {
			num += rankHits[0][i];
		}
		return new Integer(num);
	}

	public Map<Alternative, List<Double>> getCentralWeightVectors() {
		return transformMap(centralWeightVectors);
	}

	private HashMap<Alternative, List<Double>> transformMap(
			Map<Integer, List<Double>> map) {
		HashMap<Alternative, List<Double>> cw = new HashMap<Alternative, List<Double>>();		
		for (Integer index : map.keySet()) {
			cw.put(alternatives.get(index), map.get(index));
		}
		return cw;
	}
	
	public Map<Alternative, List<Double>> getRankAcceptabilities() {
		return transformMap(rankAcceptabilities);
	}
	
	public List<Criterion> getCriteria() {
		return criteria;
	}
	
	public List<Alternative> getAlternatives() {
		return alternatives;
	}

	
	private void initializeCentralWeightVectors() {
		centralWeightVectors = createAlternativeMapWithZeroes(criteria.size());
	}
	
	private void initializeRankAcceptabilities() {
		rankAcceptabilities = createAlternativeMapWithZeroes(alternatives.size());
	}

	private HashMap<Integer, List<Double>> createAlternativeMapWithZeroes(int size) {
		Double[] vals = new Double[size];
		Arrays.fill(vals, Double.NaN);
		HashMap<Integer, List<Double>> map = new HashMap<Integer, List<Double>>();		
		for (int index=0;index<alternatives.size();index++) {
			ArrayList<Double> vec = new ArrayList<Double>(Arrays.asList(vals));
			map.put(index, vec);
		}
		return map;
	}
	
	private void addCentralWeight(int altIndex, double[] weights) {
		for (int i=0;i<weights.length;i++) {
			centralWeightAdds[altIndex][i] += weights[i];
		}
	}

	private void initializeArrays() {
		int numAlts = alternatives.size();
		int numCrit = criteria.size();
		rankHits = new int[numAlts][numAlts];
		centralWeightAdds = new double[numAlts][numCrit];
	}

	private void calculateIndices() {
		calculateCentralWeightVectors();
		calculateRankAcceptabilities();
	}

	private void calculateRankAcceptabilities() {
		for (Integer altIndex : rankAcceptabilities.keySet()) {
			List<Double> vec = rankAcceptabilities.get(altIndex);
			for (int i=0;i<vec.size();i++) {
				vec.set(i, calculateRankAcceptability(altIndex, i));
			}
		}
	}

	private double calculateRankAcceptability(Integer altIndex, int rank) {
		int totalIter = 0;
		for (int i=0;i<rankHits[altIndex].length;i++) {
			totalIter += rankHits[altIndex][i];
		}
		return (double) rankHits[altIndex][rank] / (double) totalIter;
	}

	private void calculateCentralWeightVectors() {
		for(Integer altIndex : centralWeightVectors.keySet()) {
			List<Double> vec = centralWeightVectors.get(altIndex);
			for (int i=0;i<vec.size();i++) {
				vec.set(i, centralWeightAdds[altIndex][i] / rankHits[altIndex][FIRSTRANK]);
			}
		}
	}
	
	/**
	 * Ranks start from 0
	 * @param altIndex
	 * @param rank
	 */
	private void rankHit(int altIndex, int rank) {
		rankHits[altIndex][rank]++;
	}

}
