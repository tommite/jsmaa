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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jgoodies.binding.beans.Model;

import fi.smaa.jsmaa.common.DeepCopiable;

public abstract class AbstractCriterion<T extends Measurement> extends Model implements DeepCopiable, Criterion {
	
	private static final long serialVersionUID = -7263844743829489232L;
	protected String name;
	protected List<Alternative> alternatives = new ArrayList<Alternative>();
	protected Map<Alternative, T> measurements = new HashMap<Alternative, T>();
	transient private MeasurementListener measurementListener = new MeasurementListener();
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		measurementListener = new MeasurementListener();
		connectMeasurementListener();
	}

	protected AbstractCriterion(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see fi.smaa.Criterion#setName(java.lang.String)
	 */
	public void setName(String name) {
		Object oldVal = this.name;
		this.name = name;
		firePropertyChange(PROPERTY_NAME, oldVal, name);		
	}

	/* (non-Javadoc)
	 * @see fi.smaa.Criterion#getName()
	 */
	public String getName() {
		return name;
	}

	/* (non-Javadoc)
	 * @see fi.smaa.Criterion#getAlternatives()
	 */
	public List<Alternative> getAlternatives() {
		return alternatives;
	}
	
	
	/* (non-Javadoc)
	 * @see fi.smaa.Criterion#setAlternatives(java.util.List)
	 */
	public void setAlternatives(List<Alternative> alternatives) throws NullPointerException {
		if (alternatives == null) {
			throw new NullPointerException();
		}		
		Object oldVal = this.alternatives;
		this.alternatives = alternatives;
		Object oldMeasurements = this.measurements;
		disconnectMeasurementListener();
		measurements = prepareNewMeasurements();
		connectMeasurementListener();
		firePropertyChange(PROPERTY_ALTERNATIVES, oldVal, this.alternatives);
		firePropertyChange(PROPERTY_MEASUREMENTS, oldMeasurements, this.measurements);
	}
	
	/* (non-Javadoc)
	 * @see fi.smaa.Criterion#getTypeLabel()
	 */
	public abstract String getTypeLabel();
	
	/* (non-Javadoc)
	 * @see fi.smaa.Criterion#getMeasurements()
	 */
	public Map<Alternative, T> getMeasurements() {
		return new HashMap<Alternative, T>(measurements);
	}
	
	public void setMeasurements(Map<Alternative, T> measurements) {
		disconnectMeasurementListener();
		Map<Alternative, T> oldVal = this.measurements;
		this.measurements = measurements;

		connectMeasurementListener();
		firePropertyChange(PROPERTY_MEASUREMENTS, oldVal, this.measurements);
		fireMeasurementChange();
	}	
	
	protected abstract void fireMeasurementChange();
	
	protected abstract T createMeasurement();

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof AbstractCriterion)) {
			return false;
		}
		AbstractCriterion c = (AbstractCriterion) other;
		return name.equals(c.name);
	}
	
	private void disconnectMeasurementListener() {
		for (T g : measurements.values()) {
			g.removePropertyChangeListener(PROPERTY_MEASUREMENTS, measurementListener);
		}
	}

	private void connectMeasurementListener() {
		for (T g : measurements.values()) {
			g.addPropertyChangeListener(measurementListener);
		}
	}

	protected Map<Alternative, T> prepareNewMeasurements() {
		Map<Alternative, T> newMap = new HashMap<Alternative, T>();
		for (Alternative a : getAlternatives()) {
			if (measurements.containsKey(a)) {
				newMap.put(a, measurements.get(a));
			} else {
				newMap.put(a, createMeasurement());
			}
		}
		return newMap;
	}

	protected class MeasurementListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			fireMeasurementChange();
		}
	}	

	@SuppressWarnings("unchecked")
	protected void deepCopyAlternativesAndMeasurements(AbstractCriterion target) {
		List<Alternative> list = new ArrayList<Alternative>();
		Map<Alternative, T> meas = new HashMap<Alternative, T>();		
		for (Alternative a : alternatives) {
			Alternative newAlt = a.deepCopy();
			list.add(newAlt);
			T m = measurements.get(a);
			if (m == null) {
				m = createMeasurement();
			}
			meas.put(newAlt, (T) m.deepCopy());
		}
		target.setAlternatives(list);
		target.setMeasurements(meas);
	}
		
	public PropertyChangeListener getMeasurementListener() {
		return measurementListener;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}