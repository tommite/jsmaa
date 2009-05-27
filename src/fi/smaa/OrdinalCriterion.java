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

import java.util.Collection;



public class OrdinalCriterion extends Criterion<Rank> {
	
	public OrdinalCriterion(String name) {
		super(name);
	}

	private void ensureRanks() {
		ensureDifferentRanks();
		ensureNoRankGaps();		
	}

	private void ensureDifferentRanks() {
		int numRanks = measurements.size();
		
		for (int i=1;i<=numRanks;i++) {
			boolean found = false;
			for (Alternative a : getAlternatives()) {
				Rank r = measurements.get(a);
				if (r.getRank().equals(i)) {
					if (found) {
						r.setRank(r.getRank() + 1);
					} else {
						found = true;
					}
				}
			}
		}
	}
	
	private void ensureNoRankGaps() {
		int numRanks = measurements.size();
		
		for (int i=1;i<=numRanks;i++) {
			while (!measurements.containsValue(new Rank(i))) {
				shiftAllRanksDownFrom(i);
			}
		}
	}

	private void shiftAllRanksDownFrom(int i) {
		for (Alternative a : getAlternatives()) {
			Rank r = measurements.get(a);
			if (r.getRank() > i) {
				r.setRank(r.getRank()-1);
			}
		}
	}
		
	@Override
	public String getTypeLabel() {
		return "Ordinal";
	}

	private int findMax(Collection<Rank> values) {
		int max = 0;
		for (Rank i : values) {
			if (i.getRank() > max) {
				max = i.getRank();
			}
		}
		return max;
	}
	
	@Override
	protected Rank createMeasurement() {
		return new Rank(findMax(measurements.values()) + 1);
	}

	@Override
	protected void fireMeasurementChange() {
		ensureRanks();
	}

	public Object deepCopy() {
		OrdinalCriterion crit = new OrdinalCriterion(name);
		deepCopyAlternativesAndMeasurements(crit);
		return crit;
	}
}