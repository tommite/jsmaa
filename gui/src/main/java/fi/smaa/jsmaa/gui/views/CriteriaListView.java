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

import javax.swing.BorderFactory;
import javax.swing.JComponent;


import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.jsmaa.gui.IntervalFormat;
import fi.smaa.jsmaa.gui.LayoutUtil;
import fi.smaa.jsmaa.gui.ViewBuilder;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.ScaleCriterion;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.SMAAModel;

public class CriteriaListView implements ViewBuilder {
	
	private SMAAModel model;
	
	public CriteriaListView(SMAAModel model) {
		this.model = model;
	}

	public JComponent buildPanel() {
		
		FormLayout layout = new FormLayout(
				"pref, 3dlu, left:pref",
				"p" );

		if (hasCardinalCriteria(model)) {
			layout.appendColumn(ColumnSpec.decode("3dlu"));
			layout.appendColumn(ColumnSpec.decode("center:pref"));
		}
		if (hasScaleCriteria(model)) {
			layout.appendColumn(ColumnSpec.decode("3dlu"));
			layout.appendColumn(ColumnSpec.decode("center:pref"));
		}

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setBorder(BorderFactory.createEmptyBorder());		
		CellConstraints cc = new CellConstraints();
		
		builder.addLabel("Name", cc.xy(1, 1));
		builder.addLabel("Type", cc.xy(3, 1));
		
		if (hasCardinalCriteria(model)) {
			builder.addLabel("Ascending", cc.xy(5, 1));
		}
		if (hasScaleCriteria(model)) {
			builder.addLabel("Scale", cc.xy(7, 1));
		}

		int row = 3;

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
				builder.add(BasicComponentFactory.createCheckBox(
						cpm.getModel(ScaleCriterion.PROPERTY_ASCENDING), null),
						cc.xy(5, row)
						);
			}
			if (c instanceof ScaleCriterion) {
				ScaleCriterion cardCrit = (ScaleCriterion) c;
				PresentationModel<ScaleCriterion> cpm = new PresentationModel<ScaleCriterion>(cardCrit);				
				builder.add(BasicComponentFactory.createLabel(
						cpm.getModel(ScaleCriterion.PROPERTY_SCALE),
						new IntervalFormat()),
						cc.xy(7, row)
						);				
			}
	
			row += 2;
		}
		
		return builder.getPanel();
	}

	private boolean hasScaleCriteria(SMAAModel model2) {
		for (Criterion c : model.getCriteria()) {
			if (c instanceof ScaleCriterion) {
				return true;
			}
		}
		return false;	}

	private boolean hasCardinalCriteria(SMAAModel model) {
		for (Criterion c : model.getCriteria()) {
			if (c instanceof CardinalCriterion) {
				return true;
			}
		}
		return false;
	}

}
