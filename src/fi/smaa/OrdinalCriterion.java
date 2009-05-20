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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.smaa.common.RandomUtil;


public class OrdinalCriterion extends Criterion {
	private double[] tmparr;
	
	private Map<Alternative, Rank> ranks = new HashMap<Alternative, Rank>();
	
	public static final String PROPERTY_RANKS = "ranks";
	
	public OrdinalCriterion(String name) {
		super(name);
	}

	@Override
	public void sample(double[] target) {
		assert(getAlternatives().size() == target.length);
		
		RandomUtil.createSumToOneSorted(tmparr);
		
		for (int i=0;i<getAlternatives().size();i++) {
			Rank rank = ranks.get(getAlternatives().get(i));
			target[i] = tmparr[tmparr.length - rank.getRank()];
		}
	}
	
	public void setRanks(Map<Alternative, Rank> ranks) {
		Object oldVal = this.ranks;
		this.ranks = ranks;
		ensureRanks();
		firePropertyChange(PROPERTY_RANKS, oldVal, this.ranks);
	}
	
	private void ensureRanks() {
		int numRanks = ranks.size();
		for (int i=1;i<=numRanks;i++) {
			while (!ranks.containsValue(new Rank(i))) {
				shiftAllRanksDownFrom(i);
			}
		}		
	}

	private void shiftAllRanksDownFrom(int i) {
		for (Rank r : ranks.values()) {
			if (r.getRank() > i) {
				r.setRank(r.getRank()-1);
			}
		}
	}

	public Map<Alternative, Rank> getRanks() {
		return ranks;
	}
	
	@Override
	public void setAlternatives(List<Alternative> alternatives) throws NullPointerException {	
		super.setAlternatives(alternatives);
		tmparr = new double[alternatives.size()];
	}
	
	@Override
	public String getTypeLabel() {
		return "Ordinal";
	}

	@Override
	protected void updateMeasurements() {
		Map<Alternative, Rank> newMap = new HashMap<Alternative, Rank>();
		for (Alternative a : getAlternatives()) {
			if (getRanks().containsKey(a)) {
				newMap.put(a, getRanks().get(a));
			} else {
				newMap.put(a, new Rank(findMax(newMap.values()) + 1));
			}
		}
		setRanks(newMap);
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
	public String measurementsToString() {
		return ranks.toString();
	}
	
}