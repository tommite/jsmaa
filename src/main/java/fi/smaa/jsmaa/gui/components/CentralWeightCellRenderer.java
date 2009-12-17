package fi.smaa.jsmaa.gui.components;

import java.text.DecimalFormat;
import java.text.Format;

@SuppressWarnings("serial")
public class CentralWeightCellRenderer extends ResultsCellRenderer {
	
	@Override
    protected void setValue(Object value) {
		if (value instanceof Double) {
			if (value.equals(Double.NaN)) {
				setValue("NA");
			} else {
				Format decFormat = new DecimalFormat("##0.00");
				setValue(decFormat.format(value));
			}
		} else {
			super.setValue(value);
		}
    }

}
