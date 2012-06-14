/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.

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
import java.util.Map;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Category;

public class SMAATRIResults extends SMAAResults {
	
	private Acceptabilities categoryAcceptabilities;
	private List<Category> categories;
		
	public SMAATRIResults(List<Alternative> alts, List<Category> categories, int updateInterval) {
		super(alts, updateInterval);
		this.categories = categories;
		reset();
	}

	public void reset() {
		categoryAcceptabilities = new Acceptabilities(alternatives, categories.size());		
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
	
	public List<Category> getCategories() {
		return categories;
	}
}
