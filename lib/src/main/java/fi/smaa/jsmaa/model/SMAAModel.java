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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.jsmaa.model.xml.AlternativeList;
import fi.smaa.jsmaa.model.xml.CriterionList;
import fi.smaa.jsmaa.model.xml.InvalidModelVersionException;



public class SMAAModel extends AbstractEntity {
	public static final int MODELVERSION = 1;
	public final static String PROPERTY_NAME = "name";
	
	private String name;
	protected PreferenceInformation preferences;
	protected ImpactMatrix impactMatrix;
	
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
		preferences.addPropertyChangeListener(new MyPreferenceListener());
		fireModelChange(ModelChangeEvent.PREFERENCES_TYPE);
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
	
	public void setName(String name) {
		Object oldVal = this.name;
		this.name = name;
		firePropertyChange(PROPERTY_NAME, oldVal, name);
	}

	public String getName() {
		return name;
	}	
	
	synchronized public void addAlternative(Alternative alt) {
		if (alternatives.contains(alt)) {
			return;
		}
		alternatives.add(alt);
		
		impactMatrix.removeListener(impactListener);
		impactMatrix.addAlternative(alt);
		impactMatrix.addListener(impactListener);		
		
		fireModelChange(ModelChangeEvent.ALTERNATIVES);
	}

	public List<Criterion> getCriteria() {
		return criteria;
	}
	
	private void connectCriteriaListeners(List<Criterion> newCriteria) {
		for (Criterion c : newCriteria) {
			c.addPropertyChangeListener(critListener);
		}
	}

	synchronized public void addCriterion(Criterion crit) {
		criteria.add(crit);
		
		impactMatrix.removeListener(impactListener);
		impactMatrix.addCriterion(crit);
		impactMatrix.addListener(impactListener);		

		preferences = new MissingPreferenceInformation(getCriteria().size());
		crit.addPropertyChangeListener(critListener);
		
		fireModelChange(ModelChangeEvent.CRITERIA);
	}

	public void setMeasurement(Criterion crit, Alternative alt, Measurement meas) {
		impactMatrix.setMeasurement(crit, alt, meas);
	}
	
	public Measurement getMeasurement(Criterion crit, Alternative alt) {
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
	
	synchronized public void deleteAlternative(Alternative a) {
		if (alternatives.remove(a)) {
			impactMatrix.removeListener(impactListener);						
			impactMatrix.deleteAlternative(a);
			impactMatrix.addListener(impactListener);					
			fireModelChange(ModelChangeEvent.ALTERNATIVES);
		}
	}
	
	synchronized public void deleteCriterion(Criterion c) {
		if (criteria.remove(c)) {
			c.removePropertyChangeListener(critListener);
			
			impactMatrix.removeListener(impactListener);			
			impactMatrix.deleteCriterion(c);
			impactMatrix.addListener(impactListener);		
			
			preferences = new MissingPreferenceInformation(getCriteria().size());
			
			fireModelChange(ModelChangeEvent.CRITERIA);
			fireModelChange(ModelChangeEvent.PREFERENCES_TYPE);
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
	
	synchronized public SMAAModel deepCopy() {
		SMAAModel model = new SMAAModel(name);
		deepCopyContents(model);
		return model;
	}

	protected void deepCopyContents(SMAAModel model) {
		for (Alternative a : alternatives) {
			model.addAlternative(a.deepCopy());
		}
		for (Criterion c : criteria) {
			model.addCriterion(c.deepCopy());
		}
		model.impactMatrix = (ImpactMatrix) impactMatrix.deepCopy(model.getAlternatives(), model.getCriteria());		
		model.impactMatrix.addListener(model.impactListener);
		model.setPreferenceInformation((PreferenceInformation) preferences.deepCopy());
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
		preferences.addPropertyChangeListener(new MyPreferenceListener());		
	}	
	
	protected void fireModelChange(int type) {
		ModelChangeEvent ev = new ModelChangeEvent(this, type);
		for (SMAAModelListener l : modelListeners) {
			l.modelChanged(ev);
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
	
	private class MyPreferenceListener implements PropertyChangeListener, Serializable {
		private static final long serialVersionUID = 9084801400903300047L;

		public void propertyChange(PropertyChangeEvent arg0) {
			fireModelChange(ModelChangeEvent.PREFERENCES);
		}		
	}
	
	public ImpactMatrix getImpactMatrix() {
		return impactMatrix;
	}

	public void reorderAlternatives(List<Alternative> newAlts) {
		this.alternatives = newAlts;
		impactMatrix.reorderAlternatives(newAlts);
		fireModelChange(ModelChangeEvent.ALTERNATIVES);
	}

	public void reorderCriteria(List<Criterion> newCrit) {
		this.criteria = newCrit;
		impactMatrix.reorderCriteria(newCrit);
		fireModelChange(ModelChangeEvent.CRITERIA);		
	}
	
	protected static final XMLFormat<SMAAModel> XML = new XMLFormat<SMAAModel>(SMAAModel.class) {		
		@Override
		public boolean isReferenceable() {
			return false;
		}
		@Override
		public SMAAModel newInstance(Class<SMAAModel> cls, InputElement ie) throws XMLStreamException {
			return new SMAAModel(ie.getAttribute("name").toString());
		}
		@Override
		public void read(InputElement ie, SMAAModel model) throws XMLStreamException {
			int version = ie.getAttribute("modelVersion", 0);
			if (version > MODELVERSION) {
				throw new InvalidModelVersionException(version);
			}
			model.setName(ie.getAttribute("name").toString());
			AlternativeList alternatives = ie.get("alternatives", AlternativeList.class);
			for (Alternative a : alternatives.getList()) {
				model.addAlternative(a);
			}
			CriterionList crits = ie.get("criteria", CriterionList.class);
			for (Criterion c : crits.getList()) {
				model.addCriterion(c);
			}
			ImpactMatrix im = ie.get("measurements", ImpactMatrix.class);
			model.impactMatrix = im;
			model.impactMatrix.addListener(model.impactListener);
			
			PreferenceInformation pref = ie.get("preferences");
			if (pref != null) {
				model.setPreferenceInformation(pref);
			}
		}
		@Override
		public void write(SMAAModel model, OutputElement oe) throws XMLStreamException {
			oe.setAttribute("name", model.getName());
			oe.setAttribute("modelVersion", MODELVERSION);
			oe.add(new AlternativeList(model.alternatives), "alternatives", AlternativeList.class);			
			oe.add(new CriterionList(model.criteria), "criteria", CriterionList.class);
			oe.add(model.impactMatrix, "measurements", ImpactMatrix.class);
			if (!(model.getPreferenceInformation() instanceof MissingPreferenceInformation)) {
				oe.add(model.preferences, "preferences");
			}
		}		
	};		
}
