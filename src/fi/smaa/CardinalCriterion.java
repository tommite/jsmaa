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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import fi.smaa.common.Interval;


public abstract class CardinalCriterion extends Criterion {
	
	public final static String PROPERTY_SCALE = "scale";
	public final static String PROPERTY_ASCENDING = "ascending";

	protected Boolean ascending;
	protected MeasurementListener measurementListener = new MeasurementListener();
	
	protected CardinalCriterion() {
		ascending = true;
	}
	

	protected CardinalCriterion(String name) {
		super(name);
		ascending = true;
	}
		
	protected CardinalCriterion(String name, Boolean ascending) {
		super(name);
		this.ascending = ascending;
	}

	public abstract Interval getScale();
	
	public Boolean getAscending() {
		return ascending;
	}
	
	public void setAscending(Boolean asc) {
		Boolean oldVal = this.ascending;
		this.ascending = asc;
		firePropertyChange(PROPERTY_ASCENDING, oldVal, asc);
	}
	
	public String getScaleLabel() {
		return getScale().toString();
	}
	
	protected class MeasurementListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			firePropertyChange(PROPERTY_SCALE, null, getScale());
		}		
	}	
}
