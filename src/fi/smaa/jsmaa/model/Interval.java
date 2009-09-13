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

import java.text.DecimalFormat;
import java.util.Collection;

import fi.smaa.common.RandomUtil;

public class Interval extends CardinalMeasurement {
	
	private static final long serialVersionUID = -4036986804177522602L;
	public static final String PROPERTY_START = "start";
	public static final String PROPERTY_END = "end";
	
	private Double start;
	private Double end;
	
	public Interval() {
		start = 0.0;
		end = 0.0;
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @throws InvalidIntervalException if end < start
	 */
	public Interval(Double start, Double end) throws InvalidIntervalException {
		if (end < start) {
			throw new InvalidIntervalException();
		}
		this.start = start;
		this.end = end;
	}
	
	public Double getStart() {
		return start;
	}
	
	public Double getEnd() {
		return end;
	}
	
	public void setStart(Double start) {
		Object oldVal = this.start;
		this.start = start;
		firePropertyChange(PROPERTY_START, oldVal, this.start);
	}
	
	public void setEnd(Double end) {
		Object oldVal = this.end;
		this.end = end;
		firePropertyChange(PROPERTY_END, oldVal, this.end);
	}

	@Override
	public String toString() {
		DecimalFormat fmt = new DecimalFormat("#0.00");
		return "[" + fmt.format(start) + " - " + fmt.format(end) + "]";
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (!(other instanceof Interval)) {
			return false;
		}
		Interval io = (Interval) other;
		return getStart().equals(io.getStart()) && getEnd().equals(io.getEnd());
	}
	
	public static Interval enclosingInterval(Collection<Interval> intervals) {
		if (intervals.size() == 0) {
			return null;
		}
		Double min = Double.MAX_VALUE;
		Double max = Double.MIN_VALUE;
		for (Interval i : intervals) {
			if (i.getStart() < min) {
				min = i.getStart();
			}
			if (i.getEnd() > max) {
				max = i.getEnd();
			}
		}
		return new Interval(min, max);
	}
	
	public Double getLength() {
		return end - start;
	}
	
	public boolean includes(Interval other) {
		return other.getStart() >= getStart() && other.getEnd() <= getEnd(); 
	}

	public Interval deepCopy() {
		return new Interval(start, end);
	}

	@Override
	public Interval getRange() {
		return this;
	}

	@Override
	public double sample() {
		return getStart() + RandomUtil.createUnif01() * (getEnd() - getStart());
	}
	
	public boolean includes(Double val) {
		return val >= getStart() && val <= getEnd();
	}

	public Double getMiddle() {
		return (getStart() + getEnd()) / 2.0;
	}

}
