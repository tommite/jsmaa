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
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatter;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;

import fi.smaa.jsmaa.model.GaussianMeasurement;

@SuppressWarnings("serial")
public class GaussianMeasurementPanel extends JPanel {

	private JTextField meanField;
	private JTextField stDevField;

	public GaussianMeasurementPanel(JComponent parent, PresentationModel<GaussianMeasurement> m) {
		setLayout(new FlowLayout());

		meanField = BasicComponentFactory.createFormattedTextField(
				m.getModel(GaussianMeasurement.PROPERTY_MEAN),
				new DefaultFormatter());
		stDevField = BasicComponentFactory.createFormattedTextField(
				new NonNegativeValueModel(parent, m.getModel(GaussianMeasurement.PROPERTY_STDEV), "Standard deviation"), 
				new DefaultFormatter());

		meanField.setHorizontalAlignment(JTextField.CENTER);
		stDevField.setHorizontalAlignment(JTextField.CENTER);
		meanField.setColumns(5);
		stDevField.setColumns(5);
		add(meanField);
		add(new JLabel("\u00B1"));
		add(stDevField);		
		
		addFocusListener(new FocusTransferrer(meanField));
		setFocusTraversalPolicyProvider(true);
		setFocusTraversalPolicy(new TwoComponentFocusTraversalPolicy(meanField, stDevField));
	}
	
}
