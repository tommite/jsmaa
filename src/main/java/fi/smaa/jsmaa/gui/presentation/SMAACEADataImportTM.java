package fi.smaa.jsmaa.gui.presentation;

import javax.swing.table.AbstractTableModel;

import fi.smaa.jsmaa.SMAACEAImportData;

@SuppressWarnings("serial")
public class SMAACEADataImportTM extends AbstractTableModel {

	private SMAACEAImportData data;
	
	public SMAACEADataImportTM(SMAACEAImportData data) {
		this.data = data;
	}

	@Override
	public int getColumnCount() {
		return data.getColumnCount();
	}

	@Override
	public int getRowCount() {
		return data.getRowCount() + 1;
	}
	
	@Override
	public String getColumnName(int column) {
		return data.getColumnName(column);
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (row == 0) {
			return data.getColumnType(col);
		} else {
			return data.getDataAt(row-1, col);
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
		if (!(val instanceof SMAACEAImportData.Type)) {
			throw new IllegalArgumentException("value to set not SMAACEAImportDataType");
		}
		data.setColumnType(col, (SMAACEAImportData.Type) val);
		super.fireTableDataChanged();
	}
	
	@Override
    public boolean isCellEditable(int row, int col) {
		return row == 0;
    }
}
