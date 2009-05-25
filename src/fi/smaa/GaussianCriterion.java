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

import java.util.Map;

import fi.smaa.common.Interval;


public class GaussianCriterion extends CardinalCriterion<GaussianMeasurement> {
	
	public GaussianCriterion(String name) {
		super(name);
	}

	public GaussianCriterion(String name, Boolean ascending) {
		super(name, ascending);
	}
	
	@Override
	protected GaussianMeasurement createMeasurement() {
		return new GaussianMeasurement();
	}
	

	/**
	 * Derives max with 95% confidence interval
	 * @param double1
	 * @param double2
	 * @return
	 */
	protected Double deriveMax(Double mean, Double stdev) {
		return mean + (stdev * 1.96);
	}

	/**
	 * Derives with 95% confidence intervals
	 * @param double1
	 * @param double2
	 * @return
	 */
	protected Double deriveMin(Double mean, Double stdev) {
		return mean - (stdev * 1.96);
	}

	@Override
	public String getTypeLabel() {
		return "Gaussian";
	}
	
	@Override
	public Interval createScale(Map<Alternative, GaussianMeasurement> map) {
		if (map.values().size() == 0) {
			return new Interval(0.0, 0.0);
		}
		Double min = Double.MAX_VALUE;
		Double max = Double.MIN_VALUE;
		
		for (GaussianMeasurement m : map.values()) {
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
	public GaussianCriterion deepCopy() {
		GaussianCriterion c = new GaussianCriterion(name);
		deepCopyAscending(c);
		deepCopyAlternativesAndMeasurements(c);
		return c;
	}
}
