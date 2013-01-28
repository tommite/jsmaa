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

import java.awt.Color;
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

	private JList jlist;
	private DiscreteMeasurement dm;
	private JComponent parent;

	private JButton addButton;
	private JButton deleteButton;
	private JTextField valueText;
	private JTextField probabilityText;
	private JLabel sumProbabilityLabel;

	public DiscreteMeasurementPanel(JComponent parent,
			PresentationModel<DiscreteMeasurement> m) {
		setLayout(new FlowLayout());
		dm = m.getBean();
		this.parent = parent;

		jlist = new JList(dm);
		jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		jlist.setSelectedIndex(0);
		jlist.addListSelectionListener(this);
		add("Discrete points: ", new JScrollPane(jlist));

		valueText = new JTextField();
		valueText.addActionListener(new AddButtonListener());
		if(dm.getSize() > 0) {
			String strvalue = "" + dm.getElementAt(jlist.getSelectedIndex()).getX();
			valueText.setText(strvalue);
		}
		valueText.setHorizontalAlignment(JTextField.CENTER);
		valueText.setColumns(5);
		add(new JLabel("P(X="));
		add("Value:", valueText);
		add(new JLabel(") = "));

		probabilityText = new JTextField();
		probabilityText.addActionListener(new AddButtonListener());
		if(dm.getSize() > 0) {
			String strprobability = ""
				+ dm.getElementAt(jlist.getSelectedIndex()).getY();
			probabilityText.setText(strprobability);
		}
		probabilityText.setHorizontalAlignment(JTextField.CENTER);
		probabilityText.setColumns(5);
		add("Probability:", probabilityText);
		
		addButton = new JButton("+");
		addButton.addActionListener(new AddButtonListener());
		add(addButton);
		
		deleteButton = new JButton("-");
		deleteButton.addActionListener(new DeleteButtonListener());
		add(deleteButton);
		
		add(new JLabel("P(\u2126) = "));
		sumProbabilityLabel = new JLabel("1.0");
		refreshSummedProbability();
		add(sumProbabilityLabel);

		addFocusListener(new FocusTransferrer(valueText));
		setFocusTraversalPolicyProvider(true);
		setFocusTraversalPolicy(new TwoComponentFocusTraversalPolicy(valueText,
				probabilityText));
	}

	private void refreshSummedProbability() {
		double totalprob = dm.getTotalProbability();
		if (totalprob != 1.0) {
			sumProbabilityLabel.setForeground(Color.red);
		} else {
			sumProbabilityLabel.setForeground(Color.black);
		}
		sumProbabilityLabel.setText(""+totalprob);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		 if (e.getValueIsAdjusting() == false) {
			 
	            if (jlist.getSelectedIndex() == -1) {
	            //No selection: disable delete, up, and down buttons.
	                deleteButton.setEnabled(false);
	                valueText.setText("");
	                probabilityText.setText("");
	 
	            } else {
	            //Single selection: permit all operations.
	                deleteButton.setEnabled(true);
	                valueText.setText(""+((Point2D) jlist.getSelectedValue()).getX());
	                probabilityText.setText(""+((Point2D) jlist.getSelectedValue()).getY());
	            }
		 }

	}

	private class DeleteButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			ListSelectionModel lsm = jlist.getSelectionModel();
			int selected = lsm.getMinSelectionIndex();
			dm.remove(selected);
			refreshSummedProbability();
			
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
	private class AddButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (valueText.getText().equals("") || probabilityText.getText().equals("")) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}

			double val = 0;
			double prob = 0;
			try {
				val = Double.parseDouble(valueText.getText());
				prob = Double.parseDouble(probabilityText.getText());
			} catch (NumberFormatException ex) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}
			
			if(prob <= 0.0 || prob > 1.0) {
				JOptionPane.showMessageDialog(parent,
					    "The probability has to be between 0.0 and 1.0!",
					    "Point not added!",
					    JOptionPane.ERROR_MESSAGE);
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
			refreshSummedProbability();
			jlist.setSelectedIndex(size);
		}
	}

}
