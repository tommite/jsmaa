package fi.smaa.jsmaa.gui;

import javax.swing.JFrame;

import fi.smaa.jsmaa.model.SMAATRIModel;
import fi.smaa.jsmaa.simulator.SMAATRIResults;
import fi.smaa.jsmaa.simulator.SMAATRISimulationThread;

public class SMAATRISimulationBuilder extends SimulationBuilder<SMAATRIModel, SMAATRIResults, SMAATRISimulationThread> {

	public SMAATRISimulationBuilder(SMAATRIModel model, SMAATRIGUIFactory factory, JFrame frame) {
		super(model, factory, frame);
		
		connectNameAdapters(model.getCategories(), this.model.getCategories());		
	}
	
	@Override
	public SMAATRISimulationThread generateSimulationThread() {
		return new SMAATRISimulationThread(model, 10000);	
	}
}
