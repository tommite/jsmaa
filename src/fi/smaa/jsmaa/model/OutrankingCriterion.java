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


public class OutrankingCriterion extends DirectedCriterion {

	private static final long serialVersionUID = 2226047865113684859L;
	
	private double indifferenceThreshold;
	private double preferenceThreshold;

	protected OutrankingCriterion(String name, boolean ascending, double indifferenceThreshold,
			double preferenceThreshold) {
		super(name, ascending);
		this.indifferenceThreshold = indifferenceThreshold;
		this.preferenceThreshold = preferenceThreshold;
	}

	@Override
	public String getTypeLabel() {
		return "Outranking";
	}

	public OutrankingCriterion deepCopy() {
		// TODO Auto-generated method stub
		return null;
	}

}
