/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.common.RandomUtil;
import fi.smaa.jsmaa.model.xml.javolution.CriterionMeasurementPair;
import fi.smaa.jsmaa.simulator.IterationException;

public final class CardinalPreferenceInformation extends AbstractPreferenceInformation<CardinalMeasurement> {

	private static final long serialVersionUID = 5119910625472241337L;
	private static final int MAXGENITERS = 10000;
	protected Map<Criterion, CardinalMeasurement> prefs = new HashMap<Criterion, CardinalMeasurement>();

	public CardinalPreferenceInformation(List<Criterion> criteria) {
		super(criteria);
		initMeasurements();
	}

	private void initMeasurements() {
		for (Criterion c : criteria) {
			setMeasurement(c, new ExactMeasurement(0.0));
		}
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

	public double[] sampleWeights() throws IterationException {
		double[] weights = new double[criteria.size()];

		double lowerBounds = 0.0;
		int numIntervalCriteria = 0;
		for (int i=0;i<weights.length;i++) {
			CardinalMeasurement meas = prefs.get(criteria.get(i));
			if (meas instanceof Interval) {
				numIntervalCriteria++;
			}
			lowerBounds += meas.getRange().getStart();
		}
		if (lowerBounds > 1.0) {
			throw new IterationException("weight lower bounds over 1.0");
		}

		double[] tmpArr = new double[numIntervalCriteria];

		for (int iter=0;iter<MAXGENITERS;iter++) {
			if (numIntervalCriteria > 0) {
				RandomUtil.createSumToRand(tmpArr, 1.0 - lowerBounds);
			}

			int currentInterval = 0;
			boolean overUpperBound = false;
			for (int i=0;i<weights.length;i++) {
				CardinalMeasurement meas = prefs.get(criteria.get(i));
				if (meas instanceof ExactMeasurement) {
					weights[i] = ((ExactMeasurement) meas).getValue();
				} else if (meas instanceof Interval) {
					weights[i] = meas.getRange().getStart() + tmpArr[currentInterval];
					if (weights[i] > meas.getRange().getEnd()) {
						overUpperBound = true;
						break;
					}				
					currentInterval++;
				} else {
					throw new RuntimeException("unknown weight constraint type");
				}
			}

			if (!overUpperBound && checkSumTo1(weights)) {
				return weights;
			}
		}
		throw new IterationException("infeasible weight constraints");
	}

	private boolean checkSumTo1(double[] weights) {
		double sum = 0.0;
		for (double d : weights) {
			sum += d;
		}
		return sum == 1.0;
	}

	public CardinalPreferenceInformation deepCopy() {
		CardinalPreferenceInformation pref = new CardinalPreferenceInformation(criteria);
		for (Criterion c : prefs.keySet()) {
			pref.setMeasurement(c, prefs.get(c));
		}
		return pref;
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		for (Criterion c : criteria) {
			getMeasurement(c).addPropertyChangeListener(measListener);
		}	
	}

	@SuppressWarnings("unused")
	private static final XMLFormat<CardinalPreferenceInformation> XML 
		= new XMLFormat<CardinalPreferenceInformation>(CardinalPreferenceInformation.class) {
		
		@Override
		public CardinalPreferenceInformation newInstance(Class<CardinalPreferenceInformation> cls, InputElement ie) throws XMLStreamException {
			return new CardinalPreferenceInformation(new ArrayList<Criterion>());
		}				
		@Override
		public boolean isReferenceable() {
			return true;
		}
		@Override
		public void read(InputElement ie, CardinalPreferenceInformation pref) throws XMLStreamException {
			CriterionMeasurementPair p = null;
			while (ie.hasNext()) {
				p = ie.get("preference", CriterionMeasurementPair.class);
				pref.criteria.add(p.getCriterion());
				pref.setMeasurement(p.getCriterion(), (CardinalMeasurement) p.getMeasurement());
			}
		}
		@Override
		public void write(CardinalPreferenceInformation pref, OutputElement oe) throws XMLStreamException {
			for (Criterion c : pref.criteria) {
				CriterionMeasurementPair p = new CriterionMeasurementPair(c, pref.getMeasurement(c));
				oe.add(p, "preference", CriterionMeasurementPair.class);
			}
		}
	};	
}
