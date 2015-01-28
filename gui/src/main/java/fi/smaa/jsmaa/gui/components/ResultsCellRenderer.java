/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

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
package fi.smaa.jsmaa.gui.components;

import java.awt.Color;
import java.awt.Component;
import java.text.DecimalFormat;
import java.text.Format;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class ResultsCellRenderer extends DefaultTableCellRenderer {

	protected Double maxValue;

	public ResultsCellRenderer(Double maxValue) {
		setHorizontalAlignment(SwingConstants.CENTER);
		this.maxValue = maxValue;
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		if (value == null || value instanceof Double && value.equals(Double.NaN)) {
			setValue("NA");
		} else if (value instanceof Double) {
			setDoubleValue((Double)value);
		} else {
			super.setValue(value);
		}
		
		if (isSelected) {
			setBorder(BorderFactory.createLineBorder(Color.black, 1));
		} else {
			setBorder(BorderFactory.createEmptyBorder());
		}
		
		return this;
    }
	
	private void setDoubleValue(Double value) {
		Format decFormat = new DecimalFormat("##0.00");
		setValue(decFormat.format(value));
	}

}