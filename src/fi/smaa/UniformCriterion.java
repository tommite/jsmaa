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


public class UniformCriterion extends CardinalCriterion {
	
	public static final String PROPERTY_INTERVALS = "intervals";

	private Map<Alternative, Interval> intervals = new HashMap<Alternative, Interval>();
	
	public UniformCriterion(String name) {
		super(name);
	}

	public UniformCriterion(String name, Boolean ascending) {
		super(name, ascending);
	}

	@Override
	public void sample(double[] target) {
		assert(target.length == getAlternatives().size());
		
		for (int i=0;i<getAlternatives().size();i++) {
			Alternative a = getAlternatives().get(i);
			double intMin = intervals.get(a).getStart();
			double intMax = intervals.get(a).getEnd();
			double diff = intMax - intMin;
			target[i] = intMin + (RandomUtil.createUnif01() * diff);
		}
	}

	public Map<Alternative, Interval> getIntervals() {
		return intervals;
	}
	
	public void setIntervals(Map<Alternative, Interval> intervals) {
		for (Interval i : this.intervals.values()) {
			i.removePropertyChangeListener(measurementListener);
		}
		Interval oldScale = getScale();
		Object oldVal = this.intervals;
		this.intervals = intervals;
		for (Interval i : intervals.values()) {
			i.addPropertyChangeListener(measurementListener);
		}
		firePropertyChange(PROPERTY_INTERVALS, oldVal, this.intervals);
		firePropertyChange(PROPERTY_SCALE, oldScale, getScale());		
	}
	

	@Override
	public String getTypeLabel() {
		return "Uniform";
	}

	@Override
	public Interval getScale() {
		return Interval.enclosingInterval(intervals.values());
	}

	@Override
	protected void updateMeasurements() {
		Map<Alternative, Interval> newMap = new HashMap<Alternative, Interval>();
		for (Alternative a : getAlternatives()) {
			if (getIntervals().containsKey(a)) {
				newMap.put(a, getIntervals().get(a));
			} else {
				newMap.put(a, new Interval());
			}
		}
		setIntervals(newMap);				
	}	
	
	@Override
	public String measurementsToString() {
		return intervals.toString();
	}
	
	@Override
	public boolean deepEquals(Criterion other) {
		if (other instanceof UniformCriterion){ 
			UniformCriterion uc = (UniformCriterion) other;
			if (intervals.size() != uc.intervals.size()) {
				return false;
			}
			if (!intervals.keySet().containsAll(uc.intervals.keySet())) {
				return false;
			}
			for (Alternative a : intervals.keySet()){ 
				Interval gm = intervals.get(a);
				Interval om = uc.intervals.get(a);
				if (!gm.equals(om)) {
					return false;
				}
			}
		}
		
		return super.deepEquals(other);
	}
}
