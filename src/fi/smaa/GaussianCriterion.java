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

import java.util.HashMap;
import java.util.Map;

import fi.smaa.common.Interval;
import fi.smaa.common.RandomUtil;


public class GaussianCriterion extends CardinalCriterion {
	
	public static final String PROPERTY_MEANS = "means";
	public static final String PROPERTY_STDEVS = "stDevs";
	public static final String PROPERTY_MEASUREMENTS = "measurements";
	
	private Map<Alternative, GaussianMeasurement> measurements = new HashMap<Alternative, GaussianMeasurement>();
	
	public GaussianCriterion(String name) {
		super(name);
	}

	public GaussianCriterion(String name, Boolean ascending) {
		super(name, ascending);
	}

	@Override
	public void sample(double[] target) {
		assert(target.length == getAlternatives().size());
		
		for (int i = 0 ; i < getAlternatives().size();i++) {
			Alternative a = getAlternatives().get(i);			
			target[i] = RandomUtil.createGaussian(measurements.get(a).getMean(),
					measurements.get(a).getStDev());
		}
	}		

	public Map<Alternative, GaussianMeasurement> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(Map<Alternative, GaussianMeasurement> measurements) {
		for (GaussianMeasurement g : this.measurements.values()) {
			g.removePropertyChangeListener(measurementListener);
		}
		Interval oldScale = getScale();
		Object oldVal = this.measurements;
		this.measurements = measurements;
		
		for (GaussianMeasurement g : this.measurements.values()) {
			g.addPropertyChangeListener(measurementListener);
		}
		firePropertyChange(PROPERTY_MEASUREMENTS, oldVal, this.measurements);
		firePropertyChange(PROPERTY_SCALE, oldScale, getScale());
	}
	
	/**
	 * Derives max with 95% confidence interval
	 * @param double1
	 * @param double2
	 * @return
	 */
	private Double deriveMax(Double mean, Double stdev) {
		return mean + (stdev * 1.96);
	}

	/**
	 * Derives with 95% confidence intervals
	 * @param double1
	 * @param double2
	 * @return
	 */
	private Double deriveMin(Double mean, Double stdev) {
		return mean - (stdev * 1.96);
	}

	@Override
	public String getTypeLabel() {
		return "Gaussian";
	}
	
	@Override
	protected void updateMeasurements() {
		Map<Alternative, GaussianMeasurement> newMap = new HashMap<Alternative, GaussianMeasurement>();
		for (Alternative a : getAlternatives()) {
			if (getMeasurements().containsKey(a)) {
				newMap.put(a, getMeasurements().get(a));
			} else {
				newMap.put(a, new GaussianMeasurement());
			}
		}
		setMeasurements(newMap);
	}
	
	@Override
	public Interval getScale() {
		if (getMeasurements().values().size() == 0) {
			return new Interval(0.0, 0.0);
		}
		Double min = Double.MAX_VALUE;
		Double max = Double.MIN_VALUE;
		
		for (GaussianMeasurement m : getMeasurements().values()) {
			Double tmin = deriveMin(m.getMean(), m.getStDev());
			Double tmax = deriveMax(m.getMean(), m.getStDev());
			if (tmin < min) {
				min = tmin;
			}
			if (tmax > max) {
				max = tmax;
			}
		}
		return new Interval(min, max);
	}
	
	@Override
	public String measurementsToString() {
		return measurements.toString();
	}
}
