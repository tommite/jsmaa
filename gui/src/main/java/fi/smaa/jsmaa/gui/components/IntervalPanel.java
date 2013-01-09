/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013.

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

import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatter;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.model.Interval;

public class IntervalPanel extends JPanel {
	private static final long serialVersionUID = -5571531046764331786L;
	private JTextField startField;
	private JTextField endField;
		
	public IntervalPanel(JComponent parent, PresentationModel<Interval> model) {
		ValueModel startModel = new IntervalValueModel(parent, model.getBean(), model.getModel(Interval.PROPERTY_START), true);
		ValueModel endModel = new IntervalValueModel(parent, model.getBean(), model.getModel(Interval.PROPERTY_END), false);
				
		init(startModel, endModel);
		
		addFocusListener(new FocusTransferrer(endField));
		setFocusTraversalPolicyProvider(true);
		setFocusTraversalPolicy(new TwoComponentFocusTraversalPolicy(endField, startField));
	}
		
	public IntervalPanel(ValueModel startModel, ValueModel endModel) {
		init(startModel, endModel);
	}

	private void init(ValueModel startModel, ValueModel endModel) {
		setLayout(new FlowLayout(FlowLayout.LEADING, 0, 5));		

		startField = BasicComponentFactory.createFormattedTextField(
				startModel,
				new DefaultFormatter());
		endField = BasicComponentFactory.createFormattedTextField(
				endModel,
				new DefaultFormatter());

		startField.setHorizontalAlignment(JTextField.CENTER);
		endField.setHorizontalAlignment(JTextField.CENTER);
		startField.setColumns(5);
		endField.setColumns(5);
		add(startField);
		add(endField);
	}
}
