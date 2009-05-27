/*
	This file is part of JSMAA.
	(c) Tommi Tervonen, 2009	

    JSMAA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSMAA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSMAA.  If not, see <http://www.gnu.org/licenses/>.
*/

package fi.smaa.gui;

import javax.swing.JComponent;

import nl.rug.escher.common.gui.LayoutUtil;
import nl.rug.escher.common.gui.ViewBuilder;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.Alternative;
import fi.smaa.CardinalCriterion;
import fi.smaa.Criterion;
import fi.smaa.GaussianCriterion;
import fi.smaa.GaussianMeasurement;
import fi.smaa.LogNormalCriterion;
import fi.smaa.Measurement;
import fi.smaa.OrdinalCriterion;
import fi.smaa.UniformCriterion;
import fi.smaa.common.Interval;
import fi.smaa.common.gui.IntervalFormat;

@SuppressWarnings("unchecked")
public class CriterionView implements ViewBuilder {
	private Criterion criterion;
	
	public CriterionView(Criterion crit) {
		this.criterion = crit;
	}


	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"right:pref, 3dlu, pref, 3dlu, pref",
				"p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p" );
		
		int fullWidth = 5;

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		PresentationModel<Criterion> pm = new PresentationModel<Criterion>(criterion);
		
		builder.addSeparator("Criterion", cc.xyw(1,1, fullWidth));
		builder.addLabel("Name:", cc.xy(1, 3));
		builder.add(BasicComponentFactory.createLabel(pm.getModel(Criterion.PROPERTY_NAME)),
				cc.xyw(3, 3, fullWidth-2)
				);
		builder.addLabel("Type:", cc.xy(1, 5));
		builder.add(BasicComponentFactory.createLabel(pm.getModel(Criterion.PROPERTY_TYPELABEL)),
				cc.xyw(3, 5, fullWidth-2)
		);
		
		int row = 5;
		row = buildScalePart(layout, builder, cc, row, fullWidth);
		buildMeasurementsPart(layout, fullWidth, builder, cc, row);
			
		return builder.getPanel();
	}

	private int buildScalePart(FormLayout layout, PanelBuilder builder,
			CellConstraints cc, int row, int fullWidth) {
		if (criterion instanceof CardinalCriterion) {
			LayoutUtil.addRow(layout);
			row += 2;
			CardinalCriterion cardCrit = (CardinalCriterion) criterion;
			PresentationModel<CardinalCriterion> pmc = new PresentationModel<CardinalCriterion>(cardCrit);
			builder.addLabel("Scale:", cc.xy(1, row));
			builder.add(BasicComponentFactory.createLabel(pmc.getModel(CardinalCriterion.PROPERTY_SCALE),
					new IntervalFormat()),
					cc.xyw(3, row, fullWidth - 2));
			LayoutUtil.addRow(layout);
			row += 2;
			builder.addLabel("Ascending:", cc.xy(1, row));
			builder.add(BasicComponentFactory.createCheckBox(
					pmc.getModel(CardinalCriterion.PROPERTY_ASCENDING), null),
					cc.xy(3, row)
					);						
		}
		return row;
	}

	private void buildMeasurementsPart(FormLayout layout, int fullWidth,
			PanelBuilder builder, CellConstraints cc, int row) {
		row += 2;
		builder.addSeparator("Measurements", cc.xyw(1, row, fullWidth));
		row += 2;
		builder.addLabel("Value", cc.xy(3, row, "center, center"));
		builder.addLabel("Type", cc.xy(5, row));
		
		
//		List<JComboBox> rankSelectors = null;		
//		if (criterion instanceof OrdinalCriterion) {
//			 rankSelectors = new RankSelectorGroup((OrdinalCriterion) criterion).getSelectors();
//		}
		
		int index = 0;
		for (Alternative a : ((Criterion<Measurement>)criterion).getAlternatives()) {
			LayoutUtil.addRow(layout);
			row += 2;
			builder.add(BasicComponentFactory.createLabel(
					new PresentationModel<Alternative>(a).getModel(Alternative.PROPERTY_NAME)),
					cc.xy(1, row));
			if (criterion instanceof UniformCriterion) {
				UniformCriterion cardCrit = (UniformCriterion) criterion;
				Interval ival = cardCrit.getMeasurements().get(a);
				JComponent comp = new IntervalPanel(null, new PresentationModel<Interval>(ival));
				builder.add(comp, cc.xy(3, row));
				builder.addLabel("Interval", cc.xy(5, row));
			} else if (criterion instanceof GaussianCriterion) {
				Criterion<GaussianMeasurement> cardCrit = (Criterion<GaussianMeasurement>) criterion;
				GaussianMeasurement meas = cardCrit.getMeasurements().get(a);
				JComponent comp = ComponentBuilder.createGaussianMeasurementPanel(
						new PresentationModel<GaussianMeasurement>(meas));
				builder.add(comp, cc.xy(3, row));
				String sLabel = criterion instanceof LogNormalCriterion ? 
						"LogNormal distributed value" : "Normal distributed value";
				builder.addLabel(sLabel, cc.xy(5, row));				
			} else if (criterion instanceof OrdinalCriterion) {
//				JComboBox box = rankSelectors.get(index);
//				builder.add(box, cc.xy(3, row));
//				builder.addLabel("Rank", cc.xy(5, row));								
			}
			index++;
		}
	}
}
