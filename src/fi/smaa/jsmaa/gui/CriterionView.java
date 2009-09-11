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
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatter;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.common.gui.IntervalFormat;
import fi.smaa.common.gui.LayoutUtil;
import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.CardinalMeasurement;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class CriterionView implements ViewBuilder {
	protected Criterion criterion;
	protected SMAAModel model;
	
	public CriterionView(Criterion crit, SMAAModel model) {
		this.criterion = crit;
		this.model = model;
	}


	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"right:pref, 3dlu, pref, 3dlu, left:pref:grow",
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
		
		if (criterion instanceof ScaleCriterion) {
			row = buildScalePart(layout, builder, cc, row, fullWidth);
		}
		if (criterion instanceof CardinalCriterion) {
			row = buildAscendingPart(layout, builder, cc, row, fullWidth);
		}
		if (criterion instanceof OutrankingCriterion) {
			row = buildThresholdsPart(layout, builder, cc, row, fullWidth);			
		}		
		buildMeasurementsPart(layout, fullWidth, builder, cc, row);
			
		return builder.getPanel();
	}

	private int buildThresholdsPart(FormLayout layout, PanelBuilder builder,
			CellConstraints cc, int row, int fullWidth) {
		LayoutUtil.addRow(layout);
		row += 2;
		OutrankingCriterion outrCrit = (OutrankingCriterion) criterion;
		builder.addSeparator("Thresholds", cc.xyw(1, row, fullWidth));

		PresentationModel<Interval> indifModel = new PresentationModel<Interval>((Interval) outrCrit.getIndifMeasurement());
		PresentationModel<Interval> prefModel = new PresentationModel<Interval>((Interval) outrCrit.getPrefMeasurement());
		String dominatorText = "Preference threshold must dominate indifference threshold (be equal or larger than)";

		ValueModel indifStartModel = new IntervalValueModel(null, indifModel.getBean(), 
				indifModel.getModel(Interval.PROPERTY_START), true);
		IntervalValueModel indifEndModel = new IntervalValueModel(null, indifModel.getBean(), 
				indifModel.getModel(Interval.PROPERTY_END), false);
		
		indifEndModel.addVetoer(new RangeVetoer(prefModel.getBean(), dominatorText, false));
		
		IntervalValueModel prefStartModel = new IntervalValueModel(null, prefModel.getBean(), 
				prefModel.getModel(Interval.PROPERTY_START), true);
		ValueModel prefEndModel = new IntervalValueModel(null, prefModel.getBean(), 
				prefModel.getModel(Interval.PROPERTY_END), false);
		
		prefStartModel.addVetoer(new RangeVetoer(indifModel.getBean(), dominatorText, true));

		IntervalPanel indifPanel = new IntervalPanel(indifStartModel, indifEndModel);
		IntervalPanel prefPanel = new IntervalPanel(prefStartModel, prefEndModel);
		
		LayoutUtil.addRow(layout);
		row += 2;
		builder.addLabel("Indifference:", cc.xy(1, row));
		builder.add(indifPanel, cc.xy(3, row));				
		builder.addLabel("Interval", cc.xy(5, row));
				
		LayoutUtil.addRow(layout);
		row += 2;
		builder.addLabel("Preference:", cc.xy(1, row));		
		builder.add(prefPanel, cc.xy(3, row));				
		builder.addLabel("Interval", cc.xy(5, row));
		
		return row;
	}

	private int buildScalePart(FormLayout layout, PanelBuilder builder,
			CellConstraints cc, int row, int fullWidth) {
		LayoutUtil.addRow(layout);
		row += 2;
		ScaleCriterion cardCrit = (ScaleCriterion) criterion;
		PresentationModel<ScaleCriterion> pmc = new PresentationModel<ScaleCriterion>(cardCrit);
		builder.addLabel("Scale:", cc.xy(1, row));
		builder.add(BasicComponentFactory.createLabel(pmc.getModel(ScaleCriterion.PROPERTY_SCALE),
				new IntervalFormat()),
				cc.xyw(3, row, fullWidth - 2));
		return row;
	}


	private int buildAscendingPart(FormLayout layout, PanelBuilder builder,
			CellConstraints cc, int row, int fullWidth) {
		CardinalCriterion cardCrit = (CardinalCriterion) criterion;
		PresentationModel<CardinalCriterion> pmc = new PresentationModel<CardinalCriterion>(cardCrit);

		LayoutUtil.addRow(layout);
		row += 2;
		builder.addLabel("Ascending:", cc.xy(1, row));
		builder.add(BasicComponentFactory.createCheckBox(
				pmc.getModel(ScaleCriterion.PROPERTY_ASCENDING), null),
				cc.xy(3, row)
		);
		return row;
	}

	protected int buildMeasurementsPart(FormLayout layout, int fullWidth,
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
		for (Alternative a : model.getAlternatives()) {
			LayoutUtil.addRow(layout);
			row += 2;
			builder.add(BasicComponentFactory.createLabel(
					new PresentationModel<Alternative>(a).getModel(Alternative.PROPERTY_NAME)),
					cc.xy(1, row));
			if (criterion instanceof CardinalCriterion) {
				CardinalCriterion cardCrit = (CardinalCriterion) criterion;
				CardinalMeasurement m = model.getMeasurement(cardCrit, a);
				JComponent measComp = getMeasurementComponent(m);
				builder.add(measComp, cc.xy(3, row));				
				CriterionTypeChooser chooser = new CriterionTypeChooser(model, a, criterion);
				builder.add(chooser, cc.xy(5, row));			
			}
			index++;
		}
		return row;
	}


	private JComponent getMeasurementComponent(CardinalMeasurement m) {
		JComponent measComp = null;
		if (m instanceof ExactMeasurement) {
			ExactMeasurement em = (ExactMeasurement) m;
			JFormattedTextField tf = BasicComponentFactory.createFormattedTextField(
					new PresentationModel<ExactMeasurement>(em).getModel(ExactMeasurement.PROPERTY_VALUE),
					new DefaultFormatter());
			
			tf.setHorizontalAlignment(JTextField.CENTER);
			measComp = tf;
		} else if (m instanceof Interval) {
			Interval ival = (Interval) m;
			measComp = new IntervalPanel(null, new PresentationModel<Interval>(ival));
		} else if (m instanceof GaussianMeasurement) {
			GaussianMeasurement gm = (GaussianMeasurement) m;
		    measComp = ComponentBuilder.createGaussianMeasurementPanel(
		             new PresentationModel<GaussianMeasurement>(gm));
		}
		return measComp;
	}
}
