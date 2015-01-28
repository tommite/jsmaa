/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

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

import org.drugis.common.gui.LayoutUtil;
import org.drugis.common.gui.ViewBuilder;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

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
	
	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"right:pref, 3dlu, left:pref",
				"p" );
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setBorder(BorderFactory.createEmptyBorder());
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
