package fi.smaa.jsmaa.gui.components;

import javax.swing.JProgressBar;

import fi.smaa.jsmaa.simulator.ResultsEvent;
import fi.smaa.jsmaa.simulator.SMAAResultsListener;
import fi.smaa.jsmaa.simulator.SMAASimulator;

@SuppressWarnings("serial")
public class SimulationProgressBar extends JProgressBar {
	
	private SMAASimulator simulator;

	public SimulationProgressBar() {
		setStringPainted(true);
	}
	
	public void setSimulator(SMAASimulator simulator) {
		this.simulator = simulator;
		simulator.getResults().addResultsListener(new SimulationProgressListener());
	}
	
	private class SimulationProgressListener implements SMAAResultsListener {
		public void resultsChanged(ResultsEvent ev) {
			if (ev.getException() == null) {
				int amount = simulator.getCurrentIteration() * 100 / simulator.getTotalIterations();
				setValue(amount);
				if (amount < 100) {
					setString("Simulating: " + Integer.toString(amount) + "% done");
				} else {
					setString("Simulation complete.");
				}
			} else {
				int amount = simulator.getCurrentIteration() * 100 / simulator.getTotalIterations();
				setValue(amount);
				setString("Error in simulation : " + ev.getException().getMessage());
			}
		}
	}
}
