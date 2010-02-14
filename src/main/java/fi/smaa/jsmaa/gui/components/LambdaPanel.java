package fi.smaa.jsmaa.gui.components;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jidesoft.swing.RangeSlider;

import fi.smaa.jsmaa.model.Interval;

@SuppressWarnings("serial")
public class LambdaPanel extends JPanel {
	
	private RangeSlider lambdaSlider;
	private JLabel lambdaRangeLabel;
	private Interval lambda;
	
	private Interval range;
	private double multiplier;

	public LambdaPanel(Interval lambda, Interval range, double parameterMultiplier) {
		this.lambda = lambda;
		this.range = range;
		this.multiplier = parameterMultiplier;
		initComponents();
		
		lambda.addPropertyChangeListener(new LambdaListener());
		
		updateLambdaSlider();
		updateLambdaLabel();
	}

	private void updateLambdaSlider() {
		lambdaSlider.setLowValue((int) (lambda.getStart() * multiplier));
		lambdaSlider.setHighValue((int) (lambda.getEnd() * multiplier));
	}

	private void initComponents() {
		lambdaSlider = new RangeSlider((int) (range.getStart() * multiplier),
				(int) (range.getEnd() * multiplier), (int) (lambda.getStart() * multiplier),
				(int)(lambda.getEnd() * multiplier));
		lambdaSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				fireLambdaSliderChanged();
			}
		});
		
		lambdaRangeLabel = new JLabel();
		
		add(lambdaSlider, BorderLayout.CENTER);
		add(new JLabel("Lambda range"), BorderLayout.NORTH);				
		add(lambdaRangeLabel, BorderLayout.SOUTH);
	}
	
	private void updateLambdaLabel() {
		lambdaRangeLabel.setText("[" + lambdaSlider.getLowValue() / multiplier + "-"
				+lambdaSlider.getHighValue() / multiplier + "]");	
	}	
	
	protected void fireLambdaSliderChanged() {
		double lowVal = lambdaSlider.getLowValue() / multiplier;
		double highVal = lambdaSlider.getHighValue() / multiplier;
		lambda.setStart(lowVal);
		lambda.setEnd(highVal);
	}	
	
	private class LambdaListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (!evt.getNewValue().equals(evt.getOldValue())) {
				updateLambdaLabel();
				updateLambdaSlider();
			}
		}
	}

}
