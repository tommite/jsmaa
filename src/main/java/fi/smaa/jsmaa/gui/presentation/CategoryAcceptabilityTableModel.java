package fi.smaa.jsmaa.gui.presentation;

import javax.swing.table.AbstractTableModel;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.simulator.SMAAResultsListener;
import fi.smaa.jsmaa.simulator.SMAATRIResults;

@SuppressWarnings("serial")
public class CategoryAcceptabilityTableModel extends AbstractTableModel {

	private SMAATRIResults results;

	public CategoryAcceptabilityTableModel(SMAATRIResults results) {
		this.results = results;
		results.addResultsListener(new ResultsListener());
	}

	public int getColumnCount() {
		return results.getCategories().size() + 1;
	}

	public int getRowCount() {
		return results.getAlternatives().size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return results.getAlternatives().get(rowIndex);
		}
		Alternative alt = results.getAlternatives().get(rowIndex);
		return results.getCategoryAcceptabilities().get(alt).get(columnIndex-1);
	}
	
	@Override
	public String getColumnName(int column) {
		if (column == 0) {
			return "Alternative";
		} else {
			return results.getCategories().get(column-1).getName();
		}
	}
	
	private class ResultsListener implements SMAAResultsListener {
		public void resultsChanged() {
			fireTableStructureChanged();
		}

		public void resultsChanged(Exception e) {
			fireTableStructureChanged();			
		}
		
	}

}
