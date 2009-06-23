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



public class CardinalCriterion extends AbstractCriterion {
	
	private static final long serialVersionUID = 306783908162696324L;
	public final static String PROPERTY_SCALE = "scale";
	public final static String PROPERTY_ASCENDING = "ascending";

	protected Boolean ascending;
	private Interval scale = new Interval(0.0, 0.0);
	
	public CardinalCriterion(String name) {
		super(name);
		ascending = true;
	}
		
	public CardinalCriterion(String name, Boolean ascending) {
		super(name);
		this.ascending = ascending;
	}

	public Interval getScale() {
		return scale;
	}
	
	public void setScale(Interval scale) {
		Interval oldVal = this.scale;
		this.scale = scale;
		firePropertyChange(PROPERTY_SCALE, oldVal, this.scale);
	}
	
	public Boolean getAscending() {
		return ascending;
	}
	
	public void setAscending(Boolean asc) {
		Boolean oldVal = this.ascending;
		this.ascending = asc;
		firePropertyChange(PROPERTY_ASCENDING, oldVal, asc);
	}

	@Override
	public String getTypeLabel() {
		return "Cardinal";
	}

	public CardinalCriterion deepCopy() {
		CardinalCriterion c = new CardinalCriterion(name, ascending);
		c.setScale(scale);
		return c;
	}
		
}
