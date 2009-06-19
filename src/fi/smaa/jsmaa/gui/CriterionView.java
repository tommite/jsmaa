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

package fi.smaa.jsmaa.gui;

import javax.swing.JComponent;

import nl.rug.escher.common.gui.LayoutUtil;
import nl.rug.escher.common.gui.ViewBuilder;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.jsmaa.common.Interval;
import fi.smaa.jsmaa.common.gui.IntervalFormat;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.CardinalMeasurement;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ImpactMatrix;
import fi.smaa.jsmaa.model.NoSuchValueException;

public class CriterionView implements ViewBuilder {
	private Criterion criterion;
	private ImpactMatrix matrix;
	
	public CriterionView(Criterion crit, ImpactMatrix matrix) {
		this.criterion = crit;
		this.matrix = matrix;
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
		try {
			buildMeasurementsPart(layout, fullWidth, builder, cc, row);
		} catch (NoSuchValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
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
			PanelBuilder builder, CellConstraints cc, int row) throws NoSuchValueException {
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
		for (Alternative a : matrix.getAlternatives()) {
			LayoutUtil.addRow(layout);
			row += 2;
			builder.add(BasicComponentFactory.createLabel(
					new PresentationModel<Alternative>(a).getModel(Alternative.PROPERTY_NAME)),
					cc.xy(1, row));
			if (criterion instanceof CardinalCriterion) {
				CardinalCriterion cardCrit = (CardinalCriterion) criterion;
				CardinalMeasurement m = matrix.getMeasurement(cardCrit, a);
				if (m instanceof Interval) {
					Interval ival = (Interval) m;
					JComponent comp = new IntervalPanel(null, new PresentationModel<Interval>(ival));
					builder.add(comp, cc.xy(3, row));
					builder.addLabel("Interval", cc.xy(5, row));
				}
			}
			index++;
		}
	}
}
