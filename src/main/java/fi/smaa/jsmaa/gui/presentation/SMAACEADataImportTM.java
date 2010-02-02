package fi.smaa.jsmaa.gui.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class SMAACEADataImportTM extends AbstractTableModel {
	
	private List<String[]> data;
	private List<SMAACEAImportDataType> types = new ArrayList<SMAACEAImportDataType>();
	
	public SMAACEADataImportTM(List<String[]> data) throws InvalidInputException {
		this.data = data;
		if (data.size() > 0) {
			int frowLength = data.get(0).length;
			if (frowLength < 4 || frowLength > 6) {
				throw new InvalidInputException("invalid number of columns ("+frowLength+") in input");
			}
			for (int i=0;i<frowLength;i++) {
				types.add(SMAACEAImportDataType.values()[i]);
			}
		}
	}

	@Override
	public int getColumnCount() {
		if (data.size() == 0) {
			return 0;
		}
		return data.get(0).length;
	}

	@Override
	public int getRowCount() {
		if (data.size() == 0) {
			return 0;
		}
		return data.size();
	}
	
	@Override
	public String getColumnName(int column) {
		if (data.size() == 0) {
			return "";
		}
		return data.get(0)[column];
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (row == 0) {
			return types.get(col);			
		} else {
			return data.get(row)[col];
		}
	}
	
	@Override
	public void setValueAt(Object val, int row, int col) {
		if (row != 0) {
			throw new IllegalArgumentException("only setting value for row 0 allowed");
		}
		if (col < 0 || col > getColumnCount()) {
			throw new IndexOutOfBoundsException("column " + col + " out of bounds");
		}
		if (!(val instanceof SMAACEAImportDataType)) {
			throw new IllegalArgumentException("value to set not SMAACEAImportDataType");
		}
		types.set(col, (SMAACEAImportDataType) val);
		super.fireTableDataChanged();
	}
	
	@Override
    public boolean isCellEditable(int row, int col) {
		return row == 0;
    }
}
