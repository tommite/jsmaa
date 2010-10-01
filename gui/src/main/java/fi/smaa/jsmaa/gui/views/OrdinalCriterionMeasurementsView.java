package fi.smaa.jsmaa.gui.views;

import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.drugis.common.gui.LayoutUtil;
import org.drugis.common.gui.ViewBuilder;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.jsmaa.gui.presentation.OrdinalCriterionMeasurementsPM;

public class OrdinalCriterionMeasurementsView implements ViewBuilder {

	private OrdinalCriterionMeasurementsPM model;

	public OrdinalCriterionMeasurementsView(OrdinalCriterionMeasurementsPM model) {
		this.model = model;
	}

	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"right:pref, 3dlu, left:pref",
		"p" );

		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();
		
		List<ValueModel> altNames = model.getNameModels();
		List<JComboBox> selectors = model.getSelectors();

		for (int i=0;i<altNames.size();i++) {
			int row = 1 + (i*2);			
			if (i != 0) {
				LayoutUtil.addRow(layout);
			}
			builder.add(BasicComponentFactory.createLabel(altNames.get(i)), cc.xy(1, row));
			builder.add(selectors.get(i), cc.xy(3, row));			
		}
		return builder.getPanel();
	}
}
