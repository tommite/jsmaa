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

package fi.smaa.jsmaa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;

public class SMAA2Results extends SMAAResults {

	private double[][] centralWeightAdds;
	private Map<Integer, List<Double>> centralWeightVectors;
	private static final int FIRSTRANK = 0;
	private List<Criterion> criteria;
	private int[] confidenceHits;
	private int confidenceIteration;
	private List<Double> confidenceFactors;
	private Acceptabilities rankAcceptabilities;
	
	public SMAA2Results(List<Alternative> alternatives, List<Criterion> criteria, int updateInterval) {
		super(alternatives, updateInterval);
		this.criteria = criteria;
		reset();
	}
	
	/**
	 * 
	 * @param ranks an array, where [0] = altIndex of alternative in rank 0
	 * @param weights
	 */
	public void update(Integer[] ranks, double[] weights) {
		assert(ranks.length == rankAcceptabilities.getLength());
		assert(weights.length == centralWeightAdds[0].length);

		for (int altIndex=0;altIndex<ranks.length;altIndex++) {
			rankAcceptabilities.hit(altIndex, ranks[altIndex]);
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

	public Integer getRankAccIteration() {
		return new Integer(rankAcceptabilities.getTotalHits(0));
	}

	public Map<Alternative, Map<Criterion, Double>> getCentralWeightVectors() {
		
		Map<Alternative, Map<Criterion, Double>> cw 
			= new HashMap<Alternative, Map<Criterion, Double>>();		
		for (Integer index : centralWeightVectors.keySet()) {
			Map<Criterion, Double> vector = new HashMap<Criterion, Double>();
			for (int i=0;i<criteria.size();i++) {
				vector.put(criteria.get(i), centralWeightVectors.get(index).get(i));
			}
			cw.put(alternatives.get(index), vector);
		}
		return cw;
	}
	
	public Map<Alternative, Double> getConfidenceFactors() {
		Map<Alternative, Double> confs = new HashMap<Alternative, Double>();
		for (int i=0;i<confidenceFactors.size();i++) {
			confs.put(alternatives.get(i), confidenceFactors.get(i));
		}
		return confs;
	}

	public Map<Alternative, List<Double>> getRankAcceptabilities() {
		return rankAcceptabilities.getResults();
	}
	
	public List<Criterion> getCriteria() {
		return criteria;
	}
	
	private void initializeCentralWeightVectors() {
		centralWeightVectors = createAlternativeMapWithNans(criteria.size());
	}
	
	private void initializeRankAcceptabilities() {
		rankAcceptabilities = new Acceptabilities(alternatives, alternatives.size());
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

	public void reset() {
		int numAlts = alternatives.size();
		int numCrit = criteria.size();
		centralWeightAdds = new double[numAlts][numCrit];
		confidenceHits = new int[numAlts];
		confidenceIteration = 0;
		initializeCentralWeightVectors();
		initializeRankAcceptabilities();
		initializeConfidenceFactors();		
	}

	private void calculateRankAccsAndCentralWeights() {
		calculateCentralWeightVectors();
	}


	private void calculateCentralWeightVectors() {
		for(Integer altIndex : centralWeightVectors.keySet()) {
			List<Double> vec = centralWeightVectors.get(altIndex);
			if (rankAcceptabilities.getHits(altIndex,FIRSTRANK) > 0) {
				for (int i=0;i<vec.size();i++) {
					vec.set(i, centralWeightAdds[altIndex][i] / 
							rankAcceptabilities.getHits(altIndex, FIRSTRANK));
				}
			}
		}
	}
}
