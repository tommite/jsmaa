/*
	This file is part of JSMAA.
	(c) Tommi Tervonen, 2009	

    JSMAA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSMAA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSMAA.  If not, see <http://www.gnu.org/licenses/>.
*/

package fi.smaa.jsmaa.simulator;

import fi.smaa.jsmaa.model.SMAAModel;

public class SMAASimulator {
	
	private SimulationThread<?> simulationThread;
	private SMAAModel model;
	
	public SMAASimulator(SMAAModel smaaModel, SimulationThread<?> thread) {
		model = smaaModel;
		this.simulationThread = thread;
	}
	
	public Integer getTotalIterations() {
		return simulationThread.getTotalIterations();
	}

	public SMAAResults getResults() {
		return simulationThread.getResults();
	}
	
	synchronized public void stop() {
		simulationThread.stopSimulation();
	}
			
	synchronized public void restart() {
		stop();
		if (model.getAlternatives().size() == 0 || model.getCriteria().size() == 0) {
			return;
		}
		simulationThread.reset();
		simulationThread.start();
		// yield to let the simulation start and after isRunning() returns true
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
	}
	
	public boolean isRunning() {
		return simulationThread.isRunning();
	}

	public int getCurrentIteration() {
		return simulationThread.getIteration();
	}	
}
