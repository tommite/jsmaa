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

import java.util.Arrays;
import java.util.Map;

import fi.smaa.jsmaa.maut.UtilIndexPair;
import fi.smaa.jsmaa.maut.UtilityFunction;
import fi.smaa.jsmaa.maut.UtilitySampler;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.SMAAModel;

public class SMAA2SimulationThread extends SimulationThread {
	
	private SMAA2Results results;
	private boolean[] confidenceHits;
	private double[] weights;
	private double[][] measurements;
	private double[] utilities;
	private Integer[] ranks;
	private UtilitySampler sampler;
	private SMAAModel model;

	public SMAA2SimulationThread(SMAAModel model, int iterations) {
		this.model = model;
		results = new SMAA2Results(model.getAlternatives(), model.getCriteria(), 10);		
		reset();
		
		addPhase(new SimulationPhase() {
			public void iterate() {
				generateWeights();
				sampleCriteria();
				aggregate();
				rankAlternatives();
				results.update(ranks, weights);
			}
		}, iterations);
		addPhase(new SimulationPhase() {
			public void iterate() {
				sampleCriteria();
				aggregateWithCentralWeights();
				results.confidenceUpdate(confidenceHits);
			}
		}, iterations);
		addPhase(new SimulationPhase() {
			public void iterate() {
				results.fireResultsChanged();
			}
		}, 1);
		
	}
	
	public SMAA2Results getResults() {
		return results;
	}
	
	private void rankAlternatives() {
		UtilIndexPair[] pairs = new UtilIndexPair[utilities.length];
		for (int i=0;i<utilities.length;i++) {
			pairs[i] = new UtilIndexPair(i, utilities[i]);
		}
		Arrays.sort(pairs);
		int rank = 0;
		Double oldUtility = pairs.length > 0 ? pairs[0].util : 0.0;
		for (int i=0;i<pairs.length;i++) {
			if (!oldUtility.equals(pairs[i].util)) {
				rank++;
				oldUtility = pairs[i].util;
			}			
			ranks[pairs[i].altIndex] = rank;
		}
	}

	private void aggregate() {
		clearUtilities();
		
		for (int critIndex=0;critIndex<model.getCriteria().size();critIndex++) {
			Criterion crit = model.getCriteria().get(critIndex);
			for (int altIndex=0;altIndex<model.getAlternatives().size();altIndex++) {
				double partUtil = computePartialUtility(critIndex, crit, altIndex);
				utilities[altIndex] += weights[critIndex] * partUtil;
			}
		}
	}

	private void aggregateWithCentralWeights() {
		clearConfidenceHits();
		Map<Alternative, Map<Criterion, Double>> cws = results.getCentralWeightVectors();

		for (int altIndex=0;altIndex<model.getAlternatives().size();altIndex++) {
			Map<Criterion, Double> cw = cws.get(model.getAlternatives().get(altIndex));
			double utility = computeUtility(altIndex, cw);
			for (int otherAlt=0;otherAlt<model.getAlternatives().size();otherAlt++) {
				if (altIndex == otherAlt) {
					continue;
				}
				double otherUtility = computeUtility(otherAlt, cw);
				if (otherUtility > utility) {
					confidenceHits[altIndex] = false;
					break;
				}
			}
		}
	}

	private void clearConfidenceHits() {
		for (int i=0;i<confidenceHits.length;i++) {
			confidenceHits[i] = true;
		}
	}

	private double computeUtility(int altIndex, Map<Criterion, Double> cw) {
		double utility = 0;
		for (int i=0;i<model.getCriteria().size();i++) {
			double partUtil = computePartialUtility(i, model.getCriteria().get(i), altIndex);
			utility += partUtil * cw.get(model.getCriteria().get(i));
		}
		return utility;
	}

	private double computePartialUtility(int critIndex, Criterion crit, int altIndex) {
		if (crit instanceof CardinalCriterion) {
			return UtilityFunction.utility(((CardinalCriterion)crit), measurements[critIndex][altIndex]);
		} else if (crit instanceof OrdinalCriterion) {
			// ordinal ones are directly as simulated partial utility function values
			return measurements[critIndex][altIndex];
		} else {
			throw new RuntimeException("Unknown criterion type");
		}
	}

	private void clearUtilities() {
		Arrays.fill(utilities, 0.0);
	}

	private void sampleCriteria() {
		for (int i=0;i<model.getCriteria().size();i++) {
			sampler.sample(model.getCriteria().get(i), measurements[i]);
		}
	}

	private void generateWeights() {
		weights = model.getPreferenceInformation().sampleWeights();
	}
	

	public void reset() {
		results.reset();
		sampler = new UtilitySampler(model, model.getAlternatives());		
		int numAlts = model.getAlternatives().size();
		int numCrit = model.getCriteria().size();
		weights = new double[numCrit];
		measurements = new double[numCrit][numAlts];
		utilities = new double[numAlts];
		ranks = new Integer[numAlts];
		confidenceHits = new boolean[numAlts];
	}		

}
