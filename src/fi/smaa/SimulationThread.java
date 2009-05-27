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

package fi.smaa;

import java.util.ArrayList;
import java.util.List;

public class SimulationThread extends Thread{

	private int iteration;
	private SimulationPhase currentPhase;
	private boolean go;
	private List<SimulationPhase> phases = new ArrayList<SimulationPhase>();
	private List<Integer> phaseIterations = new ArrayList<Integer>();

	public SimulationThread() {
		currentPhase = null;
		iteration = 0;
		go = false;
	}
	
	public void addPhase(SimulationPhase phase, int iterations) {
		assert(iterations > 0);
		phases.add(phase);
		phaseIterations.add(iterations);
	}

	public void run() {
		go = true;
		for (int i=0;go && i<phases.size();i++) {
			currentPhase = phases.get(i);
			iteration = 1;
			int currentTotalIters = phaseIterations.get(i);
			while (go && iteration < currentTotalIters) {
				currentPhase.iterate();
				iteration++;
			}
		}
		go = false;
	}

	public int getTotalIteration() {
		int total = 0;
		for (Integer i : phaseIterations) {
			total += i;
		}
		return total;
	}

	public int getIteration() {
		int total = 0;
		for (int i=0;i<phases.size();i++) {
			if (currentPhase == phases.get(i)) {
				break;
			}
			total += phaseIterations.get(i);
		}
		return total + iteration;
	}

	public void stopSimulation() {
		go = false;
	}
	
	public boolean isRunning() {
		return go;
	}
}
