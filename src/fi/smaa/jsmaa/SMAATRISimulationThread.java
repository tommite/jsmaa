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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.smaa.jsmaa.electre.ElectreTri;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class SMAATRISimulationThread extends SimulationThread {
	
	private SMAATRIResults results;
	private Map<Alternative, Alternative> sortRes;

	public SMAATRISimulationThread(SMAATRIModel triModel, int iterations) {
		super(triModel);
		results = new SMAATRIResults(triModel.getAlternatives(), triModel.getCategories(), 10);
		
		addPhase(new SimulationPhase() {
			public void iterate() {
				if (((SMAATRIModel) model).getCategories().size() == 0) {
					return;
				}
				generateWeights();
				sampleThresholds();
				sampleCriteria();
				sortAlternatives();
				updateHits();
			}
		}, iterations);
	}

	protected void sampleThresholds() {
		for (Criterion c : model.getCriteria()) {
			((OutrankingCriterion)c).sampleThresholds();
		}
	}

	private void updateHits() {
		SMAATRIModel mod = (SMAATRIModel) model;
		Integer[] cats = new Integer[mod.getAlternatives().size()];
		for (int i=0;i<cats.length;i++) {
			Alternative alt = mod.getAlternatives().get(i);
			cats[i] = mod.getCategories().indexOf(sortRes.get(alt));
		}
		results.update(cats);
	}

	private void sortAlternatives() {
		SMAATRIModel mod = (SMAATRIModel) model;
		// construct measurements
		Map<Alternative, Map<OutrankingCriterion, Double>> meas = new HashMap<Alternative, Map<OutrankingCriterion, Double>>();
		
		for (int altIndex=0;altIndex<mod.getAlternatives().size();altIndex++) {
			Alternative a = mod.getAlternatives().get(altIndex);
			Map<OutrankingCriterion, Double> m = new HashMap<OutrankingCriterion, Double>();
			for (int critIndex=0;critIndex<mod.getCriteria().size();critIndex++) {
				OutrankingCriterion oc = (OutrankingCriterion) mod.getCriteria().get(critIndex);
				m.put(oc, getMeasurements(critIndex)[altIndex]);
			}
			meas.put(a, m);
		}
		
		// construct category upper bounds
		// samples category upper bounds!
		Map<Alternative, Map<OutrankingCriterion, Double>> upperBounds = 
			new HashMap<Alternative, Map<OutrankingCriterion, Double>>();

		for (int catIndex=0;catIndex<mod.getCategories().size()-1;catIndex++) {
			Alternative cat = mod.getCategories().get(catIndex);
			Map<OutrankingCriterion, Double> m = new HashMap<OutrankingCriterion, Double>();
			for (int critIndex=0;critIndex<mod.getCriteria().size();critIndex++) {
				OutrankingCriterion oc = (OutrankingCriterion) mod.getCriteria().get(critIndex);
				m.put(oc, mod.getCategoryUpperBound(oc, cat).sample());
			}
			upperBounds.put(cat, m);
		}

		
		// construct outranking-criteria-vector
		List<OutrankingCriterion> ocrits = new ArrayList<OutrankingCriterion>();
		for (Criterion c : mod.getCriteria()) {
			ocrits.add((OutrankingCriterion) c);
		}
		
		double lambda = mod.getLambda().sample();
		ElectreTri tri = new ElectreTri(mod.getAlternatives(), ocrits,
				mod.getCategories(), meas, upperBounds, weights, lambda, mod.getRule());
		
		sortRes = tri.compute();
	}

	@Override
	public SMAATRIResults getResults() {
		return results;
	}

}
