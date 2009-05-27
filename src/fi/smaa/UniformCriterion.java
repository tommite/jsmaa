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


public class UniformCriterion extends CardinalCriterion<Interval> {
	
	public UniformCriterion(String name) {
		super(name);
	}

	public UniformCriterion(String name, Boolean ascending) {
		super(name, ascending);
	}

	@Override
	public String getTypeLabel() {
		return "Uniform";
	}

	@Override
	public Interval getScale() {
		return createScale(measurements);
	}
	
	@Override
	protected Interval createScale(Map<Alternative, Interval> measurements) {
		return Interval.enclosingInterval(measurements.values());		
	}

	@Override
	protected Interval createMeasurement() {
		return new Interval();
	}

	public Object deepCopy() {
		UniformCriterion c = new UniformCriterion(name);
		deepCopyAscending(c);
		deepCopyAlternativesAndMeasurements(c);
		return c;
	}

}
