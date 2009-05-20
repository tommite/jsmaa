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
