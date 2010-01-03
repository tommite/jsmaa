package fi.smaa.jsmaa.gui.views;

import javax.swing.JComponent;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.common.gui.LayoutUtil;
import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.components.MeasurementPanel;
import fi.smaa.jsmaa.gui.presentation.ImpactMatrixPresentationModel;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.OrdinalCriterion;

public class CardinalCriterionMeasurementsView implements ViewBuilder {
	
	private Criterion criterion;
	private ImpactMatrixPresentationModel model;

	public CardinalCriterionMeasurementsView(CardinalCriterion c, ImpactMatrixPresentationModel model) {
		this.model = model;
		this.criterion = c;
	}
	
	@Override
	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"right:pref, 3dlu, left:pref",
				"p" );
		
		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();
		
		for (int i=0;i<model.getAlternatives().size();i++) {
			Alternative a = model.getAlternatives().get(i);
			int row = 1 + (i*2);			
			if (i != 0) {
				LayoutUtil.addRow(layout);
			}
			builder.add(BasicComponentFactory.createLabel(
					new PresentationModel<Alternative>(a).getModel(Alternative.PROPERTY_NAME)),
					cc.xy(1, row));
			JComponent comp = null;
			if (criterion instanceof CardinalCriterion) {
				comp = new MeasurementPanel(model.getMeasurementHolder(a, criterion));
			} else if (criterion instanceof OrdinalCriterion) {
				
			}
			builder.add(comp, cc.xy(3, row));			
		}
		return builder.getPanel();
	}

}
