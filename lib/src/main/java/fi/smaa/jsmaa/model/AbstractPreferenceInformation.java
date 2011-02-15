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
import java.util.List;

public abstract class AbstractPreferenceInformation<T extends Measurement> extends AbstractEntity implements PreferenceInformation {

	private static final long serialVersionUID = -7094213141120934341L;
	protected transient MeasurementListener measListener = new MeasurementListener();
	protected List<Criterion> criteria;
	public static final String PREFERENCES = "preferences";	

	protected AbstractPreferenceInformation(List<Criterion> criteria) {
		this.criteria = criteria;
	}
	
	public abstract T getMeasurement(Criterion c);
	
	public List<Criterion> getCriteria() {
		return criteria;
	}	
	
	private void readObject(ObjectInputStream i) throws IOException, ClassNotFoundException {
		i.defaultReadObject();
		measListener = new MeasurementListener();
	}

	protected void firePreferencesChanged() {
		firePropertyChange(PREFERENCES, null, null);
	}	

	private class MeasurementListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			firePreferencesChanged();
		}		
	}
}