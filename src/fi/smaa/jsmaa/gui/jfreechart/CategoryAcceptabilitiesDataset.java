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

import java.util.List;

import org.jfree.data.UnknownKeyException;

import fi.smaa.jsmaa.SMAATRIResults;

@SuppressWarnings("unchecked")
public class CategoryAcceptabilitiesDataset extends AlternativeColumnCategoryDataset<SMAATRIResults> {
	
	// categories = alternatives
	// series = categoryacceptabilities
	
	public CategoryAcceptabilitiesDataset(SMAATRIResults results) {
		super(results);
	}

	public int getRowIndex(Comparable cat) {
		return results.getCategories().indexOf(cat);
	}

	public Comparable getRowKey(int index) {
		if (index < 0 || index >= results.getCategories().size()) {
			throw new ArrayIndexOutOfBoundsException();
		}		
		return results.getCategories().get(index);
	}

	public List getRowKeys() {
		return results.getCategories();
	}

	public Number getValue(Comparable cat, Comparable alt) {
		if (!results.getAlternatives().contains(alt)) {
			throw new UnknownKeyException("Unknown alternative");
		}
		if (!results.getCategories().contains(cat)) {
			throw new UnknownKeyException("Unknown category");
		}
		List<Double> accs = results.getCategoryAcceptabilities().get(alt);
		return accs.get(results.getCategories().indexOf(cat));
	}

	public int getRowCount() {
		return results.getCategories().size();
	}

	public Number getValue(int catIndex, int altIndex) {
		return getValue(results.getCategories().get(catIndex), results.getAlternatives().get(altIndex));
	}
}
