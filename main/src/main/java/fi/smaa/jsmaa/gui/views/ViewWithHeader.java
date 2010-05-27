package fi.smaa.jsmaa.gui.views;

import javax.swing.JComponent;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.jsmaa.gui.ViewBuilder;

public class ViewWithHeader implements ViewBuilder {
	
	private ViewBuilder vbuilder;
	private String header;

	public ViewWithHeader(String header, ViewBuilder builder) {
		this.header = header;
		this.vbuilder = builder;
	}

	@Override
	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"fill:pref:grow",
				"p, 3dlu, p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator(header, cc.xy(1, 1));
		builder.add(vbuilder.buildPanel(), cc.xy(1, 3));
		return builder.getPanel();
	}
}
