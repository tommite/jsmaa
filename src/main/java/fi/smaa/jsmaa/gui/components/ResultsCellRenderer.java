package fi.smaa.jsmaa.gui.components;

import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

@SuppressWarnings("serial")
public class ResultsCellRenderer extends DefaultTableCellRenderer {
	
	public ResultsCellRenderer() {
		setHorizontalAlignment(SwingConstants.CENTER);		
	}

	@Override
    protected void setValue(Object value) {
		if (value == null) {
			setValue("NA");
		} else if (value instanceof Double && value.equals(Double.NaN)) {
			setValue("NA");
		} else {
			super.setValue(value);
		}
    }
}
