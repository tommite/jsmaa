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

import org.jfree.data.category.CategoryDataset;

import fi.smaa.jsmaa.SMAAResults;

@SuppressWarnings("unchecked")
public abstract class AlternativeColumnCategoryDataset<R extends SMAAResults> extends SMAADataSet<R> implements CategoryDataset {

	protected AlternativeColumnCategoryDataset(R results) {
		super(results);
	}

	public int getColumnIndex(Comparable alt) {
		return results.getAlternatives().indexOf(alt);
	}

	public Comparable getColumnKey(int index) {
		if (index < 0 || index >= results.getAlternatives().size()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return results.getAlternatives().get(index);
	}

	public List getColumnKeys() {
		return results.getAlternatives();
	}

	public int getColumnCount() {
		return results.getAlternatives().size();
	}

}