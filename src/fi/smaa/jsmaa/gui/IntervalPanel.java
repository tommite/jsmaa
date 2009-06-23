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

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatter;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.AbstractVetoableValueModel;
import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.model.Interval;

public class IntervalPanel extends JPanel {
	private static final long serialVersionUID = -5571531046764331786L;
	private JComponent parent;
	private Interval interval;
	private static final String INPUT_ERROR = "Input error";
		
	public IntervalPanel(JComponent parent, PresentationModel<Interval> model) {
		ValueModel startModel = new IntervalValueModel(model.getModel(Interval.PROPERTY_START), true);
		ValueModel endModel = new IntervalValueModel(model.getModel(Interval.PROPERTY_END), false);
		
		this.parent = parent;
		this.interval = model.getBean();
		
		setLayout(new FlowLayout());		

		JTextField startField = BasicComponentFactory.createFormattedTextField(
				startModel,
				new DefaultFormatter());
		JTextField endField = BasicComponentFactory.createFormattedTextField(
				endModel,
				new DefaultFormatter());

		startField.setHorizontalAlignment(JTextField.CENTER);
		endField.setHorizontalAlignment(JTextField.CENTER);
		startField.setColumns(5);
		endField.setColumns(5);
		add(startField);
		add(endField);
	}
	
	private class IntervalValueModel extends AbstractVetoableValueModel {
		private static final long serialVersionUID = -5651105205219639989L;
		private boolean start;
		protected IntervalValueModel(ValueModel subject, boolean start) {
			super(subject);
			this.start = start;
		}

		@Override
		public boolean proposedChange(Object oldVal, Object newVal) {
			if (start) {
				if ((Double) newVal > interval.getEnd()) {
					JOptionPane.showMessageDialog(parent, "Interval [start, end]: start has to be smaller than end",
					INPUT_ERROR, JOptionPane.ERROR_MESSAGE);
					return false;
				}
			} else { // end
				if ((Double) newVal < interval.getStart()) {
					JOptionPane.showMessageDialog(parent, "Interval [start, end]: end has to be larger than start",
							INPUT_ERROR, JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
			return true;
		}
	}	
}
