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

import org.drugis.common.threading.activity.ActivityTask;

import fi.smaa.jsmaa.model.SMAAModel;

public abstract class SMAASimulation<M extends SMAAModel> {

	protected double[][] measurements;
	protected M model;
	protected Sampler sampler;
	protected double[] weights;
	
	public static int REPORTING_INTERVAL = 100;

	public SMAASimulation(M model) {
		this.model = model;
		initialize();
	}
	
	private void initialize() {
		measurements = new double[model.getCriteria().size()][model.getAlternatives().size()];
		sampler = new Sampler(model);		
	}
	
	public abstract SMAAResults getResults();
	
	public void reset() {
		initialize();
	}
	
	protected void sampleCriteria() {
		for (int i=0;i<model.getCriteria().size();i++) {
			sampler.sample(model.getCriteria().get(i), measurements[i]);
		}
	}
	
	protected double[] getMeasurements(int critIndex) {
		assert(critIndex >= 0 && critIndex < measurements.length);
		return measurements[critIndex];
	}

	protected void generateWeights() throws IterationException {
		weights = model.getPreferenceInformation().sampleWeights();
		
		// hack until we can listen only to relevant events
		if (weights.length != model.getCriteria().size()) {
			weights = new double[model.getCriteria().size()];
		}
	}
	
	public abstract ActivityTask getActivityTask();
}
