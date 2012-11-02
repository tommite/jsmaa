/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.

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
import java.util.List;
import java.util.Map;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.common.RandomUtil;
import fi.smaa.jsmaa.model.xml.CriterionAlternativeMeasurement;
import fi.smaa.jsmaa.simulator.Sampler;

public final class ImpactMatrix extends AbstractMeasurements implements IndependentMeasurements {
	
	private static final long serialVersionUID = -5524839710856011441L;
	
	Map<Criterion, Map<Alternative, Measurement>> measurements = new HashMap<Criterion, Map<Alternative, Measurement>>();
	private Map<Criterion, BaselineGaussianMeasurement> baselines = new HashMap<Criterion, BaselineGaussianMeasurement>();
	transient MeasurementListener measListener = new MeasurementListener();
	private transient Map<Criterion, RankSet<Alternative>> ordinalCriteriaRanksSets = new HashMap<Criterion, RankSet<Alternative>>();

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
	public ImpactMatrix(List<Alternative> alternatives, List<Criterion> criteria) {
		for (Criterion c : criteria) {
			addCriterion(c, true);
		}
		for (Alternative a : alternatives) {
			addAlternative(a);
		}
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
		if (!baselines.equals(m.baselines)) {
			return false;
		}
		return true;
	}
	
	@Override
	public void setMeasurement(Criterion crit, Alternative alt, Measurement meas) {
		if (meas == null) {
			throw new NullPointerException("null measurement");
		}
		assertExistAlternativeAndCriterion(crit, alt);
		setMeasurementNoFires(crit, alt, meas);
		if (crit instanceof ScaleCriterion) {
			updateScales();
		}
		fireMeasurementTypeChanged();
	}

	private void setMeasurementNoFires(Criterion crit, Alternative alt,
			Measurement meas) {
		disconnectConnectMeasurementListener(crit, alt, meas);
		measurements.get(crit).put(alt, meas);
	}
	
	@Override
	public Measurement getMeasurement(Criterion crit, Alternative alt) {
		assertExistAlternativeAndCriterion(crit, alt);
		return measurements.get(crit).get(alt);
	}
	
	@Override
	public void deleteAlternative(Alternative alt) {
		if (!alternatives.contains(alt)) {
			return;
		}
		alternatives.remove(alt);
		for (RankSet<Alternative> set : ordinalCriteriaRanksSets.values()) {
			set.deleteObject(alt);
		}
		updateScales();
	}
	
	@Override	
	public void addAlternative(Alternative alt) {
		addAlternative(alt, true);
	}
	
	private void addAlternative(Alternative alt, boolean updateScales) {
		if (alternatives.contains(alt)) {
			return;
		}
		alternatives.add(alt);
		for (Criterion c : criteria) {			
			Map<Alternative, Measurement> map = measurements.get(c);			
			if (!map.keySet().contains(alt)) {
				if (c instanceof CardinalCriterion) {
					setMeasurementNoFires(c, alt, new Interval());
				} else if (c instanceof OrdinalCriterion) {
					ordinalCriteriaRanksSets.get(c).addObject(alt);
					Rank r = ordinalCriteriaRanksSets.get(c).getRank(alt);
					setMeasurementNoFires(c, alt, r);
				}
			}
		}
		if (updateScales) {
			updateScales();
		}
	}
	
	@Override
	public void deleteCriterion(Criterion c) {
		if (!criteria.contains(c)) {
			return;
		}
		criteria.remove(c);
		measurements.remove(c);
		ordinalCriteriaRanksSets.remove(c);
		baselines.remove(c);
	}
	
	@Override
	public void addCriterion(Criterion c, boolean updateScales) {
		if (criteria.contains(c)) {
			return;
		}
		criteria.add(c);	
		HashMap<Alternative, Measurement> newMap = new HashMap<Alternative, Measurement>();
		measurements.put(c, newMap);
		if (c instanceof OrdinalCriterion) {
			RankSet<Alternative> rs = new RankSet<Alternative>();
			ordinalCriteriaRanksSets.put(c, rs);
			for (Alternative a : alternatives) {
				rs.addObject(a);
				Rank r = rs.getRank(a);
				setMeasurementNoFires(c, a, r);
			}
		} else if (c instanceof CardinalCriterion) {
			baselines.put(c, new BaselineGaussianMeasurement());
			for (Alternative a : alternatives) {
				if (getMeasurement(c, a) == null) {
					setMeasurementNoFires(c, a, new Interval());
				}
			}
		}
		if (c instanceof ScaleCriterion && updateScales) {
			updateScales();
		}
	}
	
	private void updateScales() {
		for (Criterion c : criteria) {
			if (c instanceof ScaleCriterion) {
				Map<Alternative, Measurement> cMeas = measurements.get(c);
				
				ArrayList<Interval> ivals = new ArrayList<Interval>();
				for (Measurement m : cMeas.values()) {
					CardinalMeasurement cm = (CardinalMeasurement) m;
					ivals.add(cm.getRange());
				}
				Interval scale = ivals.size() == 0 ? new Interval(0.0, 0.0) 
					: Interval.enclosingInterval(ivals);
				((ScaleCriterion) c).setScale(scale);
			}
		}
	}
	
	private void readObject(ObjectInputStream i) throws IOException, ClassNotFoundException {
		i.defaultReadObject();
		
		measListener = new MeasurementListener();
		ordinalCriteriaRanksSets = new HashMap<Criterion, RankSet<Alternative>>();
		
		for (Criterion c : criteria) {
			if (c instanceof OrdinalCriterion) {
				Map<Alternative, Rank> ranks = new HashMap<Alternative, Rank>();
				for (Alternative a : alternatives) {
					ranks.put(a, (Rank) getMeasurement(c, a));
				}
				ordinalCriteriaRanksSets.put(c, new RankSet<Alternative>(ranks));
			}
		}
		
		for (Map<Alternative, Measurement> m : measurements.values()) {
			for (Measurement meas : m.values()) {
				if (meas != null) {
					meas.addPropertyChangeListener(measListener);
				}
			}
		}
	}

	private class MeasurementListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getSource() instanceof CardinalMeasurement) {
				updateScales();
			}
			fireMeasurementChanged();
		}
	}
	
	protected void disconnectConnectMeasurementListener(Criterion crit, Alternative alt, Measurement meas) {
		if (meas == null) {
			throw new NullPointerException("null measurement");
		}
		Measurement m = measurements.get(crit).get(alt);
		if (m != null) {
			m.removePropertyChangeListener(measListener);
		}
		meas.addPropertyChangeListener(measListener);
	}
	
	@Override
	public ImpactMatrix deepCopy(List<Criterion> crit, List<Alternative> alts) {
		if (getAlternatives().size() != alts.size()) {
			throw new IllegalArgumentException("ImpactMatrix.deepCopy() : getAlternatives().size() != alts.size()");
		}
		if (getCriteria().size() != crit.size()) {
			throw new IllegalArgumentException("ImpactMatrix.deepCopy() : getCriteria().size() != crit.size()");
		}
		
		ImpactMatrix other = new ImpactMatrix(alts, crit);	

		int cIndex = 0;
		for (Criterion c : getCriteria()) {
			int aIndex = 0;
			if (this.getBaseline(c) != null) {
				other.setBaseline(crit.get(cIndex), this.getBaseline(c).deepCopy());
			}
			for (Alternative a : getAlternatives()) {
				Measurement m = getMeasurement(c, a).deepCopy();
				// ensure measurements on the same criterion have the same baseline:
				if (m instanceof RelativeGaussianMeasurementBase) {
					((RelativeGaussianMeasurementBase)m).setBaseline(other.getBaseline(crit.get(cIndex)));
				}
				other.setMeasurement(crit.get(cIndex), alts.get(aIndex), m);
				aIndex++;
			}			
			cIndex++;
		}
		return other;		
	}

	public void setBaseline(Criterion c, BaselineGaussianMeasurement m) {
		baselines.put(c, m);		
	}

	public BaselineGaussianMeasurement getBaseline(Criterion c) {
		return baselines.get(c);
	}

	@Override
	public void reorderAlternatives(List<Alternative> newAlts) {
		this.alternatives.clear();
		this.alternatives.addAll(newAlts);
	}

	@Override
	public void reorderCriteria(List<Criterion> newCrit) {
		this.criteria.clear();
		this.criteria.addAll(newCrit);
	}
	
	@SuppressWarnings("unused")
	private static final XMLFormat<ImpactMatrix> XML = new XMLFormat<ImpactMatrix>(ImpactMatrix.class) {		
		@Override
		public boolean isReferenceable() {
			return false;
		}
		@Override
		public ImpactMatrix newInstance(Class<ImpactMatrix> cls, InputElement ie) throws XMLStreamException {
			return new ImpactMatrix();
		}
		@Override
		public void read(InputElement ie, ImpactMatrix mat) throws XMLStreamException {
			while (ie.hasNext()) {
				CriterionAlternativeMeasurement m = ie.get("measurement", CriterionAlternativeMeasurement.class);
				mat.addAlternative(m.getAlternative(), false);
				mat.addCriterion(m.getCriterion(), false);
				mat.setMeasurementNoFires(m.getCriterion(), m.getAlternative(), m.getMeasurement());
				if (m.getMeasurement() instanceof RelativeGaussianMeasurementBase) {
					RelativeGaussianMeasurementBase rm = (RelativeGaussianMeasurementBase)m.getMeasurement();
					mat.setBaseline(m.getCriterion(), rm.getBaseline());
				}
				if (m.getCriterion() instanceof ScaleCriterion) {
					ScaleCriterion sc = (ScaleCriterion) m.getCriterion();
					if (sc.getScale() == null) {
						mat.updateScales();
					}
				}
			}
		}
		@Override
		public void write(ImpactMatrix mat, OutputElement oe) throws XMLStreamException {
			for (Criterion c : mat.getCriteria()) {
				for (Alternative a : mat.getAlternatives()) {
					CriterionAlternativeMeasurement m = new CriterionAlternativeMeasurement(a, c, mat.getMeasurement(c, a));
					oe.add(m, "measurement", CriterionAlternativeMeasurement.class);
				}
			}
		}		
	};

	@Override
	public void sample(RandomUtil random, double[][] target) {
		Sampler sampler = new Sampler(this, random);
		updateBaselines(random);
		for (int i = 0; i < getCriteria().size(); i++) {
			sampler.sample(getCriteria().get(i), target[i]);
		}
	}	
	
	private void updateBaselines(RandomUtil random) {
		for (Criterion c : getCriteria()) {
			if (getBaseline(c) != null) {
				getBaseline(c).update(random);
			}
		}
	}

	@Override
	public Interval getRange(Criterion crit) {
		if (crit instanceof ScaleCriterion) {
			return ((ScaleCriterion) crit).getScale();
		}
		return null;
	}
}
