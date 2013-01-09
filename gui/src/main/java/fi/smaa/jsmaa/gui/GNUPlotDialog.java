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
package fi.smaa.jsmaa.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.jidesoft.swing.JideButton;

import fi.smaa.jsmaa.gui.jfreechart.PlotConverter;

@SuppressWarnings("serial")
public class GNUPlotDialog extends JDialog {
	
	public GNUPlotDialog(JFrame parent, PlotConverter c) {
		super(parent, "GNUPlot script");
		
		setPreferredSize(new Dimension(700, 400));

		JPanel pane = new JPanel();
		pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));
	
		JLabel label1 = new JLabel("Script");
		label1.setAlignmentX(Component.CENTER_ALIGNMENT);
		pane.add(label1);
		JTextArea scriptArea = new JTextArea();
		scriptArea.setEditable(false);
		scriptArea.setText(c.getScript());
		pane.add(new JScrollPane(scriptArea));
		
		JLabel label2 = new JLabel("Data file (save as data.dat)");
		label2.setAlignmentX(Component.CENTER_ALIGNMENT);		
		pane.add(label2);
		JTextArea dataArea = new JTextArea();
		dataArea.setEditable(false);
		dataArea.setText(c.getData());
		pane.add(new JScrollPane(dataArea));
		
		JButton closeButton = new JideButton("Close");
		closeButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		closeButton.setAlignmentX(Component.CENTER_ALIGNMENT);		
		pane.add(closeButton);
		
		setContentPane(pane);
		pack();
	}
}
