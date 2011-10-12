package fi.smaa.jsmaa.simulator;

import static org.junit.Assert.assertFalse;

import org.drugis.common.threading.AbortedException;
import org.drugis.common.threading.AbstractSuspendable;
import org.drugis.common.threading.SimpleSuspendableTask;
import org.drugis.common.threading.Task;
import org.drugis.common.threading.TaskUtil;
import org.drugis.common.threading.ThreadHandler;
import org.junit.Test;

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
			return new SMAA2Simulation(model, 10000);
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
