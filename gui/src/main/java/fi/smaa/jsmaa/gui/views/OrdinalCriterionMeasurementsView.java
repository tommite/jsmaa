/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.

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
