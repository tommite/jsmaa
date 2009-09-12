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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardinalPreferenceInformation extends PreferenceInformation {
	
	private static final long serialVersionUID = 5119910625472241337L;
	private List<Criterion> criteria;
	private Map<Criterion, CardinalMeasurement> prefs = new HashMap<Criterion, CardinalMeasurement>();
	private transient MeasurementListener measListener = new MeasurementListener();

	public CardinalPreferenceInformation(List<Criterion> criteria) {
		this.criteria = criteria;
		initMeasurements();
	}
	
	private void readObject(ObjectInputStream i) throws IOException, ClassNotFoundException {
		i.defaultReadObject();
		measListener = new MeasurementListener();
		for (Criterion c : criteria) {
			prefs.get(c).addPropertyChangeListener(measListener);
		}
	}	
	
	private void initMeasurements() {
		for (Criterion c : criteria) {
			setMeasurement(c, new ExactMeasurement(0.0));
		}
	}

	public List<Criterion> getCriteria() {
		return criteria;
	}
	
	public void setMeasurement(Criterion c, CardinalMeasurement m) {
		CardinalMeasurement oldMeas = prefs.get(c);
		if (oldMeas != null) {
			oldMeas.removePropertyChangeListener(measListener);
		}
		m.addPropertyChangeListener(measListener);
		prefs.put(c, m);
		firePreferencesChanged();
	}
	
	public CardinalMeasurement getMeasurement(Criterion c) {
		return prefs.get(c);
	}

	public double[] sampleWeights() {
		double[] weights = new double[criteria.size()];
		for (int i=0;i<weights.length;i++) {
			weights[i] = prefs.get(criteria.get(i)).sample();
		}
		return weights;
	}

	public CardinalPreferenceInformation deepCopy() {
		CardinalPreferenceInformation pref = new CardinalPreferenceInformation(criteria);
		for (Criterion c : prefs.keySet()) {
			pref.setMeasurement(c, prefs.get(c));
		}
		return pref;
	}
	
	private class MeasurementListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			firePreferencesChanged();
		}		
	}
}
