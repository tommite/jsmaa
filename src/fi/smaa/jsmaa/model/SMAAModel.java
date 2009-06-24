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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.jgoodies.binding.beans.Model;


public class SMAAModel extends Model {
	public final static String PROPERTY_NAME = "name";
	
	private String name;
	private PreferenceInformation preferences;
	private ImpactMatrix impactMatrix;
	
	private static final long serialVersionUID = 6100076809211865658L;
	
	private List<Alternative> alternatives = new ArrayList<Alternative>();
	private List<Criterion> criteria = new ArrayList<Criterion>();
	
	transient private List<SMAAModelListener> modelListeners = new ArrayList<SMAAModelListener>();
	transient protected ImpactMatrixListener impactListener = new ImpactListener();
	transient private CriteriaListener critListener = new CriteriaListener();
	
	public SMAAModel(String name) {
		this.name = name;
		setPreferenceInformation(new MissingPreferenceInformation(0));
		impactMatrix = new ImpactMatrix(alternatives, criteria);
		impactMatrix.addListener(impactListener);		
	}

	public void setPreferenceInformation(PreferenceInformation preferences) {
		this.preferences = preferences;
		preferences.addPropertyChangeListener(new PreferenceListener());
		fireModelChange(ModelChangeEvent.PREFERENCES);
	}
	
	public void addModelListener(SMAAModelListener l) {
		if (!modelListeners.contains(l)) {
			modelListeners.add(l);
		}
	}
	
	public void removeModelListener(SMAAModelListener l) {
		modelListeners.remove(l);
	}
	
	public PreferenceInformation getPreferenceInformation() {
		return preferences;
	}

	public List<Alternative> getAlternatives() {
		return alternatives;
	}
	
	public synchronized void setAlternatives(Collection<Alternative> alts) {
		List<Alternative> altsList = new ArrayList<Alternative>(alts);
		alternatives = altsList;
		impactMatrix.setAlternatives(altsList);
		fireModelChange(ModelChangeEvent.ALTERNATIVES);
	}

	public void setName(String name) {
		Object oldVal = this.name;
		this.name = name;
		firePropertyChange(PROPERTY_NAME, oldVal, name);
	}

	public String getName() {
		return name;
	}	
	
	public void addAlternative(Alternative alt) {
		List<Alternative> alts = new ArrayList<Alternative>(getAlternatives());
		alts.add(alt);
		setAlternatives(alts);
	}

	public List<Criterion> getCriteria() {
		return criteria;
	}
	
	public synchronized void setCriteria(Collection<Criterion> criteria) {
		impactMatrix.removeListener(impactListener);
		List<Criterion> critList = new ArrayList<Criterion>(criteria);
		disconnectConnectCriteriaListeners(getCriteria(), critList);
		this.criteria = critList;
		impactMatrix.setCriteria(critList);
		preferences = new MissingPreferenceInformation(getCriteria().size());
		impactMatrix.addListener(impactListener);
		fireModelChange(ModelChangeEvent.CRITERIA);
		fireModelChange(ModelChangeEvent.PREFERENCES);
	}

	private void disconnectConnectCriteriaListeners(List<Criterion> oldCriteria,
			List<Criterion> newCriteria) {
		for (Criterion c : oldCriteria) {
			c.removePropertyChangeListener(critListener);
		}
		connectCriteriaListeners(newCriteria);		
	}

	private void connectCriteriaListeners(List<Criterion> newCriteria) {
		for (Criterion c : newCriteria) {
			c.addPropertyChangeListener(critListener);
		}
	}

	public void addCriterion(Criterion cri) {
		List<Criterion> crit = new ArrayList<Criterion>();
		crit.addAll(getCriteria());
		crit.add(cri);
		setCriteria(crit);
	}

	public void setMeasurement(CardinalCriterion crit, Alternative alt, CardinalMeasurement meas) {
		impactMatrix.setMeasurement(crit, alt, meas);
	}
	
	public CardinalMeasurement getMeasurement(CardinalCriterion crit, Alternative alt) {
		return impactMatrix.getMeasurement(crit, alt);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String toStringDeep() {
		String ret = name + " : " + alternatives.size() + 
			" alternatives - " + criteria.size() + " criteria\n";
		ret += "Alternatives: " + alternatives + "\n";
		ret += "Criteria: " + criteria + "\n";
		ret += "Measurements:\n";
		ret += impactMatrix.toString() + "\n";
		return ret;
	}
	
	public void deleteAlternative(Alternative a) {
		List<Alternative> newAlts = new ArrayList<Alternative>();
		newAlts.addAll(getAlternatives());
		if (newAlts.remove(a)) {
			setAlternatives(newAlts);
		}
	}
	
	public void deleteCriterion(Criterion c) {
		List<Criterion> newCrit = new ArrayList<Criterion>();
		newCrit.addAll(getCriteria());
		if (newCrit.remove(c)) {
			setCriteria(newCrit);
		}		
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof SMAAModel)) {
			return false;
		}
		SMAAModel m = (SMAAModel) other;
		if (!m.name.equals(name)) {
			return false;
		}
		if (!m.impactMatrix.equals(impactMatrix)) {
			return false;
		}
		if (m.preferences == null) {
			if (preferences != null) {
				return false;
			}
		}
		if (!m.preferences.equals(preferences)) {
			return false;
		}
		
		return true;
	}
	
	public synchronized SMAAModel deepCopy() {
		SMAAModel model = new SMAAModel(name);
		model.setAlternatives(new ArrayList<Alternative>(alternatives));
		model.setCriteria(new ArrayList<Criterion>(criteria));
		model.impactMatrix = (ImpactMatrix) impactMatrix.deepCopy();	
		model.setPreferenceInformation((PreferenceInformation) preferences.deepCopy());
		return model;
	}
	
	public void setMissingPreferences() {
		setPreferenceInformation(
				new MissingPreferenceInformation(getCriteria().size()));
	}

	private void readObject(ObjectInputStream i) throws IOException, ClassNotFoundException {
		modelListeners = new ArrayList<SMAAModelListener>();
		impactListener = new ImpactListener();
		critListener = new CriteriaListener();
			
		i.defaultReadObject();
		impactMatrix.addListener(impactListener);
		connectCriteriaListeners(getCriteria());
		preferences.addPropertyChangeListener(new PreferenceListener());		
	}	
	
	protected void fireModelChange(ModelChangeEvent type) {
		for (SMAAModelListener l : modelListeners) {
			l.modelChanged(type);
		}
	}
	
	
	private class CriteriaListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			if (!evt.getPropertyName().equals(Criterion.PROPERTY_NAME)) {
				fireModelChange(ModelChangeEvent.MEASUREMENT);
			} 
		}
	}
	
	private class ImpactListener implements ImpactMatrixListener {
		public void measurementChanged() {
			fireModelChange(ModelChangeEvent.MEASUREMENT);			
		}

		public void measurementTypeChanged() {
			fireModelChange(ModelChangeEvent.MEASUREMENT_TYPE);
		}
	}
	
	private class PreferenceListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			fireModelChange(ModelChangeEvent.PREFERENCES);			
		}		
	}
	
}
