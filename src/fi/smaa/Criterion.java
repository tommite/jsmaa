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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jgoodies.binding.beans.Model;

public abstract class Criterion<T extends Measurement> extends Model {
	
	public final static String PROPERTY_NAME = "name";	
	public final static String PROPERTY_TYPELABEL = "typeLabel";
	public final static String PROPERTY_ALTERNATIVES = "alternatives";
	public static final String PROPERTY_MEASUREMENTS = "measurements";
	
	protected String name;
	protected List<Alternative> alternatives = new ArrayList<Alternative>();
	protected Map<Alternative, T> measurements = new HashMap<Alternative, T>();
	private MeasurementListener measurementListener = new MeasurementListener();

	protected Criterion(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public abstract void sample(double[] target);
	
	public void setName(String name) {
		Object oldVal = this.name;
		this.name = name;
		firePropertyChange(PROPERTY_NAME, oldVal, name);		
	}

	public String getName() {
		return name;
	}

	public List<Alternative> getAlternatives() {
		return alternatives;
	}
	
	
	/**
	 * 
	 * @param alternatives
	 * @throws NullPointerException if alternatives == null
	 */
	public void setAlternatives(List<Alternative> alternatives) throws NullPointerException {
		if (alternatives == null) {
			throw new NullPointerException();
		}
		Object oldVal = this.alternatives;
		this.alternatives = alternatives;
		firePropertyChange(PROPERTY_ALTERNATIVES, oldVal, this.alternatives);
		updateMeasurements();
	}
	
	public abstract String getTypeLabel();
	
	public Map<Alternative, T> getMeasurements() {
		return measurements;
	}
	
	protected abstract void fireMeasurementChange();
	
	protected abstract T createMeasurement();

	public String measurementsToString() {
		return getMeasurements().toString();
	}

	
	public boolean deepEquals(Criterion<T> other) {
		if (!name.equals(other.name)) {
			return false;
		}
		if (alternatives.size() != other.alternatives.size()) {
			return false;
		}
		for (int i=0;i<alternatives.size();i++) {
			if (!alternatives.get(i).deepEquals(other.alternatives.get(i))) {
				return false;
			}
		}
		if (!getMeasurements().equals(other.getMeasurements())) {
			return false;
		}
		return true;	
	}
	
	
	public void setMeasurements(Map<Alternative, T> measurements) {
		for (T g : getMeasurements().values()) {
			g.removePropertyChangeListener(PROPERTY_MEASUREMENTS, measurementListener);
		}
		Map<Alternative, T> oldVal = this.measurements;
		this.measurements = measurements;
		
		for (T g : getMeasurements().values()) {
			g.addPropertyChangeListener(measurementListener);
		}
		firePropertyChange(PROPERTY_MEASUREMENTS, oldVal, this.measurements);
		fireMeasurementChange();
	}

	protected void updateMeasurements() {
		Map<Alternative, T> newMap = new HashMap<Alternative, T>();
		for (Alternative a : getAlternatives()) {
			if (getMeasurements().containsKey(a)) {
				newMap.put(a, measurements.get(a));
			} else {
				newMap.put(a, createMeasurement());
			}
		}
		setMeasurements(newMap);
	}

	protected class MeasurementListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			fireMeasurementChange();
		}
	}	
}