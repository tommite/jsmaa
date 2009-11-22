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

package fi.smaa.jsmaa.gui.components;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.FocusTraversalPolicy;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

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
		
		addFocusListener(new MyFocusListener());
		setFocusTraversalPolicyProvider(true);
		setFocusTraversalPolicy(new MyFocusTraversalPolicy());
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
	
	private class MyFocusTraversalPolicy extends FocusTraversalPolicy {
		@Override
		public Component getComponentAfter(Container container,
				Component component) {
			return getOtherComponent(component);
		}
		private Component getOtherComponent(Component component) {
			if (component == startField) {
				return endField;
			} else if (component == endField) {
				return startField;
			}
			return null;
		}
		@Override
		public Component getComponentBefore(Container container,
				Component component) {
			return getOtherComponent(component);
		}
		@Override
		public Component getDefaultComponent(Container container) {
			return endField;
		}
		@Override
		public Component getFirstComponent(Container container) {
			return endField;
		}
		@Override
		public Component getLastComponent(Container container) {
			return startField;
		}
	}

	private class MyFocusListener implements FocusListener {
		public void focusGained(FocusEvent e) {
			endField.requestFocusInWindow();
		}
		public void focusLost(FocusEvent e) {
		}
	}
}
