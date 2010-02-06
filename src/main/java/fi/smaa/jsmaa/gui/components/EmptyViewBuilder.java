package fi.smaa.jsmaa.gui.components;

import javax.swing.JComponent;
import javax.swing.JPanel;

import fi.smaa.common.gui.ViewBuilder;

public class EmptyViewBuilder implements ViewBuilder {

	@Override
	public JComponent buildPanel() {
		return new JPanel();
	}

}
