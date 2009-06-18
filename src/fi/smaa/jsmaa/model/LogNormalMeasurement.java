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

package fi.smaa.jsmaa.model;

import fi.smaa.jsmaa.common.Interval;

public class LogNormalMeasurement extends GaussianMeasurement {
	
	private static final long serialVersionUID = -562511137486161262L;

	public LogNormalMeasurement(double mean, double stdev) {
		super(mean, stdev);
	}
	
	public LogNormalMeasurement() {
		super();
	}
	
	/**
	 * Derives range of 95% confidence interval.
	 */
	@Override
	public Interval getRange() {
		return new Interval(Math.exp(mean - (stDev * 1.96)),
				Math.exp(mean + (stDev * 1.96)));
	}	

	@Override
	public Object deepCopy() {
		return new LogNormalMeasurement(mean, stDev);
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof LogNormalMeasurement)) {
			return false;
		}
		return super.equals(other);
	}
}
