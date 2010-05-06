package fi.smaa.jsmaa.gui;

import java.util.LinkedList;
import java.util.Queue;

@SuppressWarnings("unchecked")
public class BuildQueue {
	private Queue<SimulationBuilder> buildQueue = new LinkedList<SimulationBuilder>();
	private BuilderThread buildSimulatorThread;
	
	synchronized public void add(SimulationBuilder builder) {
		buildQueue.add(builder);
		if (buildSimulatorThread == null) {
			buildSimulatorThread = new BuilderThread();
			buildSimulatorThread.start();
		}
	}
	
	private class BuilderThread extends Thread {
		@Override
		public void run() {
			while (!buildQueue.isEmpty()) {
				SimulationBuilder simulationBuilder = getAndEmptyQueue();
				simulationBuilder.run();
			}
			buildSimulatorThread = null;
		}
	}
	
	synchronized private SimulationBuilder getAndEmptyQueue() {
		SimulationBuilder builder = buildQueue.poll();
		buildQueue.clear();
		return builder;
	}
}
