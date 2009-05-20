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

package fi.smaa.gui;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatter;


import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;

import fi.smaa.GaussianMeasurement;
import fi.smaa.common.Interval;

public class ComponentBuilder {

	public static JComponent createIntervalPanel(PresentationModel<Interval> model) {
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());		

		JTextField startField = BasicComponentFactory.createFormattedTextField(
				model.getModel(Interval.PROPERTY_START), 
				new DefaultFormatter());
		JTextField endField = BasicComponentFactory.createFormattedTextField(
				model.getModel(Interval.PROPERTY_END), 
				new DefaultFormatter());
				
		startField.setHorizontalAlignment(JTextField.CENTER);
		endField.setHorizontalAlignment(JTextField.CENTER);
		startField.setColumns(5);
		endField.setColumns(5);
		panel.add(startField);
		panel.add(endField);
		
		return panel;
	}
	
	public static JComponent createGaussianMeasurementPanel(PresentationModel<GaussianMeasurement> model) {
		JPanel panel = new JPanel();
		
		panel.setLayout(new FlowLayout());

		JTextField meanField = BasicComponentFactory.createFormattedTextField(
				model.getModel(GaussianMeasurement.PROPERTY_MEAN), 
				new DefaultFormatter());
		JTextField stDevField = BasicComponentFactory.createFormattedTextField(
				model.getModel(GaussianMeasurement.PROPERTY_STDEV), 
				new DefaultFormatter());

		meanField.setHorizontalAlignment(JTextField.CENTER);
		stDevField.setHorizontalAlignment(JTextField.CENTER);
		meanField.setColumns(5);
		stDevField.setColumns(5);
		panel.add(meanField);
		panel.add(new JLabel("\u00B1"));
		panel.add(stDevField);		

		return panel;
	}
}
