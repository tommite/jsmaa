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

public abstract class DirectedCriterion extends AbstractCriterion {

	private static final long serialVersionUID = -1677119574393102051L;
	
	public static final String PROPERTY_ASCENDING = "ascending";
	protected Boolean ascending;

	public DirectedCriterion(String name, boolean ascending) {
		super(name);
		this.ascending = ascending;
	}

	public Boolean getAscending() {
		return ascending;
	}

	public void setAscending(Boolean asc) {
		Boolean oldVal = this.ascending;
		this.ascending = asc;
		firePropertyChange(PROPERTY_ASCENDING, oldVal, asc);
	}

}