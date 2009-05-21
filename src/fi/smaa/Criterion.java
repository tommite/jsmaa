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

package fi.smaa;

import java.util.ArrayList;
import java.util.List;

import com.jgoodies.binding.beans.Model;

public abstract class Criterion extends Model {
	
	public final static String PROPERTY_NAME = "name";	
	public final static String PROPERTY_TYPELABEL = "typeLabel";
	public final static String PROPERTY_ALTERNATIVES = "alternatives";
	
	protected String name;
	protected List<Alternative> alternatives = new ArrayList<Alternative>();

	protected Criterion(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public abstract void sample(double[] target);
	
	public void setName(String name) {
		Object oldVal = this.name;
		this.name = name;
		firePropertyChange(PROPERTY_NAME, oldVal, name);		
	}

	public String getName() {
		return name;
	}

	public List<Alternative> getAlternatives() {
		return alternatives;
	}
	
	
	/**
	 * 
	 * @param alternatives
	 * @throws NullPointerException if alternatives == null
	 */
	public void setAlternatives(List<Alternative> alternatives) throws NullPointerException {
		if (alternatives == null) {
			throw new NullPointerException();
		}
		Object oldVal = this.alternatives;
		this.alternatives = alternatives;
		firePropertyChange(PROPERTY_ALTERNATIVES, oldVal, this.alternatives);
		updateMeasurements();
	}
	
	protected abstract void updateMeasurements();
	
	public abstract String getTypeLabel();
	
	public abstract String measurementsToString();
	
	public boolean deepEquals(Criterion other) {
		if (!name.equals(other.name)) {
			return false;
		}
		if (alternatives.size() != other.alternatives.size()) {
			return false;
		}
		for (int i=0;i<alternatives.size();i++) {
			if (!alternatives.get(i).deepEquals(other.alternatives.get(i))) {
				return false;
			}
		}
		return true;	
	}
	
}