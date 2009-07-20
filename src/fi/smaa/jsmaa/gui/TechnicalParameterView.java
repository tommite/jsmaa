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

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueModel;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class TechnicalParameterView implements ViewBuilder {
	
	private SMAATRIModel model;

	public TechnicalParameterView(SMAATRIModel model) {
		this.model = model;
	}

	public JComponent buildPanel() {		
		FormLayout layout = new FormLayout(
				"left:pref, 3dlu, left:pref:grow",
				"p, 3dlu, p, 3dlu, p" );
		
		int fullWidth = 3;

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("Model technical parameters", cc.xyw(1, 1, fullWidth));
		
		builder.addLabel("Lambda interval", cc.xy(1, 3));
		
		
		Interval range = new Interval(0.5, 1.0);
		String msg = "Lambda must be within range ";
		
		PresentationModel<Interval> imodel = new PresentationModel<Interval>(model.getLambda());
		IntervalPanel lambdaPanel = new IntervalPanel(
				new ConstrainedIntervalValueModel(null, model.getLambda(), 
						imodel.getModel(Interval.PROPERTY_START), true, range, msg),
				new ConstrainedIntervalValueModel(null, model.getLambda(), 
						imodel.getModel(Interval.PROPERTY_END), false, range, msg));				
		builder.add(lambdaPanel,
				cc.xy(3, 3));
		
		builder.addLabel("Exploitation rule", cc.xy(1, 5));
		builder.add(
				BasicComponentFactory.createCheckBox(
						new PresentationModel<SMAATRIModel>(model).getModel(SMAATRIModel.PROPERTY_RULE),
						"(optimistic if checked, pessimistic otherwise)"), cc.xy(3, 5));
		return builder.getPanel();
	}

}
