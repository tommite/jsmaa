/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.

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

import java.util.LinkedList;
import java.util.Queue;

@SuppressWarnings("rawtypes")
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
