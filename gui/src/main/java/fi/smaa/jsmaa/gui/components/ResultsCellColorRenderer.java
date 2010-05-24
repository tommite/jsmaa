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
