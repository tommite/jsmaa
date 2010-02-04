/**
 * 
 */
package fi.smaa.jsmaa.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import fi.smaa.jsmaa.gui.presentation.SMAACEAImportDataType;

@SuppressWarnings("serial")
public class SMAACEAImportDataCellRenderer extends JComboBox implements TableCellRenderer {
	
	private DefaultTableCellRenderer defRenderer = new DefaultTableCellRenderer();
	
	public SMAACEAImportDataCellRenderer() {
		super(SMAACEAImportDataType.values());
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (row != 0) {
			return defRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
		}
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
		setSelectedItem(value);
		return this; 			
	}
}