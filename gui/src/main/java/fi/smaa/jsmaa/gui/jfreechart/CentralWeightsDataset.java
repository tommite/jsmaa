/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

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
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jfree.data.UnknownKeyException;
import org.jfree.data.category.CategoryDataset;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.simulator.SMAA2Results;

@SuppressWarnings("rawtypes")
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
		return getAlternativesWithCentralWeights().indexOf(alt);
	}

	private List<Alternative> getAlternativesWithCentralWeights() {
		List<Alternative> alts = new ArrayList<Alternative>();
		for (Alternative a : results.getAlternatives()) {
			Map<Criterion, Double> cws = results.getCentralWeightVectors().get(a);
			Set<Entry<Criterion, Double>> entrySet = cws.entrySet();
			if (!entrySet.isEmpty() && !entrySet.iterator().next().getValue().equals(Double.NaN)) {
				alts.add(a);
			}
		}
		return alts;
	}

	public Comparable getRowKey(int altIndex) {
		List<Alternative> alts = getAlternativesWithCentralWeights();
		if (altIndex < 0 || altIndex >= alts.size()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return alts.get(altIndex);
	}

	public List getRowKeys() {
		return getAlternativesWithCentralWeights();
	}

	public Number getValue(Comparable alt, Comparable crit) {
		if (!getAlternativesWithCentralWeights().contains(alt)) {
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
		return getAlternativesWithCentralWeights().size();
	}

	public Number getValue(int altIndex, int critIndex) {
		return getValue(getAlternativesWithCentralWeights().get(altIndex), results.getCriteria().get(critIndex));
	}
}
