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

	public SMAAResultsTableModel(T results) {
		this.results = results;
		results.addResultsListener(new ResultsListener());
		
		for (Alternative a : results.getAlternatives()) {
			a.addPropertyChangeListener(NamedObject.PROPERTY_NAME, listener);
		}
	}
	
	private class ResultsListener implements SMAAResultsListener {
		public void resultsChanged(ResultsEvent ev) {
			if (ev.getException() == null) {
				fireTableDataChanged();
			} else {
				fireTableStructureChanged();			
			}	
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
			fireTableDataChanged();
		}
	}
}