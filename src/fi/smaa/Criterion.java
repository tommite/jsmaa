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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

import com.jgoodies.binding.beans.Model;

public abstract class Criterion<T extends Measurement> extends Model {
	
	public final static String PROPERTY_NAME = "name";	
	public final static String PROPERTY_TYPELABEL = "typeLabel";
	public final static String PROPERTY_ALTERNATIVES = "alternatives";
	public static final String PROPERTY_MEASUREMENTS = "measurements";
	
	protected String name;
	protected List<Alternative> alternatives = new ArrayList<Alternative>();
	protected Map<Alternative, T> measurements = new HashMap<Alternative, T>();
	transient private MeasurementListener measurementListener = new MeasurementListener();
	
	private Semaphore changeSemaphore = new Semaphore(1);
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		measurementListener = new MeasurementListener();
		connectMeasurementListener();
	}

	protected Criterion(String name) {
		this.name = name;
	}
	
	public Semaphore getChangeSemaphore() {
		return changeSemaphore;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
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
		try {
			changeSemaphore.acquire();		
			Object oldVal = this.alternatives;
			this.alternatives = alternatives;
			firePropertyChange(PROPERTY_ALTERNATIVES, oldVal, this.alternatives);		
			updateMeasurements();
			changeSemaphore.release();				
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
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

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof Criterion)) {
			return false;
		}
		Criterion c = (Criterion) other;
		return name.equals(c.name);
	}
	

	public void setMeasurements(Map<Alternative, T> measurements) {
		disconnectMeasurementListener();
		Map<Alternative, T> oldVal = this.measurements;
		this.measurements = measurements;

		connectMeasurementListener();
		firePropertyChange(PROPERTY_MEASUREMENTS, oldVal, this.measurements);
		fireMeasurementChange();
	}

	private void disconnectMeasurementListener() {
		for (T g : getMeasurements().values()) {
			g.removePropertyChangeListener(PROPERTY_MEASUREMENTS, measurementListener);
		}
	}

	private void connectMeasurementListener() {
		for (T g : getMeasurements().values()) {
			g.addPropertyChangeListener(measurementListener);
		}
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

	public abstract Criterion<T> deepCopy();

	@SuppressWarnings("unchecked")
	protected void deepCopyAlternativesAndMeasurements(Criterion target) {
		List<Alternative> list = new ArrayList<Alternative>();
		Map<Alternative, T> meas = new HashMap<Alternative, T>();		
		for (Alternative a : alternatives) {
			Alternative newAlt = a.deepCopy();
			list.add(newAlt);
			meas.put(newAlt, (T) measurements.get(a).deepCopy());
		}
		target.setAlternatives(list);
		target.setMeasurements(meas);
	}
	
	public PropertyChangeListener getMeasurementListener() {
		return measurementListener;
	}
}