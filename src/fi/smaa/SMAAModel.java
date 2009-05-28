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
import java.util.Collections;
import java.util.List;

import com.jgoodies.binding.beans.Model;
import com.jgoodies.binding.beans.Observable;

@SuppressWarnings("unchecked")
public class SMAAModel extends Model {
	
	private static final long serialVersionUID = 1064123340687226048L;
	public final static String PROPERTY_ALTERNATIVES = "alternatives";
	public final static String PROPERTY_CRITERIA = "criteria";	
	public final static String PROPERTY_NAME = "name";
	public final static String PROPERTY_PREFERENCEINFORMATION = "preferenceInformation";
		
	private List<Alternative> alternatives;
	private List<Criterion> criteria;	
	private String name;
	private PreferenceInformation preferences;
	transient private MemberListener listener = new MemberListener();

	public SMAAModel() {
		init();
	}
	
	public SMAAModel(String name) {
		this.name = name;
		init();
	}
	
	private void writeObject(ObjectOutputStream o) throws IOException {
		o.defaultWriteObject();
	}
	
	private void readObject(ObjectInputStream i) throws IOException, ClassNotFoundException {
		i.defaultReadObject();
		listener = new MemberListener();
		disconnectConnectListeners(Collections.EMPTY_LIST, alternatives);
		disconnectConnectListeners(Collections.EMPTY_LIST, criteria);	
		disconnectConnectListeners(Collections.EMPTY_LIST,
				Collections.singletonList(preferences));				
	}
	
	private void init() {
		alternatives = new ArrayList<Alternative>();
		criteria = new ArrayList<Criterion>();
		preferences = new MissingPreferenceInformation(criteria.size());		
	}

	public void setPreferenceInformation(PreferenceInformation preferences) {
		PreferenceInformation oldVal = this.preferences;
		this.preferences = preferences;
		firePropertyChange(PROPERTY_PREFERENCEINFORMATION, oldVal, this.preferences);
		disconnectConnectListeners(Collections.singletonList(oldVal),
				Collections.singletonList(this.preferences));		
	}
	
	public PreferenceInformation getPreferenceInformation() {
		return preferences;
	}

	public List<Alternative> getAlternatives() {
		return alternatives;
	}
	
	public void setAlternatives(List<Alternative> alts) {
		List<Alternative> oldval = this.alternatives;
		this.alternatives = alts;
		updateCriteriaAlternatives();
		disconnectConnectListeners(oldval, this.alternatives);
		firePropertyChange(PROPERTY_ALTERNATIVES, oldval, this.alternatives);
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
		List<Alternative> alts = new ArrayList<Alternative>(getAlternatives());
		alts.add(alt);
		setAlternatives(alts);
	}

	public List<Criterion> getCriteria() {
		return criteria;
	}
	
	/**
	 * Side-effect: sets the numAlts in each criterion.
	 * 
	 * @param criteria
	 */
	public void setCriteria(List<Criterion> criteria) {
		List<Criterion> oldVal = this.criteria;
		this.criteria = criteria;
		PreferenceInformation oldPref = this.preferences;
		preferences = new MissingPreferenceInformation(criteria.size());
		updateCriteriaAlternatives();	
		firePropertyChange(PROPERTY_CRITERIA, oldVal, criteria);
		firePropertyChange(PROPERTY_PREFERENCEINFORMATION, oldPref,this.preferences);
		disconnectConnectListeners(oldVal, this.criteria);
	}
	
	private void disconnectConnectListeners(List<? extends Observable> disconnects,
			List<? extends Observable> connects) {
		for (Observable c : disconnects) {
			c.removePropertyChangeListener(listener);			
		}
		for (Observable c : connects) {
			c.addPropertyChangeListener(listener);
		}
	}

	private void updateCriteriaAlternatives() {
		for (Criterion c : criteria) {
			c.setAlternatives(alternatives);
		}
	}

	public void addCriterion(Criterion cri) {
		List<Criterion> crit = new ArrayList<Criterion>();
		crit.addAll(getCriteria());
		crit.add(cri);
		setCriteria(crit);
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
		for (Criterion crit : criteria) {
			ret += crit.measurementsToString() + "\n";
		}
		return ret;
	}
	
	public void deleteAlternative(Alternative a) {
		List<Alternative> newAlts = new ArrayList<Alternative>();
		newAlts.addAll(alternatives);
		if (newAlts.remove(a)) {
			setAlternatives(newAlts);
		}
	}
	
	public void deleteCriterion(Criterion c) {
		List<Criterion> newCrit = new ArrayList<Criterion>();
		newCrit.addAll(criteria);
		if (newCrit.remove(c)) {
			setCriteria(newCrit);
		}		
	}
	
	public SMAAModel deepCopy() {
		SMAAModel model = new SMAAModel(name);
		ArrayList<Alternative> alts = new ArrayList<Alternative>();
		ArrayList<Criterion> crit = new ArrayList<Criterion>();
		for (Alternative a : alternatives) {
			alts.add(a.deepCopy());
		}
		model.setAlternatives(alts);
		for (Criterion c : criteria) {
			crit.add((Criterion) c.deepCopy());
		}
		model.setCriteria(crit);
		model.setPreferenceInformation((PreferenceInformation) preferences.deepCopy());
		return model;
	}

	public void setMissingPreferences() {
		setPreferenceInformation(
				new MissingPreferenceInformation(criteria.size()));
	}	
	
	private class MemberListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			for (PropertyChangeListener p : getPropertyChangeListeners()) {
				p.propertyChange(evt);
			}
		}		
	}
}
