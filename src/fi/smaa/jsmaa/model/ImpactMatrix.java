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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.smaa.jsmaa.common.Interval;

public class ImpactMatrix {
	
	private Set<Criterion> criteria = new HashSet<Criterion>();
	private Set<Alternative> alternatives = new HashSet<Alternative>();
	private Map<Criterion, Map<Alternative, Measurement>> measurements 
		= new HashMap<Criterion, Map<Alternative, Measurement>>();
	private transient MeasurementListener measListener = new MeasurementListener();
	private transient List<ImpactMatrixListener> thisListeners = new ArrayList<ImpactMatrixListener>(); 
		
	/**
	 * Constructs an impact matrix without alternatives or criteria.
	 */
	public ImpactMatrix() {
	}
	
	@Override
	public String toString() {
		return measurements.toString();
	}
	
	/**
	 * Constructs an impact matrix with a set of alternatives and criteria.
	 * 
	 * @param alternatives the alternatives.
	 * @param criteria the criteria.
	 */
	public ImpactMatrix(Set<Alternative> alternatives, Set<Criterion> criteria) {
		setCriteria(criteria);		
		setAlternatives(alternatives);
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ImpactMatrix)) {
			return false;
		}
		ImpactMatrix m = (ImpactMatrix) other;
		if (!criteria.equals(m.criteria)) {
			return false;
		}
		if (!alternatives.equals(m.alternatives)) {
			return false;
		}
		if (!measurements.equals(m.measurements)) {
			return false;
		}
		return true;
	}
	
	public void addListener(ImpactMatrixListener l) {
		if (thisListeners.contains(l)) {
			return;
		}
		thisListeners.add(l);
	}
	
	public void removeListener(ImpactMatrixListener l) {
		thisListeners.remove(l);
	}	
	
	public void setMeasurement(CardinalCriterion crit, Alternative alt, CardinalMeasurement meas)
	throws NoSuchAlternativeException, NoSuchCriterionException {
		checkExistAlternativeAndCriterion(crit, alt);
		disconnectConnectMeasurementListener(crit, alt, meas);
		measurements.get(crit).put(alt, meas);
		fireMeasurementChanged();
		updateScales();
	}
	
	public CardinalMeasurement getMeasurement(CardinalCriterion crit, Alternative alt) 
	throws NoSuchAlternativeException, NoSuchCriterionException {
		checkExistAlternativeAndCriterion(crit, alt);
		return (CardinalMeasurement) measurements.get(crit).get(alt);
	}
	
	/*
	public void setMeasurement(OrdinalCriterion crit, Alternative alt, Rank meas)
	throws NoSuchAlternativeException, NoSuchCriterionException {
		checkExistAlternativeAndCriterion(crit, alt);
		measurements.get(crit).put(alt, meas);
	}
	*/	
	
	/*
	public Rank getMeasurement(OrdinalCriterion crit, Alternative alt) 
	throws NoSuchAlternativeException, NoSuchCriterionException {
		checkExistAlternativeAndCriterion(crit, alt);
		return (Rank) measurements.get(crit).get(alt);
	}	
	*/	


	/**
	 * Deletes an alternative. If alternative doesn't exist, does nothing.
	 * @param alt Alternative to delete.
	 */
	public void deleteAlternative(Alternative alt) {
		Set<Alternative> newAlts = new HashSet<Alternative>(alternatives);
		newAlts.remove(alt);
		setAlternatives(newAlts);
	}
	
	/**
	 * Adds an alternative. If alternative already exists, does nothing.
	 * @param alt Alternative to add.
	 */
	public void addAlternative(Alternative alt) {
		Set<Alternative> newAlts = new HashSet<Alternative>(alternatives);
		newAlts.add(alt);
		setAlternatives(newAlts);		
	}
	
	/**
	 * Deletes a criterion. If criterion doesn't exist, does nothing.
	 * @param c Criterion to delete
	 */
	public void deleteCriterion(Criterion c) {
		Set<Criterion> newCrit = new HashSet<Criterion>(criteria);
		newCrit.remove(c);
		setCriteria(newCrit);
	}
	
	/**
	 * Adds an alternative. If alternative already exists, does nothing.
	 * @param c
	 */
	public void addCriterion(Criterion c) {
		Set<Criterion> newCrit = new HashSet<Criterion>(criteria);		
		newCrit.add(c);
		setCriteria(newCrit);
	}
	
	/**
	 * Gets the alternatives.
	 * @return the alternatives. Never a null.
	 */
	public Set<Alternative> getAlternatives() {
		return alternatives;
	}
	
	/**
	 * Gets the criteria.
	 * @return the criteria. Never a null.
	 */
	public Set<Criterion> getCriteria() {
		return criteria;
	}

	public void setAlternatives(Set<Alternative> alternatives) {
		for (Alternative a : alternatives) {
			for (Criterion c : criteria) {
				Map<Alternative, Measurement> map = measurements.get(c);
				if (!map.keySet().contains(a)) {
					if (c instanceof CardinalCriterion) {
						map.put(a, new Interval());
					}
				}
			}
		}
		this.alternatives = alternatives;
		updateScales();
	}
	
	public void setCriteria(Set<Criterion> criteria) {
		for (Criterion c : criteria) {
			if (measurements.get(c) == null) {
				Map<Alternative, Measurement> m = new HashMap<Alternative, Measurement>();
				if (c instanceof CardinalCriterion) {
					for (Alternative a : alternatives) {
						m.put(a, new Interval());
					}
				}
				measurements.put(c, m);
			}
		}
		this.criteria = criteria;
		updateScales();
	}
	
	private void updateScales() {
		for (Criterion c : criteria) {
			if (c instanceof CardinalCriterion) {
				Map<Alternative, Measurement> cMeas = measurements.get(c);
				
				ArrayList<Interval> ivals = new ArrayList<Interval>();
				for (Measurement m : cMeas.values()) {
					CardinalMeasurement cm = (CardinalMeasurement) m;
					ivals.add(cm.getRange());
				}
				Interval scale = ivals.size() == 0 ? new Interval(0.0, 0.0) 
					: Interval.enclosingInterval(ivals);
				((CardinalCriterion) c).setScale(scale);
			}
		}
	}
	
	private void checkExistAlternativeAndCriterion(Criterion crit,
			Alternative alt) throws NoSuchCriterionException,
			NoSuchAlternativeException {
		if (!criteria.contains(crit)) {
			throw new NoSuchCriterionException();
		}
		if (!alternatives.contains(alt)) {
			throw new NoSuchAlternativeException();
		}
	}	
		
	private void readObject(ObjectInputStream i) throws IOException, ClassNotFoundException {
		i.defaultReadObject();
		for (Map<Alternative, Measurement> m : measurements.values()) {
			for (Measurement meas : m.values()) {
				if (meas != null) {
					meas.addPropertyChangeListener(measListener);
				}
			}
		}
	}	
	
	private void disconnectConnectMeasurementListener(CardinalCriterion crit,
			Alternative alt, CardinalMeasurement meas) {
		Measurement m = measurements.get(crit).put(alt, meas);
		if (m != null) {
			m.removePropertyChangeListener(measListener);
		}
		meas.addPropertyChangeListener(measListener);
	}
	
	private class MeasurementListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getSource() instanceof CardinalMeasurement) {
				updateScales();
			}
			fireMeasurementChanged();	
		}
	}
	
	private void fireMeasurementChanged() {
		for (ImpactMatrixListener l : thisListeners) {
			l.measurementChanged();
		}
	}			
}
