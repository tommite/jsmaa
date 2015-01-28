/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

    JSMAA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSMAA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSMAA.  If not, see <http://www.gnu.org/licenses/>.
*/
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
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(NamedObject.PROPERTY_NAME)) { 
				fireTableDataChanged();
			}
		}
	}
}