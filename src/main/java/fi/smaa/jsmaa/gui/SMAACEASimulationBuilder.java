package fi.smaa.jsmaa.gui;

import javax.swing.JFrame;

import fi.smaa.jsmaa.model.SMAACEAModel;
import fi.smaa.jsmaa.simulator.SMAACEASimulationThread;
import fi.smaa.jsmaa.simulator.SMAARankAcceptabilityResults;

public class SMAACEASimulationBuilder extends SimulationBuilder<SMAACEAModel, SMAARankAcceptabilityResults, SMAACEASimulationThread> {

	public SMAACEASimulationBuilder(SMAACEAModel model, SMAACEAGUIFactory factory, JFrame frame) {
		super(model, factory, frame);
	}

	@Override
	protected SMAACEASimulationThread generateSimulationThread() {
		return new SMAACEASimulationThread(model, 10000);
	}

}