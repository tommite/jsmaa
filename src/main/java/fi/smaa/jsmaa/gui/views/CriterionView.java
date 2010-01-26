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

package fi.smaa.jsmaa.gui.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.common.gui.LayoutUtil;
import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.IntervalFormat;
import fi.smaa.jsmaa.gui.components.MeasurementPanel;
import fi.smaa.jsmaa.gui.components.MeasurementPanel.MeasurementType;
import fi.smaa.jsmaa.gui.presentation.ImpactMatrixPresentationModel;
import fi.smaa.jsmaa.gui.presentation.OrdinalCriterionMeasurementsPM;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.CardinalMeasurement;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAA2Model;
import fi.smaa.jsmaa.model.SMAATRIModel;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class CriterionView implements ViewBuilder {
	protected Criterion criterion;
	protected SMAA2Model model;
	
	public CriterionView(Criterion crit, SMAA2Model model) {
		this.criterion = crit;
		this.model = model;
	}

	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"pref",
				"p, 3dlu, p, 3dlu, p, 3dlu, p" );

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("Criterion", cc.xy(1,1));		
		builder.add(buildOverviewPart(), cc.xy(1, 3));
		
		int row = 5;
		if (criterion instanceof OutrankingCriterion) {
			LayoutUtil.addRow(layout);
			builder.addSeparator("Thresholds", cc.xy(1, 5));
			LayoutUtil.addRow(layout);
			builder.add(buildThresholdsPart(), cc.xy(1, 7));
			row = 9;
		}
		builder.addSeparator("Measurements", cc.xy(1, row));
		
		JComponent measPanel = null;
		if (criterion instanceof CardinalCriterion) {
			ImpactMatrixPresentationModel iModel = new ImpactMatrixPresentationModel(model.getImpactMatrix());
			measPanel = new CardinalCriterionMeasurementsView((CardinalCriterion) criterion, iModel).buildPanel();			
		} else if (criterion instanceof OrdinalCriterion) {
			OrdinalCriterionMeasurementsPM pm = new OrdinalCriterionMeasurementsPM((OrdinalCriterion) criterion, model.getImpactMatrix());
			measPanel = new OrdinalCriterionMeasurementsView(pm).buildPanel();
		}
		builder.add(measPanel, cc.xy(1, row+2));
		
		if (model instanceof SMAATRIModel) {
			LayoutUtil.addRow(layout);
			builder.addSeparator("Profiles (category boundaries)", cc.xy(1, row+4));
			LayoutUtil.addRow(layout);
			builder.add(new ProfilesView((OutrankingCriterion)criterion, (SMAATRIModel)model).buildPanel(), cc.xy(1, row+6));
		}
			
		return builder.getPanel();
	}

	private JComponent buildOverviewPart() {
		FormLayout layout = new FormLayout(
				"right:pref, 3dlu, left:pref",
				"p, 3dlu, p" );
		
		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();
				
		PresentationModel<Criterion> pm = new PresentationModel<Criterion>(criterion);
		
		builder.addLabel("Name:", cc.xy(1, 1));
		builder.add(BasicComponentFactory.createLabel(pm.getModel(Criterion.PROPERTY_NAME)),
				cc.xy(3, 1)
				);
		builder.addLabel("Type:", cc.xy(1, 3));
		builder.add(BasicComponentFactory.createLabel(pm.getModel(Criterion.PROPERTY_TYPELABEL)),
				cc.xy(3, 3)
		);

		int row = 5;
		if (criterion instanceof ScaleCriterion) {
			LayoutUtil.addRow(layout);
			ScaleCriterion cardCrit = (ScaleCriterion) criterion;
			PresentationModel<ScaleCriterion> pmc = new PresentationModel<ScaleCriterion>(cardCrit);
			builder.addLabel("Scale:", cc.xy(1, row));
			builder.add(BasicComponentFactory.createLabel(pmc.getModel(ScaleCriterion.PROPERTY_SCALE),
					new IntervalFormat()), cc.xy(3, row));
			row += 2;
		}	
		
		if (criterion instanceof CardinalCriterion) {
			CardinalCriterion cardCrit = (CardinalCriterion) criterion;
			PresentationModel<CardinalCriterion> pmc = new PresentationModel<CardinalCriterion>(cardCrit);
			LayoutUtil.addRow(layout);
			builder.addLabel("Ascending:", cc.xy(1, row));
			builder.add(BasicComponentFactory.createCheckBox(
					pmc.getModel(ScaleCriterion.PROPERTY_ASCENDING), null),
					cc.xy(3, row)
			);
		}
		
		return builder.getPanel();
	}

	private JComponent buildThresholdsPart() {
		FormLayout layout = new FormLayout(
				"right:pref, 3dlu, left:pref",
				"p, 3dlu, p" );
		
		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();
		
		final OutrankingCriterion outrCrit = (OutrankingCriterion) criterion;
		
		ValueHolder indifHolder = new ValueHolder(outrCrit.getIndifMeasurement());
		indifHolder.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				outrCrit.setIndifMeasurement((CardinalMeasurement) evt.getNewValue());
			}			
		});
		ValueHolder prefHolder = new ValueHolder(outrCrit.getPrefMeasurement());
		prefHolder.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				outrCrit.setPrefMeasurement((CardinalMeasurement) evt.getNewValue());
			}	
		});

		MeasurementPanel.MeasurementType[] measVals = new MeasurementPanel.MeasurementType[] {
				MeasurementType.EXACT, MeasurementType.INTERVAL, MeasurementType.GAUSSIAN
		};
		JPanel indifPanel = new MeasurementPanel(indifHolder, measVals);		
		JPanel prefPanel = new MeasurementPanel(prefHolder, measVals);
		
		builder.addLabel("Indifference:", cc.xy(1, 1));
		builder.add(indifPanel, cc.xy(3, 1));				
				
		builder.addLabel("Preference:", cc.xy(1, 3));		
		builder.add(prefPanel, cc.xy(3, 3));				
		
		return builder.getPanel();
	}
}
