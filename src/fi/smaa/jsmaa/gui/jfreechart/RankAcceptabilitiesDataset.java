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

package fi.smaa.jsmaa.gui.jfreechart;

import java.util.ArrayList;
import java.util.List;

import org.jfree.data.UnknownKeyException;

import fi.smaa.jsmaa.SMAA2Results;

@SuppressWarnings("unchecked")
public class RankAcceptabilitiesDataset extends AlternativeColumnCategoryDataset<SMAA2Results> {
	
	private List<Rank> ranks = new ArrayList<Rank>();
	
	public static class Rank implements Comparable<Rank>{
		private int index;
		public Rank(int index) {
			this.index = index;	
		}
		public String toString() {
			return "Rank " + (index+1);
		}
		public int getIndex() {
			return index;
		}
		public int compareTo(Rank o) {
			return (new Integer(index).compareTo(new Integer(o.getIndex())));
		}
		public boolean equals(Object other) {
			if (!(other instanceof Rank)){ 
				return false;
			}
			Rank r = (Rank) other;
			return r.getIndex() == index;
		}
	}

	public RankAcceptabilitiesDataset(SMAA2Results results) {
		super(results);
		for (int i=0;i<results.getAlternatives().size();i++) {
			ranks.add(new Rank(i));
		}
	}

	public int getRowIndex(Comparable rank) {
		return ranks.indexOf(rank);
	}

	public Comparable getRowKey(int rankIndex) {
		if (rankIndex < 0 || rankIndex >= ranks.size()) {
			throw new ArrayIndexOutOfBoundsException();
		}				
		return ranks.get(rankIndex);
	}

	public List getRowKeys() {
		return ranks;
	}

	public Number getValue(Comparable rank, Comparable alt) {
		if (!ranks.contains(rank)) {
			throw new UnknownKeyException("unknown rank");
		}
		if (!results.getAlternatives().contains(alt)) {
			throw new UnknownKeyException("unknown alt");
		}
		List<Double> accs = results.getRankAcceptabilities().get(alt);
		return accs.get(((Rank) rank).getIndex());
	}

	public int getRowCount() {
		return ranks.size();
	}

	public Number getValue(int rankIndex, int altIndex) {
		return getValue(new Rank(rankIndex), results.getAlternatives().get(altIndex));
	}
}
