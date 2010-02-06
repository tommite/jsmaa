package fi.smaa.jsmaa.gui.views;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;

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
				"pref",
				"p, 3dlu, p");

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("Data", cc.xy(1,1));		
		builder.add(buildDataPart(), cc.xy(1, 3));
		
		return builder.getPanel();
	}

	private Component buildDataPart() {
		JTable dataTable = new JTable(new SMAACEADataTableModel(model));
		JScrollPane scrollPane = new JScrollPane(dataTable);
		return scrollPane;
	}

}
