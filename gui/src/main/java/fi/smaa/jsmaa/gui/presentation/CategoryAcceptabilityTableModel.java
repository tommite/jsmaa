/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013.

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
package fi.smaa.jsmaa.gui.presentation;


import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.simulator.SMAATRIResults;

@SuppressWarnings("serial")
public class CategoryAcceptabilityTableModel extends SMAAResultsTableModel<SMAATRIResults> {
	
	public CategoryAcceptabilityTableModel(SMAATRIResults results) {
		super(results);		
	}
	
	@Override
	public void setResults(SMAATRIResults results) {
		if (this.results != null) {
			for (Alternative a : this.results.getCategories()) {
				a.removePropertyChangeListener(listener);
			}					
		}
		super.setResults(results);
	
		for (Alternative a : results.getCategories()) {
			a.addPropertyChangeListener(listener);
		}
	}

	public int getColumnCount() {
		return results.getCategories().size() + 1;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return results.getAlternatives().get(rowIndex);
		}
		Alternative alt = results.getAlternatives().get(rowIndex);
		return results.getCategoryAcceptabilities().get(alt).get(columnIndex-1);
	}
	
	@Override
	public String getColumnName(int column) {
		if (column == 0) {
			return "Alternative";
		} else {
			return results.getCategories().get(column-1).getName();
		}
	}
}
