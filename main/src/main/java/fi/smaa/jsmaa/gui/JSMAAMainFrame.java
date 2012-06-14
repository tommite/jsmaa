/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper 2012.

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

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.drugis.common.gui.FileLoadDialog;
import org.drugis.common.gui.FileSaveDialog;
import org.drugis.common.gui.ViewBuilder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.StandardChartTheme;

import fi.smaa.jsmaa.AppInfo;
import fi.smaa.jsmaa.ModelFileManager;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ModelChangeEvent;
import fi.smaa.jsmaa.model.NamedObject;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.SMAAModelListener;
import fi.smaa.jsmaa.model.SMAATRIModel;
import fi.smaa.jsmaa.model.xml.InvalidModelVersionException;
import fi.smaa.jsmaa.model.xml.JSMAABinding;
import fi.smaa.jsmaa.simulator.BuildQueue;

@SuppressWarnings("serial")
public class JSMAAMainFrame extends JFrame implements MenuDirector {
	
	public static final Object JSMAA_MODELFILE_EXTENSION = "jsmaa";
	
	private ViewBuilder rightViewBuilder;
	private JScrollPane rightPane;
	private SMAAModelListener modelListener = new MySMAAModelListener();
	private GUIFactory guiFactory;
	public ModelFileManager modelManager;
	public BuildQueue buildQueue = new BuildQueue();
	public NameListener nameListener = new NameListener();
	
	public JSMAAMainFrame(SMAAModel model) {
		super(AppInfo.getAppName());
		
		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());		
		ToolTipManager.sharedInstance().setInitialDelay(0);		
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
		if (model instanceof SMAATRIModel) {
			guiFactory = new SMAATRIGUIFactory(this, (SMAATRIModel) model, this);
		} else {
			guiFactory = new SMAA2GUIFactory(this, model, this);			
		}		
		rebuildGUI();
		buildNewSimulator();
		model.addModelListener(modelListener);		
		Focuser.focus(guiFactory.getTree(), guiFactory.getTreeModel(), guiFactory.getTreeModel().getCriteriaNode());
		reconnectNameListeners();
	}	
	
	private void reconnectNameListeners() {
		for (Alternative a : modelManager.getModel().getAlternatives()) {
			a.addPropertyChangeListener(nameListener);
		}
		if (modelManager.getModel() instanceof SMAATRIModel) {
			for (Alternative cat : ((SMAATRIModel) modelManager.getModel()).getCategories()) {
				cat.addPropertyChangeListener(nameListener);
			}
		}
		for (Criterion c : modelManager.getModel().getCriteria()) {
			c.addPropertyChangeListener(nameListener);
		}		
		
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
		getContentPane().add("South", guiFactory.getBottomToolBar());
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
		final boolean[] success = new boolean[] { false };
		FileSaveDialog d = new FileSaveDialog(this, "jsmaa", "JSMAA model files") {
			public void doAction(String path, String extension) {
				File file = checkFileExtension(new File(path));
				success[0] = trySaveModel(file);
				modelManager.setModelFile(file);
			}
		};
		d.saveActions();
		return success[0];
	}

	private boolean checkSaveCurrentModel() {
		if (!modelManager.getSaved()) {
			int conf = JOptionPane.showConfirmDialog(this, 
					"Current model not saved. Do you want do save changes?",
					"Save changed",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
					ImageFactory.IMAGELOADER.getIcon(FileNames.ICON_STOP));
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
		FileLoadDialog dialog = new FileLoadDialog(this, "jsmaa", "JSMAA model files") {
			@Override
			public void doAction(String path, String extension) {
			try {		
				File file = new File(path);
				InputStream fis = new FileInputStream(file);
				SMAAModel loadedModel = JSMAABinding.readModel(new BufferedInputStream(fis));
				fis.close();

				modelManager.setModel(loadedModel);
				modelManager.setModelFile(file);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(JSMAAMainFrame.this,
						"Error loading model: "+ e.getMessage(), 
						"Load error", JOptionPane.ERROR_MESSAGE);
			} catch (InvalidModelVersionException e) {				
				showErrorIncompatibleModel(path, "file contains a an incompatible JSMAA model version " + e.getVersion()
						+ ".\nOnly versions until " + SMAAModel.MODELVERSION 
						+ " supported.\nTo open the file, upgrade to a newer version of JSMAA (www.smaa.fi)");
			} catch (Exception e) {
				e.printStackTrace();
				showErrorIncompatibleModel(path, "file doesn't dontain a JSMAA model");				
			}
			}
		};
		dialog.loadActions();
	}

	private void showErrorIncompatibleModel(String file, String reason) {
		JOptionPane.showMessageDialog(this, "Error loading model from " +
				file + ": " + reason + ".", "Load error", JOptionPane.ERROR_MESSAGE);
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
				reconnectNameListeners();
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

	private void buildNewSimulator() {
		if (modelManager.getModel() instanceof SMAATRIModel) {
			buildQueue.add(new SMAATRISimulationBuilder((SMAATRIModel) modelManager.getModel(), guiFactory, this));
		} else {
			buildQueue.add(new SMAA2SimulationBuilder(modelManager.getModel(), guiFactory, this));			
		}
	}
	
	public MenuDirector getMenuDirector() {
		return this;
	}
	
	@Override
	public ModelFileManager getFileManager() {
		return modelManager;
	}
	
	private class NameListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent ev) {
			if (ev.getPropertyName().equals(NamedObject.PROPERTY_NAME)) {
				if (!ev.getNewValue().equals(ev.getOldValue())) {					
					modelManager.setSaved(false);
				}
			}
		}		
	}
}
