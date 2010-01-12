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

package fi.smaa.jsmaa.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.AppInfo;
import fi.smaa.jsmaa.ModelFileManager;
import fi.smaa.jsmaa.gui.components.LambdaPanel;
import fi.smaa.jsmaa.model.ModelChangeEvent;
import fi.smaa.jsmaa.model.NamedObject;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.SMAAModelListener;
import fi.smaa.jsmaa.model.SMAATRIModel;
import fi.smaa.jsmaa.model.xml.InvalidModelVersionException;
import fi.smaa.jsmaa.model.xml.JSMAABinding;
import fi.smaa.jsmaa.simulator.ResultsEvent;
import fi.smaa.jsmaa.simulator.SMAA2Results;
import fi.smaa.jsmaa.simulator.SMAA2SimulationThread;
import fi.smaa.jsmaa.simulator.SMAAResults;
import fi.smaa.jsmaa.simulator.SMAAResultsListener;
import fi.smaa.jsmaa.simulator.SMAASimulator;
import fi.smaa.jsmaa.simulator.SMAATRIResults;
import fi.smaa.jsmaa.simulator.SMAATRISimulationThread;
import fi.smaa.jsmaa.simulator.SimulationThread;

@SuppressWarnings("serial")
public class JSMAAMainFrame extends JFrame implements MenuDirector {
	
	public static final Object JSMAA_MODELFILE_EXTENSION = "jsmaa";
	public static final String PROPERTY_MODELUNSAVED = "modelUnsaved";
	
	private SMAAResults results;
	private SMAASimulator simulator;
	private ViewBuilder rightViewBuilder;
	private JScrollPane rightPane;
	private JProgressBar simulationProgress;
	private SMAAModelListener modelListener = new MySMAAModelListener();
	private Queue<BuildSimulatorRun> buildQueue = new LinkedList<BuildSimulatorRun>();
	private Thread buildSimulatorThread;
	private GUIFactory guiFactory;
	private JToolBar bottomToolBar;
	public ModelFileManager modelManager;
	
	
	public JSMAAMainFrame(SMAAModel model) {
		super(AppInfo.getAppName());
		ToolTipManager.sharedInstance().setInitialDelay(0);		
		ImageLoader.setImagePath("/fi/smaa/jsmaa/gui");		
		setPreferredSize(new Dimension(1000, 800));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		modelManager = new ModelFileManager();
		modelManager.addPropertyChangeListener(ModelFileManager.PROPERTY_TITLE, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent ev) {
				setTitle((String)ev.getNewValue());
			}			
		});
		modelManager.addPropertyChangeListener(ModelFileManager.PROPERTY_MODEL, new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				initWithModel((SMAAModel) evt.getNewValue());
			}			
		});
		modelManager.setModel(model);
	}
	
	public void initWithModel(SMAAModel model) {
		model.addModelListener(modelListener);
		if (model instanceof SMAATRIModel) {
			guiFactory = new SMAATRIGUIFactory((SMAATRIModel) model, this);
		} else {
			guiFactory = new SMAA2GUIFactory(model, this);			
		}
		rebuildGUI();
		buildNewSimulator();
		Focuser.focus(guiFactory.getTree(), guiFactory.getTreeModel(), guiFactory.getTreeModel().getCriteriaNode());
	}	
	
	private void rebuildGUI() {
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.1);	   
		splitPane.setDividerSize(2);
		splitPane.setDividerLocation(-1);
		
		rightPane = new JScrollPane();
		rightPane.getVerticalScrollBar().setUnitIncrement(16);		   
		splitPane.setRightComponent(rightPane);
		
		JScrollPane leftScrollPane = new JScrollPane();
		leftScrollPane.setViewportView(guiFactory.getTree());
		splitPane.setLeftComponent(leftScrollPane);		

		getContentPane().removeAll();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", splitPane);
		getContentPane().add("North", guiFactory.getTopToolBar());
				
		bottomToolBar = new JToolBar();
		simulationProgress = new JProgressBar();	
		simulationProgress.setStringPainted(true);		
		bottomToolBar.add(simulationProgress);
		bottomToolBar.setFloatable(false);
		if (modelManager.getModel() instanceof SMAATRIModel) {
			bottomToolBar.add(new LambdaPanel((SMAATRIModel) modelManager.getModel()));
		}
		getContentPane().add("South", bottomToolBar);
		setJMenuBar(guiFactory.getMenuBar());
		
		guiFactory.getTree().addTreeSelectionListener(new LeftTreeSelectionListener());
		pack();
	}

	private void rebuildRightPanel() {
		rightPane.setViewportView(rightViewBuilder.buildPanel());
	}

	public void quit() {
		for (WindowListener w : getWindowListeners()) {
			w.windowClosing(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}

	public void newModel(SMAAModel newModel) {
		if (!checkSaveCurrentModel()) {
			return;
		}
		modelManager.setModel(newModel);
	}

	public boolean saveAs() {
		JFileChooser chooser = getFileChooser();
		int retVal = chooser.showSaveDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File file = checkFileExtension(chooser.getSelectedFile());
			trySaveModel(file);
			modelManager.setModelFile(file);
			return true;
		} else {
			return false;
		}
	}
	
	private boolean checkSaveCurrentModel() {
		if (!modelManager.getSaved()) {
			int conf = JOptionPane.showConfirmDialog(this, 
					"Current model not saved. Do you want do save changes?",
					"Save changed",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
					ImageLoader.getIcon(FileNames.ICON_STOP));
			if (conf == JOptionPane.CANCEL_OPTION) {
				return false;
			} else if (conf == JOptionPane.YES_OPTION) {
				if (!save()) {
					return false;
				}
			}
		}
		return true;
	}
		

	private boolean trySaveModel(File file) {
		try {
			FileOutputStream fos = new FileOutputStream(file);
			JSMAABinding.writeModel(modelManager.getModel(), new BufferedOutputStream(fos));
			fos.close();
			modelManager.setSaved(true);
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error saving model to " + getCanonicalPath(file) + 
					", " + e.getMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public boolean save() {
		if (modelManager.getModelFile() == null) {
			return saveAs();
		} else {
			return trySaveModel(modelManager.getModelFile());
		}
	}
	
	public void open() {
		if (!checkSaveCurrentModel()) {
			return;
		}
		JFileChooser chooser = getFileChooser();
		int retVal = chooser.showOpenDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			try {
				File file = chooser.getSelectedFile();
				InputStream fis = new FileInputStream(file);
				SMAAModel loadedModel = JSMAABinding.readModel(new BufferedInputStream(fis));
				fis.close();

				modelManager.setModel(loadedModel);
				modelManager.setModelFile(file);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this,
						"Error loading model: "+ e.getMessage(), 
						"Load error", JOptionPane.ERROR_MESSAGE);
			} catch (InvalidModelVersionException e) {				
				showErrorIncompatibleModel(chooser, "file contains a an incompatible JSMAA model version " + e.getVersion()
						+ ".\nOnly versions until " + SMAAModel.MODELVERSION 
						+ " supported.\nTo open the file, upgrade to a newer version of JSMAA (www.smaa.fi)");
			} catch (Exception e) {
				e.printStackTrace();
				showErrorIncompatibleModel(chooser, "file doesn't dontain a JSMAA model");				
			}
		}
	}

	private void showErrorIncompatibleModel(JFileChooser chooser, String reason) {
		JOptionPane.showMessageDialog(this, "Error loading model from " +
				getCanonicalPath(chooser.getSelectedFile()) + 
				": " + reason + ".", "Load error", JOptionPane.ERROR_MESSAGE);
	}

	private String getCanonicalPath(File selectedFile) {
		try {
			return selectedFile.getCanonicalPath();
		} catch (Exception e) {
			return selectedFile.toString();
		}
	}

	private File checkFileExtension(File file) {
		if (MyFileFilter.getExtension(file) == null ||
				!MyFileFilter.getExtension(file).equals(JSMAA_MODELFILE_EXTENSION)) {
			return new File(file.getAbsolutePath() + "." + JSMAA_MODELFILE_EXTENSION);
		}
		return file;
	}

	private JFileChooser getFileChooser() {
		JFileChooser chooser = new JFileChooser(new File("."));
		MyFileFilter filter = new MyFileFilter();
		filter.addExtension("jsmaa");
		filter.setDescription("JSMAA model files");
		chooser.setFileFilter(filter);
		return chooser;
	}
	
	private class LeftTreeSelectionListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {
			if (e.getNewLeadSelectionPath() == null) {				
				return;
			}
			Object node = e.getNewLeadSelectionPath().getLastPathComponent();
			rightViewBuilder = guiFactory.buildView(node);
			rebuildRightPanel();
		}
	}
	
	private class MySMAAModelListener implements SMAAModelListener {
		public void modelChanged(ModelChangeEvent ev) {
			buildNewSimulator();
			switch (ev.getType()) {
			case ModelChangeEvent.CRITERIA:
			case ModelChangeEvent.ALTERNATIVES:
			case ModelChangeEvent.CATEGORIES:
				Focuser.focus(guiFactory.getTree(), guiFactory.getTreeModel(), guiFactory.getTreeModel().getModelNode());
				break;
			case ModelChangeEvent.MEASUREMENT:
			case ModelChangeEvent.MEASUREMENT_TYPE:
			case ModelChangeEvent.PREFERENCES:
				break;
			default:
				rebuildRightPanel();
			}
		}
	}

	synchronized private void buildNewSimulator() {
		buildQueue.add(new BuildSimulatorRun());
		if (buildSimulatorThread == null) {
			buildSimulatorThread = new Thread(buildQueue.poll());
			buildSimulatorThread.start();
		}
	}
	
	synchronized private void checkStartNewSimulator() {
		if (buildQueue.isEmpty()) {
			buildSimulatorThread = null;
		} else {
			buildSimulatorThread = new Thread(buildQueue.poll());
			buildSimulatorThread.start();
			buildQueue.clear();
		}
	}
	
	private class BuildSimulatorRun implements Runnable {
		public void run() {
			if (simulator != null) {
				simulator.stop();
			}
			SMAAModel newModel = modelManager.getModel().deepCopy();
			
			connectNameAdapters(modelManager.getModel().getAlternatives(), newModel.getAlternatives());
			connectNameAdapters(modelManager.getModel().getCriteria(), newModel.getCriteria());
			if (newModel instanceof SMAATRIModel) {
				connectNameAdapters(((SMAATRIModel) modelManager.getModel()).getCategories(), 
						((SMAATRIModel) newModel).getCategories());
			}
			
			SimulationThread thread = null;
			if (newModel instanceof SMAATRIModel) {
				thread = new SMAATRISimulationThread((SMAATRIModel) newModel, 10000);				
			} else {
				thread = new SMAA2SimulationThread(newModel, 10000);
			}
			simulator = new SMAASimulator(newModel, thread);
			results = thread.getResults();
			results.addResultsListener(new SimulationProgressListener());

			if (modelManager.getModel() instanceof SMAATRIModel) {
				((SMAATRIGUIFactory)guiFactory).setResults((SMAATRIResults) results);
			} else {
				((SMAA2GUIFactory)guiFactory).setResults((SMAA2Results) results);				
			}
			
			simulationProgress.setValue(0);
			simulator.restart();
			checkStartNewSimulator();
		}
	}
	
	public MenuDirector getMenuDirector() {
		return this;
	}
	
	private void connectNameAdapters(List<? extends NamedObject> oldModelObjects,
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
				modelManager.setSaved(false);
				toUpdate.setName((String) evt.getNewValue());
			}
		}
	}
	
	private class SimulationProgressListener implements SMAAResultsListener {
		public void resultsChanged(ResultsEvent ev) {
			if (ev.getException() == null) {
				int amount = simulator.getCurrentIteration() * 100 / simulator.getTotalIterations();
				simulationProgress.setValue(amount);
				if (amount < 100) {
					simulationProgress.setString("Simulating: " + Integer.toString(amount) + "% done");
				} else {
					simulationProgress.setString("Simulation complete.");
				}
			} else {
				int amount = simulator.getCurrentIteration() * 100 / simulator.getTotalIterations();
				simulationProgress.setValue(amount);
				simulationProgress.setString("Error in simulation : " + ev.getException().getMessage());
				getContentPane().remove(bottomToolBar);
				getContentPane().add("South", bottomToolBar);
				pack();
			}
		}
	}
}
