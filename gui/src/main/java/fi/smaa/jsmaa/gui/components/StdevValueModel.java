package fi.smaa.jsmaa.gui.components;

import javax.swing.JComponent;

import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.gui.presentation.MultiVetoableValueModel;

@SuppressWarnings("serial")
public class StdevValueModel extends MultiVetoableValueModel {
	
	public StdevValueModel(JComponent parent, ValueModel subject) {
		super(parent, subject);
	}
	
	@Override
	public boolean proposedChange(Object oldVal, Object newVal) {
		if ((Double) newVal < 0.0) {
			errorMessage("Standard deviation cannot be negative");
			return false;
		}
		return super.proposedChange(oldVal, newVal);
	}
}
