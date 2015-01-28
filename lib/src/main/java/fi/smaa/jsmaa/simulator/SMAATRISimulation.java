/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drugis.common.threading.AbstractIterativeComputation;
import org.drugis.common.threading.IterativeTask;
import org.drugis.common.threading.Task;

import fi.smaa.common.RandomUtil;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAATRIModel;
import fi.smaa.jsmaa.model.electre.ElectreTri;

public class SMAATRISimulation extends SMAASimulation<SMAATRIModel> {
	
	private static final int MAX_SAMPLE_TRIES = 1000;
	private SMAATRIResults results;
	private Map<Alternative, Alternative> sortRes;
	private Map<Alternative, Map<OutrankingCriterion, Double>> criteriaMeasurements;
	private Map<Alternative, Map<OutrankingCriterion, Double>> categoryUpperBounds;
	private double lambda;
	private IterativeTask catAccComputation;

	public SMAATRISimulation(SMAATRIModel triModel, RandomUtil random, int iterations) {
		super(triModel, random);
		results = new SMAATRIResults(model.getAlternatives(), model.getCategories(), REPORTING_INTERVAL);
		
		catAccComputation = new IterativeTask(new AbstractIterativeComputation(iterations) {
			@Override
			public void doStep() {
				if (getModel().getCategories().size() == 0) {
					return;
				}
				generateWeights();
				sampleThresholds();
				sampleMeasurements();
				sampleCategoryUpperBounds();
				sampleLambda();				
				sortAlternatives();
				updateHits();
			}
		}, "CatAcc computation");
		catAccComputation.setReportingInterval(REPORTING_INTERVAL);

	}
	
	public Task getTask() {
		return catAccComputation;
	}
	
	protected void sampleThresholds() throws IterationException {
		for (Criterion c : model.getCriteria()) {
			((OutrankingCriterion)c).sampleThresholds(random);
		}
	}

	private void updateHits() {
		Integer[] cats = new Integer[getModel().getAlternatives().size()];
		for (int i=0;i<cats.length;i++) {
			Alternative alt = getModel().getAlternatives().get(i);
			cats[i] = getModel().getCategories().indexOf(sortRes.get(alt));
		}
		results.update(cats);
	}
	
	protected void sampleMeasurements() {
		super.sampleMeasurements();
		
		criteriaMeasurements = new HashMap<Alternative, Map<OutrankingCriterion, Double>>();
		
		for (int altIndex=0;altIndex<getModel().getAlternatives().size();altIndex++) {
			Alternative a = getModel().getAlternatives().get(altIndex);
			Map<OutrankingCriterion, Double> m = new HashMap<OutrankingCriterion, Double>();
			for (int critIndex=0;critIndex<getModel().getCriteria().size();critIndex++) {
				OutrankingCriterion oc = (OutrankingCriterion) getModel().getCriteria().get(critIndex);
				m.put(oc, getMeasurements(critIndex)[altIndex]);
			}
			criteriaMeasurements.put(a, m);
		}		
	}

	private void sortAlternatives() {
		// construct outranking-criteria-vector
		List<OutrankingCriterion> ocrits = new ArrayList<OutrankingCriterion>();
		for (Criterion c : getModel().getCriteria()) {
			ocrits.add((OutrankingCriterion) c);
		}
		
		ElectreTri tri = new ElectreTri(getModel().getAlternatives(), ocrits,
				getModel().getCategories(), criteriaMeasurements, categoryUpperBounds, weights, lambda, getModel().getRule());
		
		sortRes = tri.compute();
	}

	private void sampleLambda() {
		lambda = getModel().getLambda().sample(random);
	}

	private void sampleCategoryUpperBounds() throws IterationException {
		for (int i=0;i<MAX_SAMPLE_TRIES;i++) {
			categoryUpperBounds = new HashMap<Alternative, Map<OutrankingCriterion, Double>>();

			for (int catIndex=0;catIndex<getModel().getCategories().size()-1;catIndex++) {
				Alternative cat = getModel().getCategories().get(catIndex);
				Map<OutrankingCriterion, Double> m = new HashMap<OutrankingCriterion, Double>();
				for (int critIndex=0;critIndex<getModel().getCriteria().size();critIndex++) {
					OutrankingCriterion oc = (OutrankingCriterion) getModel().getCriteria().get(critIndex);
					m.put(oc, getModel().getCategoryUpperBound(oc, cat).sample(random));
				}
				categoryUpperBounds.put(cat, m);
			}
			if (checkCategoryUpperBounds()) {
				break;
			}
			if (i == MAX_SAMPLE_TRIES-1) {
				throw new IterationException("Cannot sample category upper bounds, dominance not satisfied");				
			}
		}
	}

	private SMAATRIModel getModel() {
		return (SMAATRIModel) model;
	}

	private boolean checkCategoryUpperBounds() {
		for (Criterion c : getModel().getCriteria()) {
			OutrankingCriterion oc = (OutrankingCriterion) c;
			
			Double compareVal = oc.getAscending() ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY; 

			for (int catIndex=0;catIndex<getModel().getCategories().size()-1;catIndex++) {
				Alternative cat = getModel().getCategories().get(catIndex);
				Double bound = categoryUpperBounds.get(cat).get(oc);
				if (oc.getAscending()) {
					if (bound < compareVal) {
						return false;
					}
				} else { // descending
					if (bound > compareVal) {
						return false;
					}					
				}
				compareVal = bound;
			}
		}
		return true;
	}

	@Override
	public SMAATRIResults getResults() {
		return results;
	}

}
