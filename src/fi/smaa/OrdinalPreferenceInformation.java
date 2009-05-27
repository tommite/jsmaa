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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import fi.smaa.common.RandomUtil;

public class OrdinalPreferenceInformation implements PreferenceInformation, Serializable {
	
	transient private double[] tmparr;
	transient private double[] samplearr;
	private List<Rank> ranks;
	
	private RankListener rankListener = new RankListener();
	private List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();
	
	public OrdinalPreferenceInformation(List<Rank> ranks) {
		this.ranks = ranks;
		for (Rank r : ranks) {
			r.addPropertyChangeListener(rankListener);
		}
	}
		
	public List<Rank> getRanks() {
		return ranks;
	}
	
	private void initArrays() {
		if (tmparr == null) {
			tmparr = new double[ranks.size()];
		}
		if (samplearr == null) {
			samplearr = new double[ranks.size()];			
		}
	}
	
	public double[] sampleWeights() {
		initArrays();
		RandomUtil.createSumToOneSorted(tmparr);
		
		for (int i=0;i<samplearr.length;i++) {
			Rank r = ranks.get(i);
			samplearr[i] = tmparr[tmparr.length - r.getRank()];
		}
		return samplearr;
	}
	
	@Override
	public String toString() {
		return ranks.toString();
	}

	public Object deepCopy() {
		List<Rank> myranks = new ArrayList<Rank>();
		for (Rank r : ranks) {
			myranks.add((Rank) r.deepCopy());
		}
		return new OrdinalPreferenceInformation(myranks);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		if (!listeners.contains(l)) {
			listeners.add(l);
		}
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.remove(l);
	}
	
	private class RankListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			for (PropertyChangeListener l : listeners) {
				l.propertyChange(evt);
			}
		}		
	}	
}
