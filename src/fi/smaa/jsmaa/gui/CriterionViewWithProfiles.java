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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.common.gui.LayoutUtil;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.CardinalMeasurement;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class CriterionViewWithProfiles extends CriterionView {

	public CriterionViewWithProfiles(Criterion crit, SMAATRIModel model) {
		super(crit, model);
	}
	
	@Override
	protected int buildMeasurementsPart(FormLayout layout, int fullWidth,
			PanelBuilder builder, CellConstraints cc, int row) {
		row = super.buildMeasurementsPart(layout, fullWidth, builder, cc, row);
		LayoutUtil.addRow(layout);
		row += 2;
		builder.addSeparator("Profiles (category upper bounds)", cc.xyw(1, row, fullWidth));
		
		SMAATRIModel triModel = (SMAATRIModel) model;
				
		int index = 0;
		for (int aIndex=0;aIndex<triModel.getCategories().size()-1;aIndex++) {
			Alternative a = triModel.getCategories().get(aIndex);
			
			LayoutUtil.addRow(layout);
			row += 2;
			builder.add(BasicComponentFactory.createLabel(
					new PresentationModel<Alternative>(a).getModel(Alternative.PROPERTY_NAME)),
					cc.xy(1, row));
			
			if (criterion instanceof OutrankingCriterion) {
				ValueHolder holder = createMeasurementHolder(a);
				MeasurementPanel mpanel = new MeasurementPanel(holder);
				builder.add(mpanel, cc.xy(3, row));				
			}
			index++;
		}
		return row;
	}	
	
	private ValueHolder createMeasurementHolder(Alternative prof) {
		ValueHolder holder = new ValueHolder(((SMAATRIModel)model).getCategoryUpperBound((OutrankingCriterion) criterion, prof));
		holder.addPropertyChangeListener(new HolderListener(prof));
		return holder;
	}
	
	private class HolderListener implements PropertyChangeListener {
		private Alternative profile;
		public HolderListener(Alternative profile) {
			this.profile = profile;
		}
		public void propertyChange(PropertyChangeEvent evt) {
			model.setMeasurement((CardinalCriterion) criterion, profile, (CardinalMeasurement)evt.getNewValue());
		}
	}
}
