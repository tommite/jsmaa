package fi.smaa.jsmaa.gui;

import javax.swing.JFrame;

import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.simulator.SMAA2Results;
import fi.smaa.jsmaa.simulator.SMAA2Simulation;

public class SMAA2SimulationBuilder extends BasicSimulationBuilder<SMAAModel, SMAA2Results, SMAA2Simulation> {

	public SMAA2SimulationBuilder(SMAAModel model, GUIFactory factory, JFrame frame) {
		super(model, factory, frame);
	}

	@Override
	protected SMAA2Simulation generateSimulation() {
		return new SMAA2Simulation(model, ITERATIONS );
	}

}
