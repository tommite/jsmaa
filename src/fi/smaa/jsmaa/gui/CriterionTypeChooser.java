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

import fi.smaa.jsmaa.common.Interval;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.CardinalMeasurement;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.ImpactMatrix;
import fi.smaa.jsmaa.model.NoSuchValueException;

@SuppressWarnings("serial")
public class CriterionTypeChooser extends JComboBox {
	
	private static String[] types = {"Interval", "Gaussian"};
	
	private ImpactMatrix matrix;
	private Alternative alt;
	private Criterion crit;

	public CriterionTypeChooser(ImpactMatrix matrix, Alternative alt, Criterion crit) {
		super(types);
		if (matrix == null) {
			throw new NullPointerException();
		}
		this.alt = alt;
		this.crit = crit;
		this.matrix = matrix;
		updateSelected();
		addActionListener(new MyListener());
	}

	private void updateSelected() {
		if (crit instanceof CardinalCriterion) {
			try {
				CardinalMeasurement meas = 
					matrix.getMeasurement((CardinalCriterion) crit, alt);
				if (meas instanceof Interval) {
					setSelectedIndex(0);
				} else if (meas instanceof GaussianMeasurement) {
					setSelectedIndex(1);
				}
			} catch (NoSuchValueException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void updateModel() {
		CardinalMeasurement newMeas = null;
		if (getSelectedIndex() == 0) {
			newMeas = new Interval();
		} else if (getSelectedIndex() == 1) {
			newMeas = new GaussianMeasurement();
		} else {
			throw new IllegalStateException("unknown measurement type");
		}
		try {
			matrix.setMeasurement((CardinalCriterion) crit, alt, newMeas);
		} catch (NoSuchValueException e) {
			e.printStackTrace();
		}
	}
	
	private class MyListener extends AbstractAction {
		public void actionPerformed(ActionEvent e) {
			updateModel();
		}
	}
}
