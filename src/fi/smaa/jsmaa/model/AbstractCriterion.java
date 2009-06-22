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

import com.jgoodies.binding.beans.Model;

public abstract class AbstractCriterion extends Model implements Criterion {
	
	private static final long serialVersionUID = 6926477036734383519L;
	protected String name;

	public abstract String getTypeLabel();
	
	protected AbstractCriterion(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public void setName(String name) {
		Object oldVal = this.name;
		this.name = name;
		firePropertyChange(PROPERTY_NAME, oldVal, name);		
	}

	public String getName() {
		return name;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Criterion)) {
			return false;
		}
		Criterion c = (Criterion) other;
		return name.equals(c.getName());
	}
		
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	public int compareTo(Criterion o) {
		return name.compareTo(o.getName());
	}
}