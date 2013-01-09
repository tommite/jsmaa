/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013.

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

import java.util.List;

import fi.smaa.jsmaa.model.Alternative;

public class Acceptabilities extends ResultsMap {
	
	protected int[][] hits;	

	public Acceptabilities(List<Alternative> alternatives, int size) {
		super(alternatives, size);
		hits = new int[alternatives.size()][size];		
	}

	public void computeResults() {
		calculateAcceptabilities();
	}
		
	private void calculateAcceptabilities() {
		for (Integer altIndex : results.keySet()) {
			List<Double> vec = results.get(altIndex);
			for (int i=0;i<vec.size();i++) {
				vec.set(i, calculateAcceptability(altIndex, i));
			}
		}
	}

	private double calculateAcceptability(Integer altIndex, int rank) {
		int totalIter = 0;
		for (int i=0;i<hits[altIndex].length;i++) {
			totalIter += hits[altIndex][i];
		}
		return (double) hits[altIndex][rank] / (double) totalIter;
	}	
	
	public void hit(int altIndex, int resIndex) {
		hits[altIndex][resIndex]++;
	}	
	
	
	public int getTotalHits(int altIndex) {
		int total = 0;
		if (hits.length < 1) {
			return 0;
		}
		for (int i=0;i<hits[0].length;i++) {
			total += hits[0][i];
		}
		return total;
	}	
	
	public int getHits(int altIndex, int resIndex) {
		return hits[altIndex][resIndex];
	}	

}
