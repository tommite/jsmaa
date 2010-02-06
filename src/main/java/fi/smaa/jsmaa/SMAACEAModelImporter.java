package fi.smaa.jsmaa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fi.smaa.jsmaa.gui.presentation.InvalidInputException;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.SMAACEAModel;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class SMAACEAModelImporter {

	private List<String[]> data;
	private Type[] types;

	public SMAACEAModelImporter(List<String[]> data) throws InvalidInputException {
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
		PATIENT_ID("Pat. ID", true),
		TREATMENT_ID("Treatm. ID", true),
		COST("Cost", true),
		EFFICACY("Effic.", true),
		COST_CENSORING("Cost cens.", false),
		EFFICACY_CENCORING("Effic. cens.", false);
		
		private String title;
		private boolean mandatory;
		
		Type(String title, boolean mandatory) {
			this.title = title;
			this.mandatory = mandatory;
		}
		
		@Override
		public String toString() {
			return title;
		}
		
		public boolean isMandatory() {
			return mandatory;
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
	
	public boolean isComplete() {
		List<Type> tList = Arrays.asList(types);
		for (Type t : Type.values()) {
			if (t.isMandatory()) {
				if (!tList.contains(t)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * PRE-COND: isComplete()
	 * @return
	 */
	public SMAACEAModel constructModel() {
		if (!isComplete()) {
			throw new IllegalStateException("pre-condition violation: !isComplete()");
		}
		SMAACEAModel model = new SMAACEAModel("SMAA-CEA model");
		model.addCriterion(new ScaleCriterion("Cost", false));
		model.addCriterion(new ScaleCriterion("Effect", true));
		
		List<Alternative> alts = loadAlternatives();
		for (Alternative a : alts) {
			model.addAlternative(a);
		}
		return model;
	}

	private List<Alternative> loadAlternatives() {
		List<String> altNames = new ArrayList<String>();
		int treatmentNameColumn = getColumnIndex(Type.TREATMENT_ID);
		for (int i=0;i<getRowCount();i++) {
			String treatmentName = getDataAt(i, treatmentNameColumn).toString();
			if (!altNames.contains(treatmentName)) {
				altNames.add(treatmentName);
			}
		}
		List<Alternative> alts = new ArrayList<Alternative>();
		for (String s : altNames) {
			alts.add(new Alternative(s));
		}
		return alts;
	}

	private int getColumnIndex(Type treatment_id) {
		for (int i=0;i<types.length;i++) {
			if (types[i].equals(treatment_id)) {
				return i;
			}
		}
		throw new IllegalArgumentException("no index found for treatment_id " + treatment_id);
	}
}

