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

package fi.smaa.jsmaa;

import java.util.List;
import java.util.Map;

import fi.smaa.jsmaa.model.Alternative;

public class SMAATRIResults extends SMAAResults {
	
	private Acceptabilities categoryAcceptabilities;
	private int numCategories;
		
	public SMAATRIResults(List<Alternative> alts, int numCategories, int updateInterval) {
		super(alts, updateInterval);
		this.numCategories = numCategories;
		reset();
	}

	public void reset() {
		categoryAcceptabilities = new Acceptabilities(alternatives, numCategories);		
	}
	
	public void update(Integer[] categories) {
		assert(categories.length == alternatives.size());
		
		for (int altIndex=0;altIndex<categories.length;altIndex++) {
			categoryAcceptabilities.hit(altIndex, categories[altIndex]);
		}
		
		if (getIteration() % updateInterval == 0) {
			fireResultsChanged();
		}
	}

	public Integer getIteration() {
		return new Integer(categoryAcceptabilities.getTotalHits(0));
	}	
	
	public Map<Alternative, List<Double>> getCategoryAcceptabilities() {
		return categoryAcceptabilities.getResults();
	}	
}
