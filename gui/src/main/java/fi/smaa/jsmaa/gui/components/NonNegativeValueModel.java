package fi.smaa.jsmaa.gui.components;

import javax.swing.JComponent;

import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.gui.presentation.MultiVetoableValueModel;

@SuppressWarnings("serial")
public class NonNegativeValueModel extends MultiVetoableValueModel {
	
	private String typeLabel;

	public NonNegativeValueModel(JComponent parent, ValueModel subject, String typeLabel) {
		super(parent, subject);
		this.typeLabel = typeLabel;
	}
	
	@Override
	public boolean proposedChange(Object oldVal, Object newVal) {
		if ((Double) newVal < 0.0) {
			errorMessage(typeLabel + " cannot be negative");
			return false;
		}
		return super.proposedChange(oldVal, newVal);
	}
}
