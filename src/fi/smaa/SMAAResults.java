/*
	This file is part of JSMAA.
	(c) Tommi Tervonen, 2009	

    JSMAA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSMAA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSMAA.  If not, see <http://www.gnu.org/licenses/>.
*/

package fi.smaa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class SMAAResults {

	private int[][] rankHits;
	private double[][] centralWeightAdds;
	private Map<Integer, List<Double>> centralWeightVectors;
	private Map<Integer, List<Double>> rankAcceptabilities;
	private static final int FIRSTRANK = 0;
	private List<SMAAResultsListener> listeners = new ArrayList<SMAAResultsListener>();
	private int updateInterval;
	private List<Alternative> alternatives;
	private List<Criterion> criteria;
	private int[] confidenceHits;
	private int confidenceIteration;
	private List<Double> confidenceFactors;
	
	public SMAAResults(List<Alternative> alternatives, List<Criterion> criteria, int updateInterval) {
		this.alternatives = alternatives;
		this.criteria = criteria;
		this.updateInterval = updateInterval;
		initialize();
	}

	public void reset() {
		initialize();
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
		if (getRankAccIteration()  % updateInterval == 0) {
			calculateRankAccsAndCentralWeights();		
			fireResultsChanged();
		}
	}
	
	public void confidenceUpdate(boolean[] hit) {
		assert(hit.length == confidenceHits.length);
		confidenceIteration++;
		for (int i=0;i<hit.length;i++) {
			if (hit[i]) {
				confidenceHits[i]++;
			}
		}
		if (confidenceIteration % updateInterval == 0) {
			calculateConfidenceFactors();					
			fireResultsChanged();
		}
	}
	
	private void calculateConfidenceFactors() {
		for (int i=0;i<confidenceFactors.size();i++) {
			confidenceFactors.set(i, calculateConfidenceFactor(i));
		}
	}

	private Double calculateConfidenceFactor(int altIndex) {
		return (double) confidenceHits[altIndex] / (double) confidenceIteration;
	}

	public void fireResultsChanged() {
		for (SMAAResultsListener listener : listeners) {
			listener.resultsChanged();
		}
	}

	public Integer getRankAccIteration() {
		int num = 1;
		for (int i=0;i<rankHits[0].length;i++) {
			num += rankHits[0][i];
		}
		return new Integer(num);
	}

	public Map<Alternative, List<Double>> getCentralWeightVectors() {
		return transformMap(centralWeightVectors);
	}
	
	public Map<Alternative, Double> getConfidenceFactors() {
		Map<Alternative, Double> confs = new HashMap<Alternative, Double>();
		for (int i=0;i<confidenceFactors.size();i++) {
			confs.put(alternatives.get(i), confidenceFactors.get(i));
		}
		return confs;
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
	
	private Map<Alternative, List<Double>> transformMap(
			Map<Integer, List<Double>> map) {
		Map<Alternative, List<Double>> cw = new HashMap<Alternative, List<Double>>();		
		for (Integer index : map.keySet()) {
			cw.put(alternatives.get(index), map.get(index));
		}
		return cw;
	}
	
	private void initializeCentralWeightVectors() {
		centralWeightVectors = createAlternativeMapWithNans(criteria.size());
	}
	
	private void initializeRankAcceptabilities() {
		rankAcceptabilities = createAlternativeMapWithNans(alternatives.size());
	}
	
	private void initializeConfidenceFactors() {
		confidenceFactors = new ArrayList<Double>();
		for (int i=0;i<alternatives.size();i++) {
			confidenceFactors.add(Double.NaN);
		}
	}

	private HashMap<Integer, List<Double>> createAlternativeMapWithNans(int size) {
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

	private void initialize() {
		int numAlts = alternatives.size();
		int numCrit = criteria.size();
		rankHits = new int[numAlts][numAlts];
		centralWeightAdds = new double[numAlts][numCrit];
		confidenceHits = new int[numAlts];
		confidenceIteration = 0;
		initializeCentralWeightVectors();
		initializeRankAcceptabilities();
		initializeConfidenceFactors();		
	}

	private void calculateRankAccsAndCentralWeights() {
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
			if (rankHits[altIndex][FIRSTRANK] > 0) {
				for (int i=0;i<vec.size();i++) {
					vec.set(i, centralWeightAdds[altIndex][i] / rankHits[altIndex][FIRSTRANK]);
				}
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
