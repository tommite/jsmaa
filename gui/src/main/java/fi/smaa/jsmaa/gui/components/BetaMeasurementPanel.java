package fi.smaa.jsmaa.gui.components;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatter;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.model.BetaMeasurement;

public class BetaMeasurementPanel extends JPanel {
	
	private static final long serialVersionUID = -5571531046764331786L;
	
	private JTextField alphaField;
	private JTextField betaField;	
	private JTextField minField;
	private JTextField maxField;
		
	public BetaMeasurementPanel(JComponent parent, PresentationModel<BetaMeasurement> model) {
		ValueModel alphaModel = new NonNegativeValueModel(parent, model.getModel(BetaMeasurement.PROPERTY_ALPHA), "alpha");
		ValueModel betaModel = new NonNegativeValueModel(parent, model.getModel(BetaMeasurement.PROPERTY_BETA), "beta");
		
		ValueModel minModel = new BetaMinMaxValueModel(parent, model.getBean(), model.getModel(BetaMeasurement.PROPERTY_MIN), true);
		ValueModel maxModel = new BetaMinMaxValueModel(parent, model.getBean(), model.getModel(BetaMeasurement.PROPERTY_MAX), false);		
								
		setLayout(new FlowLayout(FlowLayout.LEADING, 0, 5));
		
		alphaField = BasicComponentFactory.createFormattedTextField(alphaModel, new DefaultFormatter());
		betaField = BasicComponentFactory.createFormattedTextField(betaModel, new DefaultFormatter());

		alphaField.setHorizontalAlignment(JTextField.CENTER);
		betaField.setHorizontalAlignment(JTextField.CENTER);
		alphaField.setColumns(5);
		betaField.setColumns(5);
		
		minField = BasicComponentFactory.createFormattedTextField(minModel,	new DefaultFormatter());
		maxField = BasicComponentFactory.createFormattedTextField(maxModel,	new DefaultFormatter());

		minField.setHorizontalAlignment(JTextField.CENTER);
		maxField.setHorizontalAlignment(JTextField.CENTER);
		minField.setColumns(5);
		maxField.setColumns(5);
		add(new JLabel("\u03B1"));		
		add(alphaField);
		add(new JLabel("\u03B2"));
		add(betaField);
		add(new JLabel("min"));		
		add(minField);
		add(new JLabel("max"));		
		add(maxField);
		
		addFocusListener(new FocusTransferrer(alphaField));
		setFocusTraversalPolicyProvider(true);
	}
}
