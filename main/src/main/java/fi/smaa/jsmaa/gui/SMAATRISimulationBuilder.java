package fi.smaa.jsmaa.gui;

import javax.swing.JFrame;

import fi.smaa.jsmaa.model.SMAATRIModel;
import fi.smaa.jsmaa.simulator.SMAATRIResults;
import fi.smaa.jsmaa.simulator.SMAATRISimulation;

public class SMAATRISimulationBuilder extends BasicSimulationBuilder<SMAATRIModel, SMAATRIResults, SMAATRISimulation> {

	public SMAATRISimulationBuilder(SMAATRIModel model, GUIFactory factory, JFrame frame) {
		super(model, factory, frame);
		
		connectNameAdapters(model.getCategories(), this.model.getCategories());		
	}
	
	@Override
	public SMAATRISimulation generateSimulation() {
		return new SMAATRISimulation(model, ITERATIONS);	
	}
}
