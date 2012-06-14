/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.drugis.common.threading.AbstractIterativeComputation;
import org.drugis.common.threading.IterativeTask;
import org.drugis.common.threading.Task;
import org.drugis.common.threading.activity.ActivityModel;
import org.drugis.common.threading.activity.ActivityTask;
import org.drugis.common.threading.activity.DirectTransition;
import org.drugis.common.threading.activity.Transition;

import fi.smaa.common.RandomUtil;
import fi.smaa.common.ValueRanker;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.ScaleCriterion;
import fi.smaa.jsmaa.model.maut.UtilityFunction;

public class SMAA2Simulation extends SMAASimulation<SMAAModel> {

	private SMAA2Results results;
	private boolean[] confidenceHits;
	private double[] utilities;
	private int[] ranks;

	private IterativeTask rankAccComputation;
	private IterativeTask confFacComputation;
	private ActivityTask activityTask;

	public SMAA2Simulation(SMAAModel amodel, RandomUtil random, int iterations) {
		super(amodel, random);
		results = new SMAA2Results(model.getAlternatives(), model.getCriteria(), REPORTING_INTERVAL);		
		reset();

		rankAccComputation = new IterativeTask(new AbstractIterativeComputation(iterations) {
			@Override
			public void doStep() {
				generateWeights();
				sampleMeasurements();
				aggregate();
				rankAlternatives();
				results.update(ranks, weights);
			}
		}, "RA & CW computation");
		rankAccComputation.setReportingInterval(REPORTING_INTERVAL);

		confFacComputation = new IterativeTask(new AbstractIterativeComputation(iterations) {
			@Override
			public void doStep() {
				sampleMeasurements();
				aggregateWithCentralWeights();
				results.confidenceUpdate(confidenceHits);
			}
		}, "CF computation");
		confFacComputation.setReportingInterval(REPORTING_INTERVAL);

		List<Transition> transitions = new ArrayList<Transition>();
		transitions.add(new DirectTransition(rankAccComputation, confFacComputation));
		activityTask = new ActivityTask(
				new ActivityModel(rankAccComputation, confFacComputation, transitions), 
				"SMAA-2");
	}

	public SMAA2Results getResults() {
		return results;
	}

	private void rankAlternatives() {
		ValueRanker.rankValues(utilities, ranks);
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
		if (crit instanceof ScaleCriterion) {
			return UtilityFunction.utility(((ScaleCriterion)crit), measurements[critIndex][altIndex]);
		} else if (crit instanceof OrdinalCriterion) {
			// ordinal ones are directly as simulated partial utility function values);
			return measurements[critIndex][altIndex];
		} else {
			throw new RuntimeException("Unknown criterion type");
		}
	}

	private void clearUtilities() {
		Arrays.fill(utilities, 0.0);
	}

	public void reset() {
		super.reset();
		results.reset();
		int numAlts = model.getAlternatives().size();
		utilities = new double[numAlts];
		ranks = new int[numAlts];
		confidenceHits = new boolean[numAlts];
	}

	@Override
	public Task getTask() {
		return activityTask;
	}		

}
