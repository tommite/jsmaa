package fi.smaa.jsmaa.gui.presentation;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.simulator.SMAA2Results;

@SuppressWarnings("serial")
public class CentralWeightTableModel extends SMAA2ResultsTableModel {

	public CentralWeightTableModel(SMAA2Results results) {
		super(results);		
	}

	@Override
	public String getColumnName(int column) {
		if (column == 0) {
			return "Alternative";
		}
		if (column == 1) {
			return "CF";
		}
		return results.getCriteria().get(column-2).getName();
	}

	public int getColumnCount() {
		return results.getCriteria().size() + 2;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Alternative alt = results.getAlternatives().get(rowIndex);
		if (columnIndex == 0) {
			return alt;
		}
		if (columnIndex == 1) {
			return results.getConfidenceFactors().get(alt);
		}
		return results.getCentralWeightVectors().get(alt).get(results.getCriteria().get(columnIndex-2));
	}

}
