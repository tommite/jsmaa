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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("serial")
public abstract class PreferenceInformation implements DeepCopiable<PreferenceInformation>, Serializable {
	
	protected List<PreferenceListener> listeners = new ArrayList<PreferenceListener>();
		
	public abstract double[] sampleWeights() throws WeightGenerationException;
	
	public void addPreferenceListener(PreferenceListener list) {
		listeners.add(list);
	}
	
	public void removePreferenceListener(PreferenceListener list) {
		listeners.remove(list);
	}
	
	protected void firePreferencesChanged() {
		for (PreferenceListener l : listeners) {
			l.preferencesChanged();
		}
	}
}
