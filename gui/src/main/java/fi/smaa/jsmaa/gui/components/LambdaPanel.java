/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper 2012.

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

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jidesoft.swing.RangeSlider;

import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.SMAATRIModel;

@SuppressWarnings("serial")
public class LambdaPanel extends JPanel {
	
	private RangeSlider lambdaSlider;
	private JLabel lambdaRangeLabel;
	private SMAATRIModel model;

	public LambdaPanel(SMAATRIModel model) {
		this.model = model;
		initComponents();
		
		model.getLambda().addPropertyChangeListener(new LambdaListener());
		
		updateLambdaSlider();
		updateLambdaLabel();
	}

	private void updateLambdaSlider() {
		lambdaSlider.setLowValue((int) (model.getLambda().getStart() * 100.0));
		lambdaSlider.setHighValue((int) (model.getLambda().getEnd() * 100.0));
	}

	private void initComponents() {
		lambdaSlider = new RangeSlider(50, 100, (int) (model.getLambda().getStart() * 100.0),
				(int)(model.getLambda().getEnd() * 100.0));
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
		lambdaRangeLabel.setText("[" + lambdaSlider.getLowValue() / 100.0 + "-"
				+lambdaSlider.getHighValue() / 100.0+ "]");	
	}	
	
	protected void fireLambdaSliderChanged() {
		Interval lambda = model.getLambda();
		double lowVal = lambdaSlider.getLowValue() / 100.0;
		double highVal = lambdaSlider.getHighValue() / 100.0;
		lambda.setStart(lowVal);
		lambda.setEnd(highVal);
	}	
	
	private class LambdaListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			if (!evt.getNewValue().equals(evt.getOldValue())) {
				updateLambdaLabel();
				updateLambdaSlider();
			}
		}
	}

}
