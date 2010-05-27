package fi.smaa.jsmaa.gui.views;

import javax.swing.JComponent;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.jsmaa.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.presentation.PreferencePresentationModel;

public class PreferenceInformationViewWithHeader implements ViewBuilder {
	
	private PreferencePresentationModel model;

	public PreferenceInformationViewWithHeader(PreferencePresentationModel model) {
		this.model = model;
	}

	@Override
	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"fill:pref:grow",
				"p, 3dlu, p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("Preferences", cc.xy(1, 1));
		builder.add(new PreferenceInformationView(model).buildPanel(), cc.xy(1, 3));
		return builder.getPanel();
	}
}
