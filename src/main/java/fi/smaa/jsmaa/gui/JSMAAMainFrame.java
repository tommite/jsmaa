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

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.AppInfo;
import fi.smaa.jsmaa.DefaultModels;
import fi.smaa.jsmaa.ModelFileManager;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ModelChangeEvent;
import fi.smaa.jsmaa.model.NamedObject;
import fi.smaa.jsmaa.model.SMAA2Model;
import fi.smaa.jsmaa.model.SMAAModelListener;
import fi.smaa.jsmaa.model.SMAATRIModel;
import fi.smaa.jsmaa.model.xml.InvalidModelVersionException;
import fi.smaa.jsmaa.model.xml.JSMAABinding;

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
	
	public JSMAAMainFrame(SMAA2Model model) {
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
				initWithModel((SMAA2Model) evt.getNewValue());
			}			
		});
		modelManager.setModel(model);
	}
	
	public void initWithModel(SMAA2Model model) {
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

	public void newModel(MenuDirector.ModelType type) {
		if (!checkSaveCurrentModel()) {
			return;
		}
		if (type.equals(MenuDirector.ModelType.SMAA2)) {
			modelManager.setModel(DefaultModels.getSMAA2Model());
		} else if (type.equals(MenuDirector.ModelType.SMAATRI)) {
			modelManager.setModel(DefaultModels.getSMAATRIModel());
		} else if (type.equals(MenuDirector.ModelType.SMAACEA)) {
			new SMAACEAModelLoaderWizard(modelManager).start(this);
		}
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
				SMAA2Model loadedModel = JSMAABinding.readModel(new BufferedInputStream(fis));
				fis.close();

				modelManager.setModel(loadedModel);
				modelManager.setModelFile(file);
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this,
						"Error loading model: "+ e.getMessage(), 
						"Load error", JOptionPane.ERROR_MESSAGE);
			} catch (InvalidModelVersionException e) {				
				showErrorIncompatibleModel(chooser, "file contains a an incompatible JSMAA model version " + e.getVersion()
						+ ".\nOnly versions until " + SMAA2Model.MODELVERSION 
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
