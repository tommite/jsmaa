/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper 2012.

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.smaa.common.RandomUtil;

/**
 * Measurements that are independent between criteria, but dependent between alternatives.
 * @see Measurement. 
 */
public class PerCriterionMeasurements extends AbstractMeasurements implements FullJointMeasurements {
	private class MeasurementListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			updateScales();
			fireMeasurementChanged();
		}
	}
		
	
	private static final long serialVersionUID = -6733139577051452519L;
	private final Map<Criterion, CriterionMeasurement> critMeas = new HashMap<Criterion, CriterionMeasurement>();
	private final MeasurementListener listener = new MeasurementListener();
	
	public PerCriterionMeasurements(List<Criterion> criteria, List<Alternative> alternatives) {
		super(criteria, alternatives);
		for (Criterion c : criteria) {
			critMeas.put(c, createDefaultMeasurement());
		}
		updateScales();
	}

	private MultivariateGaussianCriterionMeasurement createDefaultMeasurement() {
		final MultivariateGaussianCriterionMeasurement m = new MultivariateGaussianCriterionMeasurement(this.alternatives);
		m.addPropertyChangeListener(listener);
		return m;
	}
	
	/**
	 * Get the measurement for this criterion.
	 */
	public CriterionMeasurement getCriterionMeasurement(Criterion c) {
		return critMeas.get(c);
	}
	
	/**
	 * Set the measurement for this criterion.
	 */
	public void setCriterionMeasurement(Criterion c, CriterionMeasurement m) {
		if (critMeas.containsKey(c)) {
			critMeas.get(c).removePropertyChangeListener(listener);
		}
		critMeas.put(c, m);
		m.addPropertyChangeListener(listener);
		updateScales();
	}

	@Override
	public void addAlternative(Alternative alt) {
		alternatives.add(alt);
		for (Criterion c : criteria) {
			critMeas.get(c).addAlternative(alt);
		}
	}

	@Override
	public void deleteAlternative(Alternative alt) {
		alternatives.remove(alt);
		for (Criterion c : criteria) {
			critMeas.get(c).deleteAlternative(alt);
		}		
	}

	@Override
	public void reorderAlternatives(List<Alternative> alts) {
		alternatives.clear();
		alternatives.addAll(alts);
		for (Criterion c : criteria) {
			critMeas.get(c).reorderAlternatives(alts);
		}				
	}

	@Override
	public void addCriterion(Criterion c) {
		criteria.add(c);
		critMeas.put(c, createDefaultMeasurement());
	}

	@Override
	public void deleteCriterion(Criterion c) {
		criteria.remove(c);
		critMeas.get(c).removePropertyChangeListener(listener);
		critMeas.remove(c);
	}

	@Override
	public void reorderCriteria(List<Criterion> crits) {
		criteria.clear();
		criteria.addAll(crits);
	}

	@Override
	public void sample(RandomUtil random, double[][] target) {
		for(int i = 0; i < criteria.size(); ++i) { 
			getCriterionMeasurement(criteria.get(i)).sample(random, target, i);
		}
	}

	@Override
	public Interval getRange(Criterion crit) {
		return getCriterionMeasurement(crit).getRange();
	}

	@Override
	public PerCriterionMeasurements deepCopy(List<Criterion> crit, List<Alternative> alts) {
		if (getAlternatives().size() != alts.size()) {
			throw new IllegalArgumentException("getAlternatives().size() != alts.size()");
		}
		if (getCriteria().size() != crit.size()) {
			throw new IllegalArgumentException("getCriteria().size() != crit.size()");
		}
		
		PerCriterionMeasurements m = new PerCriterionMeasurements(crit, alts);
		for (int i = 0; i < crit.size(); ++i) { 
			m.setCriterionMeasurement(crit.get(i), this.getCriterionMeasurement(criteria.get(i)).deepCopy(alts));
		}
		return m;
	}
	

	private void updateScales() {
		for(Criterion c : this.criteria) { 
			if(c instanceof ScaleCriterion) { 
				((ScaleCriterion)c).setScale(getRange(c));
			}
		}
	}	
}
