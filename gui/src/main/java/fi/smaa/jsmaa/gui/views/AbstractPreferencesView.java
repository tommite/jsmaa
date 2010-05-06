package fi.smaa.jsmaa.gui.views;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.jsmaa.gui.LayoutUtil;
import fi.smaa.jsmaa.gui.components.FocusTransferrer;
import fi.smaa.jsmaa.model.AbstractPreferenceInformation;
import fi.smaa.jsmaa.model.Criterion;

public abstract class AbstractPreferencesView<T extends AbstractPreferenceInformation<?>> {

	protected T prefs;

	public AbstractPreferencesView(T prefs) {
		this.prefs = prefs;
	}
	
	protected abstract JComponent getPreferenceComponent(Criterion c, int i);
	protected abstract String getTypeName();

	public JComponent buildPanel() {		
		FormLayout layout = new FormLayout(
				"right:pref, 3dlu, center:pref",
				"p, 3dlu, p" );
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addLabel("Criterion", cc.xy(1, 1));
		builder.addLabel(getTypeName(), cc.xy(3, 1));
		
		int row = 3;
		int i=0;
		JComponent firstComp = null;
		for (Criterion c : prefs.getCriteria()) {
			row += 2;
			LayoutUtil.addRow(layout);
			JLabel label = new JLabel();
			Bindings.bind(label, "text",
					new PresentationModel<Criterion>(c).getModel(
							Criterion.PROPERTY_NAME)
			);
			builder.add(label, cc.xy(1, row));
			JComponent measurementComp = getPreferenceComponent(c, i);
			builder.add(measurementComp, cc.xy(3, row));
			if (firstComp == null) {
				firstComp = measurementComp;
			}			
			i++;
		}
		
		JPanel panel = builder.getPanel();
		if (firstComp != null) {
			panel.addFocusListener(new FocusTransferrer(firstComp));
		}		
		return panel;
	}
}