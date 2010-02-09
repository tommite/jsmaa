package fi.smaa.jsmaa.gui.presentation;

import javax.swing.table.AbstractTableModel;

import fi.smaa.jsmaa.model.SMAACEAModel;

@SuppressWarnings("serial")
public class SMAACEADataTableModel extends AbstractTableModel {
	
	private SMAACEAModel model;
	
	public static final int INDEX_TREATMENT = 0;
	public static final int INDEX_COST = 1;
	public static final int INDEX_EFFECT = 2;

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
		case INDEX_TREATMENT:
			return model.getDataPoints().get(row).getAlternative();
		case INDEX_COST:
			return model.getDataPoints().get(row).getCost();			
		case INDEX_EFFECT:
			return model.getDataPoints().get(row).getEffect();			
		}

		return null;
	}
	
	@Override
	public String getColumnName(int index) {
		switch (index) {
		case INDEX_TREATMENT:
			return "Treatment";
		case INDEX_COST:
			return "Cost";
		case INDEX_EFFECT:
			return "Effect";
		}
		return null;
	}

}
