package fi.smaa;

public abstract class SimulationThread extends Thread{

	private int iteration;
	private int totalIterations;
	private boolean go;

	public SimulationThread(int totalIterations) {
		iteration = 1;
		this.totalIterations = totalIterations;
		go = false;
	}

	public void run() {
		go = true;
		while (go && iteration < totalIterations) {
			iterate();
			iteration++;
		}
		go = false;
	}

	public abstract void iterate();

	public int getTotalIteration() {
		return totalIterations;
	}

	public int getIteration() {
		return iteration;
	}

	public void stopSimulation() {
		go = false;
	}
	
	public boolean isRunning() {
		return go;
	}
}
