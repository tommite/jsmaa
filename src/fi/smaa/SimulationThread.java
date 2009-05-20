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
