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
package fi.smaa.jsmaa.gui.components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

@SuppressWarnings("serial")
public class ResultsCellColorRenderer extends ResultsCellRenderer {
	
	public ResultsCellColorRenderer(Double maxValue) {
		super(maxValue);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if (value == null || value instanceof Double && value.equals(Double.NaN)) {
			setBackground(Color.white);			
		} else if (value instanceof Double) {
			setDoubleColor((Double) value);
		} else {
			setBackground(Color.white);		
		}
		
		return this;
    }

	protected void setDoubleColor(Double value) {
		Double share = value / maxValue;
		int red = 0;
		int green = 0;
		
		if (share < 0.5) {
			share = share * 2.0;
			green = (int) (255 * share);
			red = 255;
		} else {
			share = (share-0.5) * 2.0;
			red = (int) (255 - (share * 255));
			green = 255;
		}
		
		setBackground(new Color(red, green, 0));
	}
}
