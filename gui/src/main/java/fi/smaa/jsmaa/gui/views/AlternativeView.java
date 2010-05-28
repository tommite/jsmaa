package fi.smaa.jsmaa.gui.views;

import javax.swing.JComponent;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.jsmaa.gui.LayoutUtil;
import fi.smaa.jsmaa.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.components.MeasurementPanel;
import fi.smaa.jsmaa.gui.presentation.ImpactMatrixPresentationModel;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ImpactMatrix;
import fi.smaa.jsmaa.model.OrdinalCriterion;

public class AlternativeView implements ViewBuilder {
	
	private Alternative alt;
	private ImpactMatrix impactMatrix;

	public AlternativeView(Alternative a, ImpactMatrix m) {
		this.alt = a;
		this.impactMatrix =  m;
	}

	public JComponent buildPanel() {
		
		FormLayout layout = new FormLayout(
				"pref, 3dlu, left:pref:grow",
				"p, 3dlu, p, 3dlu, p" );

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		int fullwidth = 3;
		
		builder.addLabel("Name:", cc.xy(1, 1));
		builder.add(BasicComponentFactory.createLabel(new PresentationModel<Alternative>(alt).getModel(Alternative.PROPERTY_NAME)),
				cc.xy(3, 1));
		
		builder.addSeparator("Measurements", cc.xyw(1, 3, fullwidth));
		
		builder.add(buildMeasurementsPart(), cc.xyw(1, 5, fullwidth));

		return builder.getPanel();
	}

	private JComponent buildMeasurementsPart() {
		
		FormLayout layout = new FormLayout(
				"pref, 3dlu, left:pref:grow",
				"p" );

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addLabel("Criterion", cc.xy(1, 1));
		builder.addLabel("Measurement", cc.xy(3, 1));
		
		ImpactMatrixPresentationModel model = new ImpactMatrixPresentationModel(impactMatrix);

		for (int i=0;i<impactMatrix.getCriteria().size();i++) {
			LayoutUtil.addRow(layout);
			
			int row = 1 + (i+1) * 2;
			
			Criterion c = impactMatrix.getCriteria().get(i);
			
			builder.add(BasicComponentFactory.createLabel(new PresentationModel<Criterion>(c).getModel(Criterion.PROPERTY_NAME)),
					cc.xy(1, row));

			if (c instanceof OrdinalCriterion) {
				builder.addLabel("Ordinal criterion, set measurements in the criterion view", cc.xy(3, row));
			} else {
				builder.add(new MeasurementPanel(model.getMeasurementHolder(alt, c)), cc.xy(3, row));
			}
		}
		return builder.getPanel();
	}

}
