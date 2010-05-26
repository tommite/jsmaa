package fi.smaa.jsmaa.gui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JFrame;

import fi.smaa.jsmaa.model.NamedObject;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.SMAATRIModel;
import fi.smaa.jsmaa.simulator.ResultsEvent;
import fi.smaa.jsmaa.simulator.SMAA2Results;
import fi.smaa.jsmaa.simulator.SMAAResults;
import fi.smaa.jsmaa.simulator.SMAAResultsListener;
import fi.smaa.jsmaa.simulator.SMAATRIResults;
import fi.smaa.jsmaa.simulator.SimulationBuilder;
import fi.smaa.jsmaa.simulator.SimulationThread;

public abstract class BasicSimulationBuilder<M extends SMAAModel, R extends SMAAResults, T extends SimulationThread<M>> 
	extends SimulationBuilder<M, R, T> {

	private GUIFactory factory;
	private JFrame frame;

	public BasicSimulationBuilder(M model, GUIFactory factory, JFrame frame) {
		super(model);
		this.factory = factory;
		this.frame = frame;
		
		connectNameAdapters(model.getAlternatives(), this.model.getAlternatives());
		connectNameAdapters(model.getCriteria(), this.model.getCriteria());
	}
	
	protected void prepareSimulation(R results) {
		results.addResultsListener(new SimulationProgressListener());

		if (model instanceof SMAATRIModel) {
			((SMAATRIGUIFactory)factory).setResults((SMAATRIResults) results);
		} else {
			((SMAA2GUIFactory)factory).setResults((SMAA2Results) results);				
		}
		
		factory.getProgressBar().setValue(0);
	}

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
