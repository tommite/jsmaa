package fi.smaa.jsmaa.gui.components;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;

@SuppressWarnings("serial")
public class CentralWeightsCellRenderer extends ResultsCellColorRenderer {

	public CentralWeightsCellRenderer(Double maxValue) {
		super(maxValue);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		
		if (column != 1) {
			setBackground(Color.white);
		}
		
		return this;
    }
}
