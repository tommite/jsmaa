/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

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
import fi.smaa.jsmaa.simulator.SMAASimulation;

public abstract class BasicSimulationBuilder<M extends SMAAModel, R extends SMAAResults, T extends SMAASimulation<M>> 
	extends SimulationBuilder<M, R, T> {

	private GUIFactory factory;
	private JFrame frame;
	public int ITERATIONS = 10000;

	public BasicSimulationBuilder(M model, GUIFactory factory, JFrame frame) {
		super(model);
		this.factory = factory;
		this.frame = frame;
		
		connectNameAdapters(model.getAlternatives(), this.model.getAlternatives());
		connectNameAdapters(model.getCriteria(), this.model.getCriteria());
	}
	
	@Override
	protected void prepareSimulation(T simulation, R results) {
		results.addResultsListener(new SimulationProgressListener());

		if (model instanceof SMAATRIModel) {
			((SMAATRIGUIFactory)factory).setResults((SMAATRIResults) results);
		} else {
			((SMAA2GUIFactory)factory).setResults((SMAA2Results) results);				
		}
		
		factory.getProgressModel().setTask(simulation.getTask());
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
			if (ev.getException() != null) {
				frame.getContentPane().remove(factory.getBottomToolBar());
				frame.getContentPane().add("South", factory.getBottomToolBar());
				frame.pack();
			}
		}
	}
}
