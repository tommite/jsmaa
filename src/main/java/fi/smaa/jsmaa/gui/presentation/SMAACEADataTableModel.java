package fi.smaa.jsmaa.gui.presentation;

import javax.swing.table.AbstractTableModel;

import fi.smaa.jsmaa.model.SMAACEAModel;

@SuppressWarnings("serial")
public class SMAACEADataTableModel extends AbstractTableModel {
	
	private SMAACEAModel model;

	public SMAACEADataTableModel(SMAACEAModel model) {
		this.model = model;
	}

	public int getColumnCount() {
		return 3;
	}

	public int getRowCount() {
		return model.getDataPoints().size();
	}

	public Object getValueAt(int row, int col) {
		switch (col) {
		case 0:
			return model.getDataPoints().get(row).getAlternative();
		case 1:
			return model.getDataPoints().get(row).getCost();			
		case 2:
			return model.getDataPoints().get(row).getEffect();			
		}

		return null;
	}
	
	@Override
	public String getColumnName(int index) {
		switch (index) {
		case 0:
			return "Treatment";
		case 1:
			return "Cost";
		case 2:
			return "Effect";
		}
		return null;
	}

}
