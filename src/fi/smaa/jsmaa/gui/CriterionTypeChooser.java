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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.ScaleCriterion;
import fi.smaa.jsmaa.model.CardinalMeasurement;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.LogNormalMeasurement;
import fi.smaa.jsmaa.model.SMAAModel;

@SuppressWarnings("serial")
public class CriterionTypeChooser extends JComboBox {
	
	private static String[] types = {"Interval", "Gaussian", "LogNormal"};
	
	private SMAAModel model;
	private Alternative alt;
	private Criterion crit;

	public CriterionTypeChooser(SMAAModel model, Alternative alt, Criterion crit) {
		super(types);
		if (model == null) {
			throw new NullPointerException();
		}
		this.alt = alt;
		this.crit = crit;
		this.model = model;
		updateSelected();
		addActionListener(new MyListener());
	}

	private void updateSelected() {
		if (crit instanceof ScaleCriterion) {
			CardinalMeasurement meas = 
				model.getMeasurement((ScaleCriterion) crit, alt);
			if (meas instanceof Interval) {
				setSelectedIndex(0);
			} else if (meas instanceof LogNormalMeasurement) {
				setSelectedIndex(2);
			} else if (meas instanceof GaussianMeasurement) {
				setSelectedIndex(1);
			}
		}
	}
	
	private void updateModel() {
		CardinalMeasurement newMeas = null;
		if (getSelectedIndex() == 0) {
			newMeas = new Interval();
		} else if (getSelectedIndex() == 1) {
			newMeas = new GaussianMeasurement();
		} else if (getSelectedIndex() == 2) {
			newMeas = new LogNormalMeasurement();
		} else {
			throw new IllegalStateException("unknown measurement type");
		}
		model.setMeasurement((ScaleCriterion) crit, alt, newMeas);
	}
	
	private class MyListener extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			updateModel();
		}
	}
}
