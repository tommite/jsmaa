package fi.smaa.jsmaa.gui.presentation;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.NamedObject;
import fi.smaa.jsmaa.simulator.SMAATRIResults;

@SuppressWarnings("serial")
public class CategoryAcceptabilityTableModel extends SMAAResultsTableModel<SMAATRIResults> {
	
	private CategoryListener catListener = new CategoryListener();

	public CategoryAcceptabilityTableModel(SMAATRIResults results) {
		super(results);
		
		for (Alternative a : results.getCategories()) {
			a.addPropertyChangeListener(catListener);
		}
	}

	public int getColumnCount() {
		return results.getCategories().size() + 1;
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
	
	private class CategoryListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(NamedObject.PROPERTY_NAME)) {
				fireTableStructureChanged();
			}
		}
	}

}
