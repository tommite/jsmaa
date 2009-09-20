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
import org.jfree.data.category.CategoryDataset;

import fi.smaa.jsmaa.simulator.SMAA2Results;

@SuppressWarnings("unchecked")
public class CentralWeightsDataset extends SMAADataSet<SMAA2Results> implements CategoryDataset{
	
	public CentralWeightsDataset(SMAA2Results res) {
		super(res);
	}

	public int getColumnIndex(Comparable crit) {
		return results.getCriteria().indexOf(crit);
	}

	public Comparable getColumnKey(int critIndex) {
		if (critIndex < 0 || critIndex >= results.getCriteria().size()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return results.getCriteria().get(critIndex);
	}

	public List getColumnKeys() {
		return results.getCriteria();
	}

	public int getRowIndex(Comparable alt) {
		return results.getAlternatives().indexOf(alt);
	}

	public Comparable getRowKey(int altIndex) {
		if (altIndex < 0 || altIndex >= results.getAlternatives().size()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return results.getAlternatives().get(altIndex);
	}

	public List getRowKeys() {
		return results.getAlternatives();
	}

	public Number getValue(Comparable alt, Comparable crit) {
		if (!results.getAlternatives().contains(alt)) {
			throw new UnknownKeyException("unknown alt");
		}
		if (!results.getCriteria().contains(crit)){ 
			throw new UnknownKeyException("unknown crit");
		}
		return results.getCentralWeightVectors().get(alt).get(crit);
	}

	public int getColumnCount() {
		return results.getCriteria().size();
	}

	public int getRowCount() {
		return results.getAlternatives().size();
	}

	public Number getValue(int altIndex, int critIndex) {
		return getValue(results.getAlternatives().get(altIndex), results.getCriteria().get(critIndex));
	}
}
