package fi.smaa.jsmaa.gui.presentation;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import fi.smaa.jsmaa.model.cea.SMAACEAModelImporter;
import fi.smaa.jsmaa.model.cea.SMAACEAModelImporter.Type;

public class SMAACEAModelImporterPM {

	private SMAACEAModelImporter importer;
	private List<SelectionType> types = new ArrayList<SelectionType>();
	private List<List<SMAACEAModelImporter.Type>> selectionLists =	new ArrayList<List<SMAACEAModelImporter.Type>>();

	public SMAACEAModelImporterPM(SMAACEAModelImporter importer) {
		this.importer = importer;
		constructTypes();
		constructSelectionLists();
	}

	private void constructSelectionLists() {
		for (int i=0;i<types.size();i++) {
			selectionLists.add(getList(types.get(i)));
		}
	}

	private List<SMAACEAModelImporter.Type> getList(SelectionType selectionType) {
		ArrayDeque<SMAACEAModelImporter.Type> list = new ArrayDeque<Type>(); 
		switch (selectionType) {
		case ALL:
			list.offerFirst(Type.COST_CENSORING);
		case DOUBLE:
			list.offerFirst(Type.EFFECT);				
			list.offerFirst(Type.COST);		
		case CHAR:
			list.offerFirst(Type.TREATMENT_ID);
			list.offerFirst(Type.PATIENT_ID);
			break;			
		}
		return new ArrayList<SMAACEAModelImporter.Type>(list);
	}

	private void constructTypes() {
		for (int col=0;col<importer.getColumnCount();col++) {
			SelectionType type = SelectionType.ALL; 
			for (int row=0;row<importer.getRowCount();row++) {				
				String data = (String) importer.getDataAt(row, col);
				if (type == SelectionType.ALL) {
					if (!isBoolean(data)) {
						type = SelectionType.DOUBLE;
					}
				}				
				if (type == SelectionType.DOUBLE) {
					if (!isDouble(data)) {
						type = SelectionType.CHAR;
						row = importer.getRowCount(); // break loop
					}
				}
			} // end inner for
			types.add(type);
		}
	}
	
	private boolean isBoolean(String data) {
		try {
			int val = Integer.parseInt(data);
			if (val == 0 || val == 1) {
				return true;
			}
		} catch (NumberFormatException e) {
		}
		return false;
	}

	private boolean isDouble(String data) {
		try {
			Double.parseDouble(data);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public List<SMAACEAModelImporter.Type> getTypeSelectionList(int index) {
		return selectionLists.get(index);
	}
	
	private enum SelectionType {
		ALL,
		DOUBLE,		
		CHAR
	}
}
