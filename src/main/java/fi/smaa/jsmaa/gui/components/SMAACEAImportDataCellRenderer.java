/**
 * 
 */
package fi.smaa.jsmaa.gui.components;

import java.awt.Component;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import fi.smaa.jsmaa.model.cea.SMAACEAModelImporter.Type;

@SuppressWarnings("serial")
public class SMAACEAImportDataCellRenderer extends JComboBox implements TableCellRenderer {
	
	private DefaultTableCellRenderer defRenderer = new DefaultTableCellRenderer();
	
	public SMAACEAImportDataCellRenderer(List<Type> list) {
		super(list.toArray());
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if (row != 0) {
			return defRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column); 
		}
		setSelectedItem(value);
		return this; 			
	}
}