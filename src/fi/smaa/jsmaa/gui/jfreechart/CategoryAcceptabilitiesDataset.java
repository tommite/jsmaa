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
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.general.DatasetGroup;

import fi.smaa.jsmaa.SMAAResultsListener;
import fi.smaa.jsmaa.SMAATRIResults;

@SuppressWarnings("unchecked")
public class CategoryAcceptabilitiesDataset implements CategoryDataset, SMAAResultsListener {
	
	// categories = alternatives
	// series = categoryacceptabilities
	
	private SMAATRIResults results;
	private List<DatasetChangeListener> dataListeners = new ArrayList<DatasetChangeListener>();
	private DatasetGroup group;

	public CategoryAcceptabilitiesDataset(SMAATRIResults results) {
		this.results = results;
		results.addResultsListener(this);
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

	public int getColumnCount() {
		return results.getAlternatives().size();
	}

	public int getRowCount() {
		return results.getCategories().size();
	}

	public Number getValue(int catIndex, int altIndex) {
		return getValue(results.getCategories().get(catIndex), results.getAlternatives().get(altIndex));
	}

	public void addChangeListener(DatasetChangeListener l) {
		dataListeners.add(l);
	}

	public DatasetGroup getGroup() {
		return group;
	}

	public void removeChangeListener(DatasetChangeListener l) {
		dataListeners.remove(l);
	}

	public void setGroup(DatasetGroup g) {
		group = g;
	}

	public void resultsChanged() {
		for (DatasetChangeListener l : dataListeners) {
			l.datasetChanged(new DatasetChangeEvent(this, this));
		}
	}

	public void resultsChanged(Exception e) {
		// TODO
	}
}
