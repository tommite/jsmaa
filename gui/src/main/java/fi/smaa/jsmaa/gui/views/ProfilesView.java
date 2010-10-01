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

import javax.swing.JPanel;

import org.drugis.common.gui.LayoutUtil;
import org.drugis.common.gui.ViewBuilder;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.jsmaa.gui.components.MeasurementPanel;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalMeasurement;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class ProfilesView implements ViewBuilder {

	private SMAATRIModel model;
	private OutrankingCriterion crit;

	public ProfilesView(OutrankingCriterion crit, SMAATRIModel model) {
		this.crit = crit;
		this.model = model;
	}
	
	public JPanel buildPanel() {
		FormLayout layout = new FormLayout(
				"right:pref, 1dlu, center:pref, 1dlu, right:pref, 3dlu, left:pref:grow",
				"p" );
		
		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();		
		
		for (int aIndex=0;aIndex<model.getCategories().size()-1;aIndex++) {
			Alternative a = model.getCategories().get(aIndex);
			
			if (aIndex != 0) {
				LayoutUtil.addRow(layout);	
			}
			int row = 1 + (aIndex*2);			
			
			builder.add(BasicComponentFactory.createLabel(
					new PresentationModel<Alternative>(a).getModel(Alternative.PROPERTY_NAME)),
					cc.xy(1, row));
			
			builder.addLabel("-", cc.xy(3, row));
			builder.add(BasicComponentFactory.createLabel(
					new PresentationModel<Alternative>(model.getCategories().get(aIndex+1)).getModel(Alternative.PROPERTY_NAME)),
					cc.xy(5, row));			
			
			ValueHolder holder = createMeasurementHolder(a);
			MeasurementPanel mpanel = new MeasurementPanel(holder);
			builder.add(mpanel, cc.xy(7, row));				
		}
		return builder.getPanel();
	}	
	
	private ValueHolder createMeasurementHolder(Alternative prof) {
		ValueHolder holder = new ValueHolder(model.getCategoryUpperBound(crit, prof));
		holder.addPropertyChangeListener(new HolderListener(prof));
		return holder;
	}
	
	private class HolderListener implements PropertyChangeListener {
		private Alternative profile;
		public HolderListener(Alternative profile) {
			this.profile = profile;
		}
		public void propertyChange(PropertyChangeEvent evt) {
			model.setMeasurement(crit, profile, (CardinalMeasurement)evt.getNewValue());
		}
	}
}
