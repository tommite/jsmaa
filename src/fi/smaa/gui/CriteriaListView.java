package fi.smaa.gui;

import javax.swing.JComponent;

import nl.rug.escher.common.gui.LayoutUtil;
import nl.rug.escher.common.gui.ViewBuilder;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.CardinalCriterion;
import fi.smaa.Criterion;
import fi.smaa.SMAAModel;
import fi.smaa.common.gui.IntervalFormat;

public class CriteriaListView implements ViewBuilder {
	
	private SMAAModel model;
	
	public CriteriaListView(SMAAModel model) {
		this.model = model;
	}

	public JComponent buildPanel() {
		
		FormLayout layout = new FormLayout(
				"pref, 3dlu, pref, 3dlu, center:pref, 3dlu, pref",
				"p, 3dlu, p" );
		
		int fullWidth = 7;

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("Criteria", cc.xyw(1, 1, fullWidth));
		builder.addLabel("Name", cc.xy(1, 3));
		builder.addLabel("Type", cc.xy(3, 3));
		builder.addLabel("Scale", cc.xy(5, 3));
		builder.addLabel("Ascending", cc.xy(7, 3));

		int row = 5;

		for (Criterion c : model.getCriteria()) {
			LayoutUtil.addRow(layout);
			
			PresentationModel<Criterion> pm = new PresentationModel<Criterion>(c);
			builder.add(BasicComponentFactory.createLabel(
					pm.getModel(Criterion.PROPERTY_NAME)),
					cc.xy(1, row)
					);

			builder.add(BasicComponentFactory.createLabel(
					pm.getModel(Criterion.PROPERTY_TYPELABEL)),
					cc.xy(3, row)
					);
			if (c instanceof CardinalCriterion) {
				CardinalCriterion cardCrit = (CardinalCriterion) c;
				PresentationModel<CardinalCriterion> cpm = new PresentationModel<CardinalCriterion>(cardCrit);
				builder.add(BasicComponentFactory.createLabel(
						cpm.getModel(CardinalCriterion.PROPERTY_SCALE),
						new IntervalFormat()),
						cc.xy(5, row)
						);
				builder.add(BasicComponentFactory.createCheckBox(
						cpm.getModel(CardinalCriterion.PROPERTY_ASCENDING), null),
						cc.xy(7, row)
						);
			}
	
			row += 2;
		}
		
		return builder.getPanel();
	}

}
