package fi.smaa.jsmaa.gui.components;

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jgoodies.binding.PresentationModel;

import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.RelativeLogitNormalMeasurement;

public class RelativeLogitNormalMeasurementPanel extends JPanel {
	private static final long serialVersionUID = 2630898942489705411L;

	public RelativeLogitNormalMeasurementPanel(JComponent parent, RelativeLogitNormalMeasurement meas) {
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
