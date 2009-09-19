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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.smaa.jsmaa.model.Alternative;

public abstract class ResultsMap {

	protected List<Alternative> alternatives;
	protected Map<Integer, List<Double>> results;	
	protected int size;

	public ResultsMap(List<Alternative> alternatives, int size) {
		this.alternatives = alternatives;
		this.size = size;
		results = createAlternativeMapWithNans(alternatives, size);
	}
	
	public Map<Alternative, List<Double>> getResults() {
		computeResults();
		return transformMap(results);
	}
	
	protected abstract void computeResults();
	
	private static HashMap<Integer, List<Double>> createAlternativeMapWithNans(
			List<Alternative> alternatives,	int size) {
		Double[] vals = new Double[size];
		Arrays.fill(vals, Double.NaN);
		HashMap<Integer, List<Double>> map = new HashMap<Integer, List<Double>>();		
		for (int index=0;index<alternatives.size();index++) {
			ArrayList<Double> vec = new ArrayList<Double>(Arrays.asList(vals));
			map.put(index, vec);
		}
		return map;
	}
	
	private Map<Alternative, List<Double>> transformMap(Map<Integer, List<Double>> map) {
		Map<Alternative, List<Double>> cw = new HashMap<Alternative, List<Double>>();		
		for (Integer index : map.keySet()) {
			cw.put(alternatives.get(index), map.get(index));
		}
		return cw;
	}

	public int getLength() {
		return size;
	}
		
}
