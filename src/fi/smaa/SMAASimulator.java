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
import java.util.List;

import fi.smaa.common.RandomUtil;


public class SMAASimulator {
	
	private SimulationThread simulationThread;
	private SMAAResults results;
	private Integer iterations;
	
	private double[] weights;
	private double[][] measurements;
	private double[] utilities;
	private Integer[] ranks;
	
	private List<Criterion> criteria;
	private List<Alternative> alternatives;
	
	public SMAASimulator(SMAAModel model, Integer iterations) {
		criteria = new ArrayList<Criterion>(model.getCriteria());
		alternatives = new ArrayList<Alternative>(model.getAlternatives());
		this.iterations = iterations;
		results = new SMAAResults(criteria, alternatives, 10);		
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
			try {
				simulationThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			simulationThread = null;
		}		
	}
			
	synchronized public void restart() {
		stop();
		resetValues();
		if (alternatives.size() == 0 || criteria.size() == 0) {
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
		return new SimulationThread(iterations) {
			public void iterate() {
				generateWeights();
				sampleCriteria();
				aggregate();
				rankAlternatives();
				updateHits();
			}			
		};
	}

	private void resetValues() {
		results.reset();
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

	private void updateHits() {
		results.update(ranks, weights);
	}

	private void aggregate() {
		clearUtilities();
		
		for (int critIndex=0;critIndex<criteria.size();critIndex++) {
			Criterion crit = criteria.get(critIndex);
			for (int altIndex=0;altIndex<alternatives.size();altIndex++) {
				double partUtil = computePartialUtility(critIndex, crit, altIndex);
				utilities[altIndex] += weights[critIndex] * partUtil;
			}
		}
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
		for (int i=0;i<criteria.size();i++) {
			criteria.get(i).sample(measurements[i]);
		}
	}

	private void generateWeights() {
		RandomUtil.createSumToOneRand(weights); 
	}
	

	private void init() {
		int numAlts = alternatives.size();
		int numCrit = criteria.size();
		weights = new double[numCrit];
		measurements = new double[numCrit][numAlts];
		utilities = new double[numAlts];
		ranks = new Integer[numAlts];
	}	
}
