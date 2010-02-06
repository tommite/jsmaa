package fi.smaa.jsmaa;

import java.util.List;

import fi.smaa.jsmaa.gui.presentation.InvalidInputException;

public class SMAACEAImportData {

	private List<String[]> data;
	private Type[] types;

	public SMAACEAImportData(List<String[]> data) throws InvalidInputException {
		this.data = data;
		if (data.size() < 2) {
			throw new InvalidInputException("Not enough rows in input data, required at least header row an one input");
		}
		int cols = data.get(0).length;
		if (cols < 4 || cols > 6) {
			throw new InvalidInputException("Invalid number of columns in input ("+cols+") - should be between 4 and 6");
		}
		for (int i=1;i<data.size();i++) {
			if (data.get(i).length != cols) {
				throw new InvalidInputException("Invalid number of columns ("+data.get(i).length+") in input row "+i+
							". Input header has " + cols + " columns.");
			}
		}
		types = new Type[getColumnCount()];
		for (int i=0;i<types.length;i++) {
			types[i] = Type.values()[i];
		}
	}
	
	public Object getDataAt(int row, int col) {
		return data.get(row+1)[col];
	}
	
	public enum Type {
		PATIENT_ID("Pat. ID"),
		TREATMENT_ID("Treatm. ID"),
		COST("Cost"),
		EFFICACY("Effic."),
		COST_CENSORING("Cost cens."),
		EFFICACY_CENCORING("Effic. cens.");
		
		private String title;
		
		Type(String title) {
			this.title = title;
		}
		
		@Override
		public String toString() {
			return title;
		}
	}
	
	public Type getColumnType(int column) {
		return types[column];
	}
	
	public void setColumnType(int column, Type type) {
		Type oldType = types[column];
		if (oldType.equals(type)) {
			return;
		}
		int swapIndex = -1;
		for (int i=0;i<types.length;i++) {
			if (types[i] == type) {
				swapIndex = i;
				break;
			}
		}
		types[column] = type;
		if (swapIndex >= 0) {
			types[swapIndex] = oldType;
		}
	}

	public int getColumnCount() {
		return data.get(0).length;
	}

	public int getRowCount() {
		return data.size() - 1;
	}

	public String getColumnName(int column) {
		return data.get(0)[column];
	}
}

