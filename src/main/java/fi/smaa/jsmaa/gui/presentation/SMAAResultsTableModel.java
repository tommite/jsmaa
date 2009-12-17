package fi.smaa.jsmaa.gui.presentation;

import javax.swing.table.AbstractTableModel;

import fi.smaa.jsmaa.simulator.SMAAResults;
import fi.smaa.jsmaa.simulator.SMAAResultsListener;

@SuppressWarnings("serial")
public abstract class SMAAResultsTableModel<T extends SMAAResults> extends AbstractTableModel {

	protected T results;

	public SMAAResultsTableModel(T results) {
		this.results = results;
		results.addResultsListener(new ResultsListener());		
	}
	
	private class ResultsListener implements SMAAResultsListener {
		public void resultsChanged() {
			fireTableDataChanged();
		}

		public void resultsChanged(Exception e) {
			fireTableStructureChanged();			
		}	
	}
	
	@Override
	public abstract String getColumnName(int column);

	public int getRowCount() {
		return results.getAlternatives().size();
	}
}