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
