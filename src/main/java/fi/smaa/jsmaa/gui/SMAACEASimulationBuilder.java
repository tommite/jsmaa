package fi.smaa.jsmaa.gui;

import javax.swing.JFrame;

import fi.smaa.jsmaa.model.SMAACEAModel;
import fi.smaa.jsmaa.simulator.SMAACEAResults;
import fi.smaa.jsmaa.simulator.SMAACEASimulationThread;

public class SMAACEASimulationBuilder extends SimulationBuilder<SMAACEAModel, SMAACEAResults, SMAACEASimulationThread> {

	public SMAACEASimulationBuilder(SMAACEAModel model, SMAACEAGUIFactory factory, JFrame frame) {
		super(model, factory, frame);
	}

	@Override
	protected SMAACEASimulationThread generateSimulationThread() {
		return new SMAACEASimulationThread(model, 10000, 10);
	}

}