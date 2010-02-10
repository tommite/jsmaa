package fi.smaa.jsmaa.gui;

import javax.swing.JFrame;

import fi.smaa.jsmaa.model.SMAA2Model;
import fi.smaa.jsmaa.simulator.SMAA2Results;
import fi.smaa.jsmaa.simulator.SMAA2SimulationThread;

public class SMAA2SimulationBuilder extends SimulationBuilder<SMAA2Model, SMAA2Results, SMAA2SimulationThread> {

	public SMAA2SimulationBuilder(SMAA2Model model, SMAA2GUIFactory factory, JFrame frame) {
		super(model, factory, frame);
	}

	@Override
	protected SMAA2SimulationThread generateSimulationThread() {
		return new SMAA2SimulationThread(model, 10000);
	}

}
