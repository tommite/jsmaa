package fi.smaa.jsmaa.gui.presentation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.table.AbstractTableModel;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.NamedObject;
import fi.smaa.jsmaa.simulator.ResultsEvent;
import fi.smaa.jsmaa.simulator.SMAAResults;
import fi.smaa.jsmaa.simulator.SMAAResultsListener;

@SuppressWarnings("serial")
public abstract class SMAAResultsTableModel<T extends SMAAResults> extends AbstractTableModel {

	protected T results;
	protected NameListener listener = new NameListener(); 
	private ResultsListener resultsListener = new ResultsListener();

	public SMAAResultsTableModel(T results) {
		setResults(results);
	}

	synchronized public void setResults(T results) {
		if (this.results != null) {
			this.results.removeResultsListener(resultsListener);
			for (Alternative a : this.results.getAlternatives()) {
				a.removePropertyChangeListener(listener);
			}			
		}
		
		this.results = results;
		results.addResultsListener(resultsListener);
		
		for (Alternative a : results.getAlternatives()) {
			a.addPropertyChangeListener(listener);
		}
		fireTableStructureChanged();
	}
	
	@Override
	synchronized public void fireTableDataChanged() {
		super.fireTableDataChanged();
	}
	

	private class ResultsListener implements SMAAResultsListener {
		public void resultsChanged(ResultsEvent ev) {
			fireTableDataChanged();
		}	
	}
	
	@Override
	public abstract String getColumnName(int column);

	public int getRowCount() {
		return results.getAlternatives().size();
	}
	
	protected class NameListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(NamedObject.PROPERTY_NAME)) { 
				fireTableDataChanged();
			}
		}
	}
}