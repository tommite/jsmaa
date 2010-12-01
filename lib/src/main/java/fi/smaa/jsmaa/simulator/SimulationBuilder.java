package fi.smaa.jsmaa.simulator;

import org.drugis.common.threading.ThreadHandler;

import fi.smaa.jsmaa.model.SMAAModel;

public abstract class SimulationBuilder<M extends SMAAModel, R extends SMAAResults, T extends SMAASimulation<M>> implements Runnable {

	protected M model;
	protected static ThreadHandler handler = ThreadHandler.getInstance();

	@SuppressWarnings("unchecked")
	protected SimulationBuilder(M model) {
		this.model = (M) model.deepCopy();
	}

	@SuppressWarnings("unchecked")
	public synchronized void run() {
		T simul = generateSimulation();
		handler.clear();
		R results = (R) simul.getResults();
		prepareSimulation(simul, results);		
		handler.scheduleTask(simul.getTask());
	}

	protected abstract void prepareSimulation(T simulation, R results);
	
	protected abstract T generateSimulation();
}
