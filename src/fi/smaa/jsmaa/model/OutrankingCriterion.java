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
		indifferenceThreshold = indifMeasurement.sample();
		preferenceThreshold = prefMeasurement.sample();		
	}
	
	public void sampleThresholds() {
		indifferenceThreshold = indifMeasurement.sample();
		preferenceThreshold = prefMeasurement.sample();
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
	
	public OutrankingCriterion deepCopy() {
		return new OutrankingCriterion(name, ascending, (CardinalMeasurement)indifMeasurement.deepCopy(),
				(CardinalMeasurement)prefMeasurement.deepCopy());
	}
	
	private class PrefListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			firePropertyChange(PROPERTY_PREF_MEASUREMENT, null, getPrefMeasurement());
		}
	}
	
	private class IndifListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			firePropertyChange(PROPERTY_INDIF_MEASUREMENT, null, getIndifMeasurement());
		}
	}	

}
