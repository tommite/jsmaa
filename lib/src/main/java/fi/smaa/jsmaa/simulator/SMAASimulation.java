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

import org.drugis.common.threading.Task;

import fi.smaa.common.RandomUtil;
import fi.smaa.jsmaa.model.SMAAModel;

public abstract class SMAASimulation<M extends SMAAModel> {
	protected RandomUtil random;
	protected double[][] measurements;
	protected M model;
	protected double[] weights;
	
	public static int REPORTING_INTERVAL = 100;

	public SMAASimulation(M model, RandomUtil random) {
		this.model = model;
		this.random = random;
		initialize();
	}
	
	private void initialize() {
		measurements = new double[model.getCriteria().size()][model.getAlternatives().size()];
	}
	
	public abstract SMAAResults getResults();
	
	public void reset() {
		initialize();
	}
	
	protected void sampleMeasurements() {
		this.model.getMeasurements().sample(random, measurements);
	}

	protected double[] getMeasurements(int critIndex) {
		assert(critIndex >= 0 && critIndex < measurements.length);
		return measurements[critIndex];
	}

	protected void generateWeights() throws IterationException {
		weights = model.getPreferenceInformation().sampleWeights(random);
		
		// hack until we can listen only to relevant events
		if (weights.length != model.getCriteria().size()) {
			weights = new double[model.getCriteria().size()];
		}
	}
	
	public abstract Task getTask();
}
