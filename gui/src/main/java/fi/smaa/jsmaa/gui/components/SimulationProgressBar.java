package fi.smaa.jsmaa.gui.components;

import javax.swing.JProgressBar;

import fi.smaa.jsmaa.simulator.ResultsEvent;
import fi.smaa.jsmaa.simulator.SMAAResultsListener;

@SuppressWarnings("serial")
public class SimulationProgressBar extends JProgressBar {
	
	public SimulationProgressBar() {
		setStringPainted(true);
	}
	
	
	@SuppressWarnings("unused")
	private class SimulationProgressListener implements SMAAResultsListener {
		public void resultsChanged(ResultsEvent ev) {
		}
	}
}
