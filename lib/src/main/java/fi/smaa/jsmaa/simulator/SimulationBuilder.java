/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

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

import org.drugis.common.threading.Task;
import org.drugis.common.threading.ThreadHandler;

import fi.smaa.jsmaa.model.SMAAModel;

public abstract class SimulationBuilder<M extends SMAAModel, R extends SMAAResults, T extends SMAASimulation<M>> implements Runnable {

	protected M model;
	protected static ThreadHandler handler = ThreadHandler.getInstance();
	private Task d_task;

	@SuppressWarnings("unchecked")
	protected SimulationBuilder(M model) {
		this.model = (M) model.deepCopy();
	}

	@SuppressWarnings("unchecked")
	public synchronized void run() {
		T simul = generateSimulation();
		if (d_task != null) {
			handler.abortTask(d_task);
		}
		R results = (R) simul.getResults();
		prepareSimulation(simul, results);		
		d_task = simul.getTask();
		handler.scheduleTask(d_task);
	}

	protected abstract void prepareSimulation(T simulation, R results);
	
	protected abstract T generateSimulation();
}
