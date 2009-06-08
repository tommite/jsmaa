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

package fi.smaa.jsmaa.simulator;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import fi.smaa.jsmaa.SMAAResults;
import fi.smaa.jsmaa.maut.UtilIndexPair;
import fi.smaa.jsmaa.maut.UtilityFunction;
import fi.smaa.jsmaa.maut.UtilitySampler;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.SMAAModel;

@SuppressWarnings("unchecked")
public class SMAASimulator {
	
	private SimulationThread simulationThread;
	private SMAAResults results;
	private Integer iterations;
	
	private boolean[] confidenceHits;
	private double[] weights;
	private double[][] measurements;
	private double[] utilities;
	private Integer[] ranks;
	private UtilitySampler sampler;
	private SMAAModel model;
	
	public SMAASimulator(SMAAModel smaaModel, Integer iterations) {
		model = smaaModel.deepCopy();
		results = new SMAAResults(model.getAlternatives(), model.getCriteria(), 10);
		sampler = new UtilitySampler(model.getAlternatives().size());		
		this.iterations = iterations;
		init();
	}
	
	public Integer getTotalIterations() {
		return iterations;
	}

	public SMAAResults getResults() {
		return results;
	}
	
	synchronized public void stop() {
		if (simulationThread != null) {
			simulationThread.stopSimulation();
			simulationThread = null;
		}		
	}
			
	synchronized public void restart() {
		stop();
		results.reset();
		if (model.getAlternatives().size() == 0 || model.getCriteria().size() == 0) {
			return;
		}
		simulationThread = createSimulationThread();
		simulationThread.start();
	}
	
	public boolean isRunning() {
		if (simulationThread == null) {
			return false;
		}
		return simulationThread.isRunning();
	}	

	private SimulationThread createSimulationThread() {
		SimulationThread th = new SimulationThread();
		th.addPhase(new SimulationPhase() {
			public void iterate() {
				generateWeights();
				sampleCriteria();
				aggregate();
				rankAlternatives();
				results.update(ranks, weights);
			}
		}, iterations);
		th.addPhase(new SimulationPhase() {
			public void iterate() {
				sampleCriteria();
				aggregateWithCentralWeights();
				results.confidenceUpdate(confidenceHits);				
			}	
		}, iterations);
		th.addPhase(new SimulationPhase() {
			public void iterate() {
				results.fireResultsChanged();
			}
		}, 1);
		return th;
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
		Map<Alternative, List<Double>> cws = results.getCentralWeightVectors();

		for (int altIndex=0;altIndex<model.getAlternatives().size();altIndex++) {
			List<Double> cw = cws.get(model.getAlternatives().get(altIndex));
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

	private double computeUtility(int altIndex, List<Double> cw) {
		double utility = 0;
		for (int i=0;i<model.getCriteria().size();i++) {
			double partUtil = computePartialUtility(i, model.getCriteria().get(i), altIndex);
			utility += partUtil * cw.get(i);
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
	

	private void init() {
		int numAlts = model.getAlternatives().size();
		int numCrit = model.getCriteria().size();
		weights = new double[numCrit];
		measurements = new double[numCrit][numAlts];
		utilities = new double[numAlts];
		ranks = new Integer[numAlts];
		confidenceHits = new boolean[numAlts];
	}	
}
