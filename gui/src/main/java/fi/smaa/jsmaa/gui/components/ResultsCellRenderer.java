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