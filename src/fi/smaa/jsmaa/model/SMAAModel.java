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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashSet;
import java.util.Set;

import com.jgoodies.binding.beans.Model;


public class SMAAModel extends Model {
	
	private static final long serialVersionUID = 1064123340687226048L;
	public final static String PROPERTY_NAME = "name";
		
	private Set<Alternative> alternatives;
	private Set<Criterion> criteria;	
	private String name;
	private PreferenceInformation preferences;
	private ImpactMatrix impactMatrix;
	transient private Set<SMAAModelListener> modelListeners = new HashSet<SMAAModelListener>();
	transient private ImpactMatrixListener impactListener = new ImpactListener();
	
	public SMAAModel(String name) {
		this.name = name;
		init();
	}

	public void setPreferenceInformation(PreferenceInformation preferences) {
		this.preferences = preferences;
		firePreferencesChanged();
	}
	
	public void addModelListener(SMAAModelListener l) {
		modelListeners.add(l);
	}
	
	public void removeModelListener(SMAAModelListener l) {
		modelListeners.remove(l);
	}
	
	public PreferenceInformation getPreferenceInformation() {
		return preferences;
	}

	public Set<Alternative> getAlternatives() {
		return alternatives;
	}
	
	public void setAlternatives(Set<Alternative> alts) {
		this.alternatives = alts;
		impactMatrix.setAlternatives(alts);
		fireAlternativesChanged();
	}

	public void setName(String name) {
		Object oldVal = this.name;
		this.name = name;
		firePropertyChange(PROPERTY_NAME, oldVal, name);
	}

	public String getName() {
		return name;
	}	
	
	public void addAlternative(Alternative alt) throws AlternativeExistsException {
		if (getAlternatives().contains(alt)) {
			throw new AlternativeExistsException();
		}
		Set<Alternative> alts = new HashSet<Alternative>(getAlternatives());
		alts.add(alt);
		setAlternatives(alts);
	}

	public Set<Criterion> getCriteria() {
		return criteria;
	}
	
	public void setCriteria(Set<Criterion> criteria) {
		this.criteria = criteria;
		impactMatrix.setCriteria(criteria);
		preferences = new MissingPreferenceInformation(criteria.size());
		fireCriteriaChanged();
		firePreferencesChanged();
	}

	public void addCriterion(Criterion cri) {
		Set<Criterion> crit = new HashSet<Criterion>();
		crit.addAll(getCriteria());
		crit.add(cri);
		setCriteria(crit);
	}
	
	public ImpactMatrix getImpactMatrix() {
		return impactMatrix;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String toStringDeep() {
		String ret = name + " : " + alternatives.size() + " alternatives - " + criteria.size() + " criteria\n";
		ret += "Alternatives: " + alternatives + "\n";
		ret += "Criteria: " + criteria + "\n";
		ret += "Measurements:\n";
		ret += impactMatrix.toString() + "\n";
		return ret;
	}
	
	public void deleteAlternative(Alternative a) {
		Set<Alternative> newAlts = new HashSet<Alternative>();
		newAlts.addAll(alternatives);
		if (newAlts.remove(a)) {
			setAlternatives(newAlts);
		}
	}
	
	public void deleteCriterion(Criterion c) {
		Set<Criterion> newCrit = new HashSet<Criterion>();
		newCrit.addAll(criteria);
		if (newCrit.remove(c)) {
			setCriteria(newCrit);
		}		
	}
	
	public SMAAModel deepCopy() {
		SMAAModel model = new SMAAModel(name);
		Set<Alternative> alts = new HashSet<Alternative>();
		Set<Criterion> crit = new HashSet<Criterion>();
		for (Alternative a : alternatives) {
			alts.add(a.deepCopy());
		}
		model.setAlternatives(alts);
		for (Criterion c : criteria) {
			crit.add((AbstractCriterion) c.deepCopy());
		}
		model.setCriteria(crit);
		model.setPreferenceInformation((PreferenceInformation) preferences.deepCopy());
		return model;
	}

	public void setMissingPreferences() {
		setPreferenceInformation(
				new MissingPreferenceInformation(criteria.size()));
	}

	private void init() {
		alternatives = new HashSet<Alternative>();
		criteria = new HashSet<Criterion>();
		preferences = new MissingPreferenceInformation(criteria.size());
		impactMatrix = new ImpactMatrix(alternatives, criteria);
		impactMatrix.addListener(impactListener);
	}
	
	private void readObject(ObjectInputStream i) throws IOException, ClassNotFoundException {
		i.defaultReadObject();
		impactMatrix.addListener(impactListener);
	}	
	
	private void firePreferencesChanged() {
		for (SMAAModelListener l : modelListeners) {
			l.preferencesChanged();
		}
	}
	
	private void fireAlternativesChanged() {
		for (SMAAModelListener l : modelListeners) {
			l.alternativesChanged();
		}		
	}
	
	private void fireCriteriaChanged() {
		for (SMAAModelListener l : modelListeners) {
			l.criteriaChanged();
		}				
	}
	
	private void fireMeasurementsChanged() {
		for (SMAAModelListener l : modelListeners) {
			l.measurementsChanged();
		}
	}	
	
	private class ImpactListener implements ImpactMatrixListener {
		public void measurementChanged() {
			fireMeasurementsChanged();
		}		
	}
	
}
