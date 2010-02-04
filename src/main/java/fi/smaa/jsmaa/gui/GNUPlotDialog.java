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
			@Override
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
