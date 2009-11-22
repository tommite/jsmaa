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

	public GaussianMeasurementPanel(JComponent parent, PresentationModel<GaussianMeasurement> m) {
		setLayout(new FlowLayout());

		JTextField meanField = BasicComponentFactory.createFormattedTextField(
				m.getModel(GaussianMeasurement.PROPERTY_MEAN),
				new DefaultFormatter());
		JTextField stDevField = BasicComponentFactory.createFormattedTextField(
				new StdevValueModel(parent, m.getModel(GaussianMeasurement.PROPERTY_STDEV)), 
				new DefaultFormatter());

		meanField.setHorizontalAlignment(JTextField.CENTER);
		stDevField.setHorizontalAlignment(JTextField.CENTER);
		meanField.setColumns(5);
		stDevField.setColumns(5);
		add(meanField);
		add(new JLabel("\u00B1"));
		add(stDevField);		
	}
}
