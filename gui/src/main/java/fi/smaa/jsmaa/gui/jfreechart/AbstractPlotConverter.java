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
package fi.smaa.jsmaa.gui.jfreechart;

import org.jfree.data.category.CategoryDataset;

public abstract class AbstractPlotConverter<S extends CategoryDataset> implements PlotConverter {
	
	protected S dataset;

	protected AbstractPlotConverter(S s) {
		this.dataset = s;
	}

	public String getData() {
		String res = "Alt";
		for (int i=0;i<dataset.getRowCount();i++) {
			res += "\t\"" + dataset.getRowKey(i) + "\"";
		}			
		for (int i=0;i<dataset.getColumnCount();i++) {
			res += "\n";
			res += "\"" + dataset.getColumnKey(i) + "\"";
			for (int j=0;j<dataset.getRowCount();j++) {
				res += "\t" + dataset.getValue(j, i);
			}
		}
		return res;
	}
}
