/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

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
package fi.smaa.jsmaa.gui.components;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.binding.PresentationModel;

import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.RelativeGaussianMeasurementBase;

public class RelativMeasurementPanel extends JPanel {
	private static final long serialVersionUID = 2630898942489705411L;

	public RelativMeasurementPanel(JComponent parent, RelativeGaussianMeasurementBase meas) {
		PresentationModel<GaussianMeasurement> baselineModel = 
			new PresentationModel<GaussianMeasurement>(meas.getBaseline());
		PresentationModel<GaussianMeasurement> relativeModel = 
			new PresentationModel<GaussianMeasurement>(meas.getRelative());
		
		setLayout(new FlowLayout());

		JComponent baselineComp = new GaussianMeasurementPanel(this, baselineModel);
		JComponent relativeComp = new GaussianMeasurementPanel(this, relativeModel);
		
		add(baselineComp);
		add(new JLabel(" + "));
		add(relativeComp);
	}
}
