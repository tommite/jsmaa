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


public class OutrankingCriterion extends CardinalCriterion {
	
	public static final String PROPERTY_INDIFFERENCE_THRESHOLD = "indifferenceThreshold";
	public static final String PROPERTY_PREFERENCE_THRESHOLD = "preferenceThreshold";

	private static final long serialVersionUID = 2226047865113684859L;
	
	private double indifferenceThreshold;
	private double preferenceThreshold;

	public OutrankingCriterion(String name, boolean ascending, double indifferenceThreshold,
			double preferenceThreshold) {
		super(name, ascending);
		this.indifferenceThreshold = indifferenceThreshold;
		this.preferenceThreshold = preferenceThreshold;
	}
	
	public double getIndifferenceThreshold() {
		return indifferenceThreshold;
	}

	public void setIndifferenceThreshold(double indifferenceThreshold) {
		double oldVal = this.indifferenceThreshold;
		this.indifferenceThreshold = indifferenceThreshold;
		firePropertyChange(PROPERTY_INDIFFERENCE_THRESHOLD, oldVal, this.indifferenceThreshold);
	}

	public double getPreferenceThreshold() {
		return preferenceThreshold;
	}

	public void setPreferenceThreshold(double preferenceThreshold) {
		double oldVal = this.preferenceThreshold;
		this.preferenceThreshold = preferenceThreshold;
		firePropertyChange(PROPERTY_PREFERENCE_THRESHOLD, oldVal, this.preferenceThreshold);		
	}	

	@Override
	public String getTypeLabel() {
		return "Outranking";
	}
	
	public OutrankingCriterion deepCopy() {
		return new OutrankingCriterion(name, ascending, indifferenceThreshold, preferenceThreshold);
	}

}
