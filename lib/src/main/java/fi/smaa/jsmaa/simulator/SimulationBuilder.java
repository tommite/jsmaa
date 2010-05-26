package fi.smaa.jsmaa.simulator;

import fi.smaa.jsmaa.model.SMAAModel;

public abstract class SimulationBuilder<M extends SMAAModel, R extends SMAAResults, T extends SimulationThread<M>> implements Runnable {

	protected M model;
	protected static SMAASimulator simulator;

	@SuppressWarnings("unchecked")
	protected SimulationBuilder(M model) {
		this.model = (M) model.deepCopy();
	}

	@SuppressWarnings("unchecked")
	public synchronized void run() {
		if (simulator != null) {
			simulator.stop();
		}
		T thread = generateSimulationThread();
		thread.setPriority(Thread.MIN_PRIORITY);
		simulator = new SMAASimulator(model, thread);
		R results = (R) thread.getResults();
		prepareSimulation(results);
		simulator.restart();
	}

	protected abstract void prepareSimulation(R results);
	
	protected abstract T generateSimulationThread();
}
