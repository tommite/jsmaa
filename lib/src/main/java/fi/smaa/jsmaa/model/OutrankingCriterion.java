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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import fi.smaa.common.RandomUtil;
import fi.smaa.jsmaa.simulator.IterationException;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;


public final class OutrankingCriterion extends CardinalCriterion {
	public static final String PROPERTY_INDIF_MEASUREMENT = "indifMeasurement";
	public static final String PROPERTY_PREF_MEASUREMENT = "prefMeasurement";

	private static final long serialVersionUID = 2226047865113684859L;
	
	private double indifferenceThreshold;
	private double preferenceThreshold;
	private CardinalMeasurement indifMeasurement;
	private CardinalMeasurement prefMeasurement;
	
	public OutrankingCriterion(String name, boolean ascending, CardinalMeasurement indifMeasurement,
			CardinalMeasurement prefMeasurement) {
		super(name, ascending);
		setIndifMeasurement(indifMeasurement);
		setPrefMeasurement(prefMeasurement);
		RandomUtil random = RandomUtil.createWithFixedSeed(); // FIXME: hack
		indifferenceThreshold = indifMeasurement.sample(random);
		preferenceThreshold = prefMeasurement.sample(random);
	}
		
	public void sampleThresholds(RandomUtil random) throws IterationException {
		for (int i=0;i<1000;i++) {
			indifferenceThreshold = indifMeasurement.sample(random);
			preferenceThreshold = prefMeasurement.sample(random);
			if (indifferenceThreshold <= preferenceThreshold) {
				return;
			}
		}
		throw new IterationException("Cannot sample thresholds for "+getName()+": indifference > preference");
	}
	
	public double getIndifferenceThreshold() {
		return indifferenceThreshold;
	}

	public void setIndifMeasurement(CardinalMeasurement indifMeasurement) {
		CardinalMeasurement oldVal = this.indifMeasurement;
		this.indifMeasurement = indifMeasurement;
		this.indifMeasurement.addPropertyChangeListener(new IndifListener());		
		firePropertyChange(PROPERTY_INDIF_MEASUREMENT, oldVal, this.indifMeasurement);
	}
	
	public void setPrefMeasurement(CardinalMeasurement prefMeasurement) {
		CardinalMeasurement oldVal = this.prefMeasurement;
		this.prefMeasurement = prefMeasurement;
		this.prefMeasurement.addPropertyChangeListener(new PrefListener());
		firePropertyChange(PROPERTY_PREF_MEASUREMENT, oldVal, this.prefMeasurement);
	}	
	
	public CardinalMeasurement getIndifMeasurement() {
		return indifMeasurement;
	}
	
	public CardinalMeasurement getPrefMeasurement() {
		return prefMeasurement;
	}

	public double getPreferenceThreshold() {
		return preferenceThreshold;
	}

	@Override
	public String getTypeLabel() {
		return "Outranking";
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.prefMeasurement.addPropertyChangeListener(new PrefListener());
		this.indifMeasurement.addPropertyChangeListener(new IndifListener());
	}
	
	public OutrankingCriterion deepCopy() {
		return new OutrankingCriterion(name, ascending, (CardinalMeasurement)indifMeasurement.deepCopy(),
				(CardinalMeasurement)prefMeasurement.deepCopy());
	}
	
	private class PrefListener implements PropertyChangeListener, Serializable {
		private static final long serialVersionUID = -2064991382287461823L;

		public void propertyChange(PropertyChangeEvent evt) {
			firePropertyChange(PROPERTY_PREF_MEASUREMENT, null, getPrefMeasurement());
		}
	}
	
	private class IndifListener implements PropertyChangeListener, Serializable {
		private static final long serialVersionUID = -5202261775299851878L;

		public void propertyChange(PropertyChangeEvent evt) {
			firePropertyChange(PROPERTY_INDIF_MEASUREMENT, null, getIndifMeasurement());
		}
	}
	
	@SuppressWarnings("unused")
	private static final XMLFormat<OutrankingCriterion> XML = new XMLFormat<OutrankingCriterion>(OutrankingCriterion.class) {
		@Override
		public OutrankingCriterion newInstance(Class<OutrankingCriterion> cls, InputElement ie) throws XMLStreamException {
			return new OutrankingCriterion(ie.getAttribute("name", null), ie.getAttribute("ascending", true), 
					new ExactMeasurement(0.0), new ExactMeasurement(1.0)); 
		}		
		@Override
		public boolean isReferenceable() {
			return CardinalCriterion.XML.isReferenceable();
		}
		@Override
		public void read(InputElement ie, OutrankingCriterion crit) throws XMLStreamException {
			CardinalCriterion.XML.read(ie, crit);
			crit.indifMeasurement = ie.get("indifferenceTH");
			crit.prefMeasurement = ie.get("preferenceTH");			
		}
		@Override
		public void write(OutrankingCriterion crit, OutputElement oe) throws XMLStreamException {
			CardinalCriterion.XML.write(crit, oe);
			oe.add(crit.indifMeasurement, "indifferenceTH");
			oe.add(crit.prefMeasurement, "preferenceTH");			
		}		
	};	

}
