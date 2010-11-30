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
		prepareSimulation(results);		
		handler.scheduleTask(simul.getActivityTask());
	}

	protected abstract void prepareSimulation(R results);
	
	protected abstract T generateSimulation();
}
