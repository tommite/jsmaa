package fi.smaa.jsmaa.gui.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideButton;

import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.presentation.SMAACEADataTableModel;
import fi.smaa.jsmaa.model.SMAACEAModel;
import fi.smaa.jsmaa.model.cea.SMAACEASurvivalAnalysis;

public class SMAACEADataView implements ViewBuilder {
	
	private SMAACEAModel model;

	public SMAACEADataView(SMAACEAModel model) {
		this.model = model;
	}

	@Override
	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"pref:grow:fill",
				"p, 3dlu, p, 3dlu, p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("Data", cc.xy(1,1));		
		builder.add(buildDataPart(), cc.xy(1, 3));
		builder.add(buildSurvivalButton(), cc.xy(1, 5));
		
		return builder.getPanel();
	}

	private Component buildSurvivalButton() {
		JideButton survButton = new JideButton("Survival analysis");
		survButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SMAACEASurvivalAnalysis anal = new SMAACEASurvivalAnalysis(model);
				System.out.println(anal.getMeanSurvivalRates());
			}
			
		});
		return survButton;
	}

	private Component buildDataPart() {
		SMAACEADataTableModel tableModel = new SMAACEADataTableModel(model);
		JTable dataTable = new JTable(tableModel);
		dataTable.setAutoCreateRowSorter(true);
		dataTable.setDefaultRenderer(Object.class, new MyTableCellRenderer());
		JScrollPane scrollPane = new JScrollPane(dataTable);
		return scrollPane;
	}
	
	private class MyTableCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object val, boolean isSelected, 
				boolean hasFocus, int row, int col) {			
			if (!model.getDataPoints().get(row).isNotCensored()) {
				setBackground(Color.red);
				this.setToolTipText("Censored");
			} else {
				setBackground(Color.white);
				this.setToolTipText(null);				
			}
			return super.getTableCellRendererComponent(table, val, isSelected, hasFocus, row, col);
		}
	}
}
