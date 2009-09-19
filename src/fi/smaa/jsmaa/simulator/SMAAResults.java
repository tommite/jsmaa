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

package fi.smaa.jsmaa.simulator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import fi.smaa.jsmaa.model.Alternative;

public abstract class SMAAResults {

	protected ConcurrentLinkedQueue<SMAAResultsListener> listeners = new ConcurrentLinkedQueue<SMAAResultsListener>();
	protected List<Alternative> alternatives;
	protected int updateInterval;
	
	protected SMAAResults(List<Alternative> alts, int updateInterval) {
		this.alternatives = alts;
		this.updateInterval = updateInterval;
	}
	
	public abstract void reset();

	public void addResultsListener(SMAAResultsListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(listener);
		}
	}
	
	public void removeResultsListener(SMAAResultsListener listener) {
		listeners.remove(listener);
	}

	protected void fireResultsChanged() {
		for (SMAAResultsListener listener : listeners) {
			listener.resultsChanged();
		}
	}
	
	public List<Alternative> getAlternatives() {
		return alternatives;
	}

	protected Map<Alternative, List<Double>> transformMap(Map<Integer, List<Double>> map) {
		Map<Alternative, List<Double>> cw = new HashMap<Alternative, List<Double>>();		
		for (Integer index : map.keySet()) {
			cw.put(alternatives.get(index), map.get(index));
		}
		return cw;
	}

	protected void fireResultsChanged(IterationException e) {
		for (SMAAResultsListener listener : listeners) {
			listener.resultsChanged(e);
		}		
	}
	
}