package fi.smaa.jsmaa.gui.components;

import javax.swing.JComponent;

import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.gui.presentation.MultiVetoableValueModel;
import fi.smaa.jsmaa.model.BetaMeasurement;

@SuppressWarnings("serial")
public class BetaMinMaxValueModel extends MultiVetoableValueModel{
	protected boolean start;
	protected BetaMeasurement meas;
	
	public BetaMinMaxValueModel(JComponent parent, BetaMeasurement meas, ValueModel subject, boolean start) {
		super(parent, subject);
		this.start = start;
		this.meas = meas;
	}

	@Override
	public boolean proposedChange(Object oldVal, Object newVal) {
		if (start) {
			if ((Double) newVal > meas.getMax()) {
				errorMessage("Beta min start has to be smaller than max");
				return false;
			}
		} else { // end
			if ((Double) newVal < meas.getMin()) {
				errorMessage("Beta max has to be larger than min");
				return false;
			}
		}
		return super.proposedChange(oldVal, newVal);
	}
}
