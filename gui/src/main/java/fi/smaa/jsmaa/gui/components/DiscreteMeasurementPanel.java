/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.

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
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.binding.PresentationModel;

import fi.smaa.jsmaa.model.DiscreteMeasurement;
import fi.smaa.jsmaa.model.Point2D;

public class DiscreteMeasurementPanel extends JPanel implements
		ListSelectionListener {

	private static final long serialVersionUID = 3084366501203897609L;

	private JList<Point2D> jlist;
	private DiscreteMeasurement dm;
	private JComponent parent;

	private JButton addButton;
	private JButton deleteButton;
	private JTextField value;
	private JTextField probability;

	private static final String addString = "+";
	private static final String deleteString = "-";

	public DiscreteMeasurementPanel(JComponent parent,
			PresentationModel<DiscreteMeasurement> m) {
		setLayout(new FlowLayout());
		dm = m.getBean();
		this.parent = parent;

		jlist = new JList<Point2D>(dm);
		jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlist.setSelectedIndex(0);
		jlist.addListSelectionListener(this);
		add("Discrete points: ", new JScrollPane(jlist));

		value = new JTextField();
		value.addActionListener(new AddButtonListener());
		String strvalue = "" + dm.getElementAt(jlist.getSelectedIndex()).getX();
		value.setText(strvalue);
		value.setHorizontalAlignment(JTextField.CENTER);
		value.setColumns(5);
		add(new JLabel("P(X="));
		add("Value:", value);
		add(new JLabel(") = "));

		probability = new JTextField();
		probability.addActionListener(new AddButtonListener());
		String strprobability = ""
				+ dm.getElementAt(jlist.getSelectedIndex()).getY();
		probability.setText(strprobability);
		probability.setHorizontalAlignment(JTextField.CENTER);
		probability.setColumns(5);
		add("Probability:", probability);
		
		addButton = new JButton(addString);
		addButton.setActionCommand(addString);
		addButton.addActionListener(new AddButtonListener());
		add(addButton);
		
		deleteButton = new JButton(deleteString);
		deleteButton.setActionCommand(deleteString);
		deleteButton.addActionListener(new DeleteButtonListener());
		add(deleteButton);

		addFocusListener(new FocusTransferrer(value));
		setFocusTraversalPolicyProvider(true);
		setFocusTraversalPolicy(new TwoComponentFocusTraversalPolicy(value,
				probability));
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		 if (e.getValueIsAdjusting() == false) {
			 
	            if (jlist.getSelectedIndex() == -1) {
	            //No selection: disable delete, up, and down buttons.
	                deleteButton.setEnabled(false);
	                value.setText("");
	                probability.setText("");
	 
	            } else {
	            //Single selection: permit all operations.
	                deleteButton.setEnabled(true);
	                value.setText(""+jlist.getSelectedValue().getX());
	                probability.setText(""+jlist.getSelectedValue().getY());
	            }
		 }

	}

	class DeleteButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			ListSelectionModel lsm = jlist.getSelectionModel();
			int selected = lsm.getMinSelectionIndex();
			dm.remove(selected);

			int size = dm.size();

			if (size == 0) {
				// List is empty: disable delete, up, and down buttons.
				deleteButton.setEnabled(false);
			} else {
				// Adjust the selection.
				if (selected == dm.getSize()) {
					// Removed item in last position.
					selected--;
				}
				jlist.setSelectedIndex(selected);
			}
		}
	}

	/** A listener shared by the text field and add button. */
	class AddButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (value.getText().equals("") || probability.getText().equals("")) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}

			double val = 0;
			double prob = 0;
			try {
				val = Double.parseDouble(value.getText());
				prob = Double.parseDouble(probability.getText());
			} catch (NumberFormatException ex) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}

			int size = dm.getSize();
			Point2D point = new Point2D(val, prob);
			if(!dm.add(point)) {
				JOptionPane.showMessageDialog(parent,
					    "The summed probability exeeds 1.0",
					    "Point not added!",
					    JOptionPane.ERROR_MESSAGE);
				return;
			}
			jlist.setSelectedIndex(size);
		}
	}

}
