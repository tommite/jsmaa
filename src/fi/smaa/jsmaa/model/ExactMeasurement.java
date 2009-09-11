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

public class ExactMeasurement extends CardinalMeasurement {
	
	private static final long serialVersionUID = 1661283322088107801L;

	private Double val;
	
	public static final String PROPERTY_VALUE = "value";
	
	public ExactMeasurement(Double val) {
		this.val = val;
	}
	
	public Double getValue() {
		return val;
	}
	
	public void setValue(Double value) {
		Double oldVal = this.val;
		val = value;
		firePropertyChange(PROPERTY_VALUE, oldVal, val);
	}

	@Override
	public Interval getRange() {
		return new Interval(val, val);
	}

	@Override
	public double sample() {
		return val;
	}

	public ExactMeasurement deepCopy() {
		return new ExactMeasurement(val);
	}
}
