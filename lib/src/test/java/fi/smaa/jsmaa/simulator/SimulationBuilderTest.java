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

import static org.junit.Assert.assertFalse;

import org.drugis.common.threading.AbortedException;
import org.drugis.common.threading.AbstractSuspendable;
import org.drugis.common.threading.SimpleSuspendableTask;
import org.drugis.common.threading.Task;
import org.drugis.common.threading.TaskUtil;
import org.drugis.common.threading.ThreadHandler;
import org.junit.Test;

import fi.smaa.common.RandomUtil;
import fi.smaa.jsmaa.model.SMAAModel;

public class SimulationBuilderTest {
	public static class SleepySuspendable extends AbstractSuspendable {
		private final int d_ms;
		boolean d_done;

		public SleepySuspendable(int ms) {
			d_ms = ms;
		}
		
		public synchronized boolean getDone() {
			return d_done;
		}
		
		public void run() {
			if (d_done)
				throw new IllegalStateException("Thread already done.");
			try {
				Thread.sleep(d_ms);
				waitIfSuspended();
			} catch (InterruptedException e) {
			} catch (AbortedException t) {
			}
			d_done = true;
		}
	};
	
	public class SimpleSimulationBuilder extends SimulationBuilder<SMAAModel, SMAAResults, SMAA2Simulation> {

		protected SimpleSimulationBuilder(SMAAModel model) {
			super(model);
		}

		protected SMAA2Simulation generateSimulation() {
			return new SMAA2Simulation(model, RandomUtil.createWithFixedSeed(), 10000);
		}
		
		protected void prepareSimulation(SMAA2Simulation simulation, SMAAResults results) {
		}
		
	}
	
	/**
	 * Test whether restarting a simulation does not abort other tasks.
	 * The previous behavior (ThreadHandler.clear()) caused tasks to disappear in ADDIS. 
	 * @see SimulationBuilder.run
	 * @see http://mantis.drugis.org/view.php?id=373
	 */
	@Test
	public void testParallelSimulationsDoNotInterfere() throws InterruptedException {
		Task otherTask = new SimpleSuspendableTask(new SleepySuspendable(1000));
		ThreadHandler th = ThreadHandler.getInstance();
		th.scheduleTask(otherTask);
		SimpleSimulationBuilder builder = new SimpleSimulationBuilder(new SMAAModel("test"));  
		builder.run();
		Thread.sleep(250);
		builder.run();

		TaskUtil.waitUntilReady(otherTask);
		assertFalse(otherTask.isAborted());
	}	
}
