package fi.smaa.jsmaa.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JFrame;

import fi.smaa.jsmaa.model.NamedObject;
import fi.smaa.jsmaa.model.SMAA2Model;
import fi.smaa.jsmaa.model.SMAATRIModel;
import fi.smaa.jsmaa.simulator.ResultsEvent;
import fi.smaa.jsmaa.simulator.SMAA2Results;
import fi.smaa.jsmaa.simulator.SMAAResults;
import fi.smaa.jsmaa.simulator.SMAAResultsListener;
import fi.smaa.jsmaa.simulator.SMAASimulator;
import fi.smaa.jsmaa.simulator.SMAATRIResults;
import fi.smaa.jsmaa.simulator.SimulationThread;

public abstract class SimulationBuilder<M extends SMAA2Model, R extends SMAAResults, T extends SimulationThread> implements Runnable {

	private static SMAASimulator simulator;
	protected M model;
	protected R results;
	private GUIFactory factory;
	private JFrame frame;

	@SuppressWarnings("unchecked")
	public SimulationBuilder(M model, GUIFactory factory, JFrame frame) {
		this.model = (M) model.deepCopy();
		this.factory = factory;
		this.frame = frame;
		
		connectNameAdapters(model.getAlternatives(), this.model.getAlternatives());
		connectNameAdapters(model.getCriteria(), this.model.getCriteria());
	}
	
	public R getResults() {
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public synchronized void run() {
		if (simulator != null) {
			simulator.stop();
		}
		T thread = generateSimulationThread();
		thread.setPriority(Thread.MIN_PRIORITY);
		simulator = new SMAASimulator(model, thread);
		results = (R) thread.getResults();
		results.addResultsListener(new SimulationProgressListener());

		if (model instanceof SMAATRIModel) {
			((SMAATRIGUIFactory)factory).setResults((SMAATRIResults) results);
		} else {
			((SMAA2GUIFactory)factory).setResults((SMAA2Results) results);				
		}
		
		factory.getProgressBar().setValue(0);
		simulator.restart();
	}

	protected abstract T generateSimulationThread();

	protected void connectNameAdapters(List<? extends NamedObject> oldModelObjects,
			List<? extends NamedObject> newModelObjects) {
		assert(oldModelObjects.size() == newModelObjects.size());
		for (int i=0;i<oldModelObjects.size();i++) {
			NamedObject mCrit = oldModelObjects.get(i);
			NamedObject nmCrit = newModelObjects.get(i);
			mCrit.addPropertyChangeListener(new NameUpdater(nmCrit));
		}
	}		

	private class NameUpdater implements PropertyChangeListener {

		private NamedObject toUpdate;
		public NameUpdater(NamedObject toUpdate) {
			this.toUpdate = toUpdate;
		}
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(NamedObject.PROPERTY_NAME)){ 
				toUpdate.setName((String) evt.getNewValue());
			}
		}
	}

	private class SimulationProgressListener implements SMAAResultsListener {
		public void resultsChanged(ResultsEvent ev) {
			if (ev.getException() == null) {
				int amount = simulator.getCurrentIteration() * 100 / simulator.getTotalIterations();
				factory.getProgressBar().setValue(amount);
				if (amount < 100) {
					factory.getProgressBar().setString("Simulating: " + Integer.toString(amount) + "% done");
				} else {
					factory.getProgressBar().setString("Simulation complete.");
				}
			} else {
				int amount = simulator.getCurrentIteration() * 100 / simulator.getTotalIterations();
				factory.getProgressBar().setValue(amount);
				factory.getProgressBar().setString("Error in simulation : " + ev.getException().getMessage());
				frame.getContentPane().remove(factory.getBottomToolBar());
				frame.getContentPane().add("South", factory.getBottomToolBar());
				frame.pack();
			}
		}
	}
}
