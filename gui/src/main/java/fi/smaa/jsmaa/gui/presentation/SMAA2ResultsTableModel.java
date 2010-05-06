package fi.smaa.jsmaa.gui.presentation;

import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.simulator.SMAA2Results;

@SuppressWarnings("serial")
public abstract class SMAA2ResultsTableModel extends SMAAResultsTableModel<SMAA2Results> {

	public SMAA2ResultsTableModel(SMAA2Results results) {
		super(results);
		
		for (Criterion c : results.getCriteria()) {
			c.addPropertyChangeListener(listener);
		}		
	}

}