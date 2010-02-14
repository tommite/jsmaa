package fi.smaa.jsmaa.gui.views;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.presentation.SMAACEADataTableModel;
import fi.smaa.jsmaa.model.SMAACEAModel;

public class SMAACEADataView implements ViewBuilder {
	
	private SMAACEAModel model;

	public SMAACEADataView(SMAACEAModel model) {
		this.model = model;
	}

	@Override
	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"pref:grow:fill",
				"p, 3dlu, p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("Data", cc.xy(1,1));		
		builder.add(buildDataPart(), cc.xy(1, 3));
		
		return builder.getPanel();
	}

	private Component buildDataPart() {
		SMAACEADataTableModel tableModel = new SMAACEADataTableModel(model);
		JTable dataTable = new JTable(tableModel);
		dataTable.setAutoCreateRowSorter(true);
		dataTable.setDefaultRenderer(Object.class, new MyTableCellRenderer());
		JScrollPane scrollPane = new JScrollPane(dataTable);
		return scrollPane;
	}
	
	@SuppressWarnings("serial")
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
