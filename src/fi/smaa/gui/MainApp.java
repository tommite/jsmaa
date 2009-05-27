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

package fi.smaa.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import nl.rug.escher.common.gui.GUIHelper;
import nl.rug.escher.common.gui.ViewBuilder;
import sun.ExampleFileFilter;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.beans.Model;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;

import fi.smaa.Alternative;
import fi.smaa.AlternativeExistsException;
import fi.smaa.Criterion;
import fi.smaa.GaussianCriterion;
import fi.smaa.LogNormalCriterion;
import fi.smaa.OrdinalCriterion;
import fi.smaa.Rank;
import fi.smaa.SMAAModel;
import fi.smaa.SMAAResults;
import fi.smaa.SMAAResultsListener;
import fi.smaa.SMAASimulator;
import fi.smaa.UniformCriterion;
import fi.smaa.common.ImageLoader;

@SuppressWarnings("unchecked")
public class MainApp extends Model {
	
	private static final String VERSION = "0.2";
	private static final Object JSMAA_MODELFILE_EXTENSION = "jsmaa";
	private static final String PROPERTY_MODELUNSAVED = "modelUnsaved";
	private JFrame frame;
	private JSplitPane splitPane;
	private JTree leftTree;
	private SMAAModel model;
	private SMAAResults results;
	private SMAASimulator simulator;
	private ViewBuilder rightViewBuilder;
	private LeftTreeModel leftTreeModel;
	private JProgressBar simulationProgress;
	private JScrollPane rightPane;
	private JMenuItem editRenameItem;
	private JMenuItem editDeleteItem;
	private ImageLoader imageLoader = new ImageLoader("/gfx/");
	private File currentModelFile;
	private Boolean modelUnsaved = true;
	private SMAAModelListener modelListener = new SMAAModelListener();
	
	public Boolean getModelUnsaved() {
		return modelUnsaved;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MainApp app = new MainApp();
		app.startGui();
	}


	private void startGui() {
	   	initDefaultModel();
		initFrame();
		initComponents();		
		initWithModel(model);
		expandLeftMenu();
		frame.pack();
		frame.setVisible(true);	
		updateFrameTitle();
	}


	private void updateFrameTitle() {
		String appString = "JSMAA v" + VERSION;
		String file = "Untitled model";
		
		if (currentModelFile != null) {
			try {
				file = currentModelFile.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String modelSavedStar = modelUnsaved ? "*" : "";
		frame.setTitle(appString + " - " + file + modelSavedStar);
	}


	private void initDefaultModel() {
		model = new SMAAModel("model");
		model.addCriterion(new UniformCriterion("Criterion 1"));
		model.addCriterion(new GaussianCriterion("Criterion 2"));
		try {
			model.addAlternative(new Alternative("Alternative 1"));
			model.addAlternative(new Alternative("Alternative 2"));			
			model.addAlternative(new Alternative("Alternative 3"));
		} catch (AlternativeExistsException e) {
			e.printStackTrace();
		}
	}


	private void initFrame() {
		GUIHelper.initializeLookAndFeel();		
		frame = new JFrame("SMAA");
		frame.setPreferredSize(new Dimension(800, 500));
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				quitApplication();
			}
		});
	}
		
	private void rebuildRightPanel() {
		rightPane.setViewportView(rightViewBuilder.buildPanel());
	}

	private void initComponents() {
	   splitPane = new JSplitPane();
	   splitPane.setResizeWeight(0.0);	   
	   splitPane.setDividerSize(2);
	   splitPane.setDividerLocation(-1);
	   rightPane = new JScrollPane();
	   splitPane.setRightComponent(rightPane);

	   
	   frame.getContentPane().setLayout(new BorderLayout());
	   frame.getContentPane().add("Center", splitPane);
	   frame.getContentPane().add("South", createToolBar());
	   frame.setJMenuBar(createMenuBar());
	}


	private void initWithModel(SMAAModel model) {
		initLeftPanel();		
		model.addPropertyChangeListener(modelListener);
		buildNewSimulator();
		setRightViewToCriteria();
		leftTreeFocusCriteria();
	}
	
	private JComponent createToolBar() {
		simulationProgress = new JProgressBar();	
		simulationProgress.setStringPainted(true);
		JToolBar bar = new JToolBar();
		bar.add(simulationProgress);
		bar.setFloatable(false);
		return bar;
	}


	private void setRightViewToCentralWeights() {		
		rightViewBuilder = new CentralWeightsView(results);
		rebuildRightPanel();
	}
	
	private void setRightViewToRankAcceptabilities() {
		rightViewBuilder = new RankAcceptabilitiesView(results);
		rebuildRightPanel();
	}
	
	private void setRightViewToCriteria() {
		rightViewBuilder = new CriteriaListView(model);
		rebuildRightPanel();
	}
	
	public void setRightViewToAlternatives() {
		rightViewBuilder = new AlternativeInfoView(model);
		rebuildRightPanel();
	}
	
	public void setRightViewToCriterion(Criterion node) {
		rightViewBuilder = new CriterionView(node);
		rebuildRightPanel();
	}	
	
	public void setRightViewToPreferences() {
		rightViewBuilder = new PreferenceInformationView(
				new SMAAModelPreferencePresentationModel(model));
		rebuildRightPanel();
	}
	
	private void initLeftPanel() {
		leftTreeModel = new LeftTreeModel(model);
		leftTree = new JTree(new LeftTreeModel(model));
		leftTree.addTreeSelectionListener(new LeftTreeSelectionListener());
		leftTree.setEditable(true);
		splitPane.setLeftComponent(leftTree);
		LeftTreeCellRenderer renderer = new LeftTreeCellRenderer(leftTreeModel, imageLoader);
		leftTree.setCellEditor(new MyCellEditor(leftTree, renderer));
		leftTree.setCellRenderer(renderer);
	}
	
	private class MyCellEditor extends DefaultTreeCellEditor {
		
		private ArrayList<String> oldNames = new ArrayList<String>();
		private String oldName;
		
		public MyCellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
			super(tree, renderer);
			addCellEditorListener(new CellEditorListener() {
				public void editingCanceled(ChangeEvent e) {
					validateEditing();					
				}

				public void editingStopped(ChangeEvent e) {
					validateEditing();
				}

				private void validateEditing() {
					String newName = (String) getCellEditorValue();
					Object editObject = lastPath.getLastPathComponent();
					
					if (editObject instanceof Alternative) {
						if (!isValidName(newName)) {
							showErrorAlternativeExists(newName);
							leftTree.startEditingAtPath(lastPath);							
						}
					} else if (editObject instanceof Criterion) {
						if (!isValidName(newName)) {
							showErrorCriterionExists(newName);
							leftTree.startEditingAtPath(lastPath);
						}
					}
				}
			});
		}
		
		private boolean isValidName(String name) {
			return !oldNames.contains(name) || name.equals(oldName);
		}
		
		@Override
		public void prepareForEditing() {
			oldNames.clear();
			Object obj = lastPath.getLastPathComponent();
			if (obj instanceof Alternative) {
				oldName = ((Alternative) obj).getName();
				for (Alternative a : model.getAlternatives()) {
					oldNames.add(a.getName());
				}
			} else if (obj instanceof Criterion) {
				oldName = ((Criterion) obj).getName();				
				for (Criterion c : model.getCriteria()) {
					oldNames.add(c.getName());
				}
			}
			super.prepareForEditing();
		}
	}
	

	private void showErrorCriterionExists(String name) {
		JOptionPane.showMessageDialog(leftTree, "There exists a criterion with name: " + name 
				+ ", input another one.", "Input error", JOptionPane.ERROR_MESSAGE);		
	}					
	
	private void showErrorAlternativeExists(String name) {
		JOptionPane.showMessageDialog(leftTree, "There exists an alternative with name: " + name 
				+ ", input another one.", "Input error", JOptionPane.ERROR_MESSAGE);
	}	
	
	private void expandLeftMenu() {
		leftTree.expandPath(new TreePath(new Object[]{leftTreeModel.getRoot(), leftTreeModel.getAlternativesNode()}));
		leftTree.expandPath(new TreePath(new Object[]{leftTreeModel.getRoot(), leftTreeModel.getCriteriaNode()}));
		leftTree.expandPath(new TreePath(new Object[]{leftTreeModel.getRoot(), leftTreeModel.getResultsNode()}));
	}


	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
		
		JMenu fileMenu = createFileMenu();
		JMenu editMenu = createEditMenu();
		JMenu critMenu = createCriteriaMenu();	
		JMenu altMenu = createAlternativeMenu();
		JMenu resultsMenu = createResultsMenu();
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(critMenu);
		menuBar.add(altMenu);
		menuBar.add(resultsMenu);
		
		return menuBar;
	}


	private JMenu createResultsMenu() {
		JMenu resultsMenu = new JMenu("Results");
		resultsMenu.setMnemonic('r');
		JMenuItem cwItem = new JMenuItem("Central weight vectors", 
				getIcon(FileNames.ICON_CENTRALWEIGHTS));
		cwItem.setMnemonic('c');
		JMenuItem racsItem = new JMenuItem("Rank acceptability indices", 
				getIcon(FileNames.ICON_RANKACCEPTABILITIES));
		racsItem.setMnemonic('r');
		
		cwItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				setRightViewToCentralWeights();
			}
		});
		
		racsItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				setRightViewToRankAcceptabilities();
			}			
		});
		
		resultsMenu.add(cwItem);
		resultsMenu.add(racsItem);
		return resultsMenu;
	}


	private JMenu createEditMenu() {
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic('e');
		
		editRenameItem = new JMenuItem("Rename", getIcon(FileNames.ICON_RENAME));
		editRenameItem.setMnemonic('r');
		editRenameItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));		
		editRenameItem.setEnabled(false);
		editDeleteItem = new JMenuItem("Delete", getIcon(FileNames.ICON_DELETE));
		editDeleteItem.setMnemonic('d');
		editDeleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));		
		editDeleteItem.setEnabled(false);
		
		editRenameItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				menuRenameClicked();
			}			
		});
		
		editDeleteItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				menuDeleteClicked();
			}
		});
		editMenu.add(editRenameItem);
		editMenu.add(editDeleteItem);
		return editMenu;
	}


	private Icon getIcon(String name) {
		try {
			return imageLoader.getIcon(name);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	private void menuDeleteClicked() {
		Object selection = getLeftMenuSelection();
		if (selection instanceof Alternative) {
			confirmDeleteAlternative((Alternative) selection);
		} else if (selection instanceof Criterion) {
			confirmDeleteCriterion((Criterion)selection);
		}
	}


	private void confirmDeleteCriterion(Criterion criterion) {
		int conf = JOptionPane.showConfirmDialog(frame, 
				"Do you really want to delete criterion " + criterion + "?",
				"Confirm deletion",					
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				getIcon(FileNames.ICON_DELETE));
		if (conf == JOptionPane.YES_OPTION) {
			model.deleteCriterion(criterion);
		}
	}


	private void confirmDeleteAlternative(Alternative alternative) {
		int conf = JOptionPane.showConfirmDialog(frame, 
				"Do you really want to delete alternative " + alternative + "?",
				"Confirm deletion",					
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				getIcon(FileNames.ICON_DELETE));
		if (conf == JOptionPane.YES_OPTION) {
			model.deleteAlternative(alternative);
		}
	}


	private Object getLeftMenuSelection() {
		return leftTree.getSelectionPath().getLastPathComponent();
	}

	protected void menuRenameClicked() {
		leftTree.startEditingAtPath(leftTree.getSelectionPath());
	}

	private JMenu createFileMenu() {
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('f');
		
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.setMnemonic('s');
		saveItem.setIcon(getIcon(FileNames.ICON_SAVEFILE));
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		Bindings.bind(saveItem, "enabled", new PresentationModel<MainApp>(this).getModel(PROPERTY_MODELUNSAVED));
		JMenuItem saveAsItem = new JMenuItem("Save As");
		saveAsItem.setMnemonic('a');
		saveAsItem.setIcon(getIcon(FileNames.ICON_SAVEAS));
		JMenuItem openItem = new JMenuItem("Open");
		openItem.setMnemonic('o');
		openItem.setIcon(getIcon(FileNames.ICON_OPENFILE));
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));		
		JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.setMnemonic('q');
		quitItem.setIcon(getIcon(FileNames.ICON_STOP));
		
		saveItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		saveAsItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				saveAs();
			}
		});
		openItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				openOpenFileDialog();
			}
		});
		quitItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				quitApplication();
			}
		});
		
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);
		fileMenu.addSeparator();
		fileMenu.add(quitItem);		
		return fileMenu;
	}
	
	private boolean saveAs() {
		JFileChooser chooser = getFileChooser();
		int retVal = chooser.showSaveDialog(frame);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			File file = checkFileExtension(chooser.getSelectedFile());
			trySaveModel(file);
			setCurrentModelFile(file);
			updateFrameTitle();
			return true;
		} else {
			return false;
		}
	}


	private void setCurrentModelFile(File file) {
		currentModelFile = file;
	}


	private boolean trySaveModel(File file) {
		try {
			saveModel(model, file);
			return true;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "Error saving model to " + getCanonicalPath(file) + 
					", " + e.getMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	protected boolean save() {
		if (currentModelFile == null) {
			return saveAs();
		} else {
			return trySaveModel(currentModelFile);
		}
	}
	
	protected void openOpenFileDialog() {
		JFileChooser chooser = getFileChooser();
		int retVal = chooser.showOpenDialog(frame);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			try {
				loadModel(chooser.getSelectedFile());
				expandLeftMenu();	
				leftTreeFocusCriteria();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(frame,
						"Error loading model: "+ e.getMessage(), 
						"Load error", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {				
				JOptionPane.showMessageDialog(frame, "Error loading model from " +
						getCanonicalPath(chooser.getSelectedFile()) + 
						", file doesn't contain a JSMAA model.", "Load error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private String getCanonicalPath(File selectedFile) {
		try {
			return selectedFile.getCanonicalPath();
		} catch (Exception e) {
			return selectedFile.toString();
		}
	}

	private void leftTreeFocusCriteria() {
		leftTree.setSelectionPath(new TreePath(
				new Object[] {leftTreeModel.getRoot(), leftTreeModel.getCriteriaNode() }));
	}

	private void loadModel(File file) throws IOException, ClassNotFoundException {
		ObjectInputStream s = new ObjectInputStream(
				new BufferedInputStream(
						new FileInputStream(file)));
		SMAAModel loadedModel = (SMAAModel) s.readObject();
		this.model = loadedModel;
		initWithModel(model);
		setCurrentModelFile(file);
		setModelUnsaved(false);
		updateFrameTitle();		
	}


	private void saveModel(SMAAModel model, File file) throws IOException {
		ObjectOutputStream s = new ObjectOutputStream(new BufferedOutputStream(
						new FileOutputStream(file)));
		s.writeObject(model);
		s.close();
		setModelUnsaved(false);
	}


	private void setModelUnsaved(boolean b) {
		Boolean oldVal = modelUnsaved;
		this.modelUnsaved = b;
		firePropertyChange(PROPERTY_MODELUNSAVED, oldVal, this.modelUnsaved);
		updateFrameTitle();
	}

	private File checkFileExtension(File file) {
		if (ExampleFileFilter.getExtension(file) == null ||
				!ExampleFileFilter.getExtension(file).equals(JSMAA_MODELFILE_EXTENSION)) {
			return new File(file.getAbsolutePath() + "." + JSMAA_MODELFILE_EXTENSION);
		}
		return file;
	}


	private JFileChooser getFileChooser() {
		JFileChooser chooser = new JFileChooser(new File("."));
		ExampleFileFilter filter = new ExampleFileFilter();
		filter.addExtension("jsmaa");
		filter.setDescription("JSMAA model files");
		chooser.setFileFilter(filter);
		return chooser;
	}

	protected void quitApplication() {
		if (modelUnsaved) {
			int conf = JOptionPane.showConfirmDialog(frame, 
					"Model not saved. Do you want do save changes before quitting JSMAA?",
					"Save changed",					
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
					getIcon(FileNames.ICON_STOP));
			if (conf == JOptionPane.CANCEL_OPTION) {
				return;
			} else if (conf == JOptionPane.YES_OPTION) {
				if (!save()) {
					return;
				}
			}
		}
		System.exit(0);
	}


	private JMenu createAlternativeMenu() {
		JMenu alternativeMenu = new JMenu("Alternatives");
		alternativeMenu.setMnemonic('a');
		JMenuItem showItem = new JMenuItem("Show");
		showItem.setMnemonic('s');
		JMenuItem addAltButton = new JMenuItem("Add new");
		addAltButton.setMnemonic('n');
		addAltButton.setIcon(getIcon(FileNames.ICON_ADD));
		addAltButton.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));		
		
		showItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				setRightViewToAlternatives();
			}			
		});
				
		addAltButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addAlternative();
			}
		});
		alternativeMenu.add(showItem);
		alternativeMenu.addSeparator();
		alternativeMenu.add(addAltButton);
		return alternativeMenu;
	}


	private JMenu createCriteriaMenu() {
		JMenu criteriaMenu = new JMenu("Criteria");
		criteriaMenu.setMnemonic('c');
		JMenuItem showItem = new JMenuItem("Show");
		showItem.setMnemonic('s');
		showItem.setIcon(getIcon(FileNames.ICON_CRITERIALIST));
		JMenuItem addUnifItem = new JMenuItem("Add uniform");
		addUnifItem.setMnemonic('u');
		addUnifItem.setIcon(getIcon(FileNames.ICON_ADD));
		addUnifItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.CTRL_MASK));
		JMenuItem addGaussianItem = new JMenuItem("Add gaussian");
		addGaussianItem.setMnemonic('g');
		addGaussianItem.setIcon(getIcon(FileNames.ICON_ADD));
		addGaussianItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
		JMenuItem addLogNormalItem = new JMenuItem("Add lognormal");
		addLogNormalItem.setIcon(getIcon(FileNames.ICON_ADD));
		addLogNormalItem.setMnemonic('l');
		addLogNormalItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));				
		
		JMenuItem addOrdinalItem = new JMenuItem("Add ordinal");
		addOrdinalItem.setMnemonic('o');
		addOrdinalItem.setIcon(getIcon(FileNames.ICON_ADD));		
		
		showItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				setRightViewToCriteria();
			}
		});
		addUnifItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addUniformCriterion();
			}			
		});
		addGaussianItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addGaussianCriterion();
			}
		});
		addLogNormalItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addLogNormalCriterion();
			}
		});		
		addOrdinalItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addOrdinalCriterion();
			}			
		});
		criteriaMenu.add(showItem);
		criteriaMenu.addSeparator();
		criteriaMenu.add(addUnifItem);
		//toolBarAddCritMenu.add(addOrdinalButton);			
		criteriaMenu.add(addGaussianItem);
		criteriaMenu.add(addLogNormalItem);
		return criteriaMenu;
	}

	protected void addGaussianCriterion() {
		model.addCriterion(new GaussianCriterion(generateNextCriterionName()));	
	}
	
	protected void addLogNormalCriterion() {
		model.addCriterion(new LogNormalCriterion(generateNextCriterionName()));
	}	

	protected void addUniformCriterion() {
		model.addCriterion(new UniformCriterion(generateNextCriterionName()));
	}
	
	protected void addOrdinalCriterion() {
		model.addCriterion(new OrdinalCriterion(generateNextCriterionName()));
	}

	private String generateNextCriterionName() {
		List<Criterion> crit = model.getCriteria();
		
		int index = 1;
		while(true) {
			String testName = "Criterion " + index;
			boolean found = false;
			for (Criterion c : crit) {
				if (testName.equals(c.getName())) {
					found = true;
					break;
				}
			}
			if (!found) {
				return "Criterion " + index;				
			}
			index++;
		}
	}

	protected void addAlternative() {
		List<Alternative> alts = model.getAlternatives();
		
		int index = 1;
		while (true) {
			Alternative a = new Alternative("Alternative " + index);
			boolean found = false; 
			for (Alternative al : alts) {
				if (al.getName().equals(a.getName())) {
					found = true;
					break;
				}
			}
			if (!found) {
				try {
					model.addAlternative(a);
				} catch (AlternativeExistsException e) {
					throw new RuntimeException("Error: alternative with this name shouldn't exist");
				}
				return;
			}
			index++;
		}
	}
	
	private class LeftTreeSelectionListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {
			if (e.getNewLeadSelectionPath() == null) {
				setEditMenuItemsEnabled(false);				
				return;
			}
			Object node = e.getNewLeadSelectionPath().getLastPathComponent();
			if (node == leftTreeModel.getAlternativesNode()) {
				setRightViewToAlternatives();
				setEditMenuItemsEnabled(false);				
			} else if (node == leftTreeModel.getCriteriaNode()){
				setRightViewToCriteria();
				setEditMenuItemsEnabled(false);
			} else if (node instanceof Criterion) {
				setRightViewToCriterion((Criterion)node);
				setEditMenuItemsEnabled(true);
			} else if (node instanceof Alternative) {
				setEditMenuItemsEnabled(true);
			} else if (node == leftTreeModel.getCentralWeightsNode()) {
				setRightViewToCentralWeights();
				setEditMenuItemsEnabled(false);				
			} else if (node == leftTreeModel.getRankAcceptabilitiesNode()) {
				setRightViewToRankAcceptabilities();
				setEditMenuItemsEnabled(false);
			} else if (node == leftTreeModel.getModelNode()) {
				editRenameItem.setEnabled(true);
				editDeleteItem.setEnabled(false);
			} else if (node == leftTreeModel.getPreferencesNode()) {
				setRightViewToPreferences();
				setEditMenuItemsEnabled(false);
			} else {
				setEditMenuItemsEnabled(false);
			}
		}
	}
	
	private void setEditMenuItemsEnabled(boolean enable) {
		editDeleteItem.setEnabled(enable);
		editRenameItem.setEnabled(enable);
	}			

	private class SMAAModelListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			setModelUnsaved(true);
			if (evt.getSource() instanceof Criterion) {
				if (!evt.getPropertyName().equals(Criterion.PROPERTY_NAME)) {
					buildNewSimulator();
				}
			}
			if (evt.getSource() instanceof Rank) {
				buildNewSimulator();
			}
			if (evt.getSource() == model) {
				if (evt.getPropertyName().equals(SMAAModel.PROPERTY_ALTERNATIVES) ||
						evt.getPropertyName().equals(SMAAModel.PROPERTY_CRITERIA) ||
						evt.getPropertyName().equals(SMAAModel.PROPERTY_PREFERENCEINFORMATION)) {
					buildNewSimulator();
				}
				if (evt.getPropertyName().equals(SMAAModel.PROPERTY_ALTERNATIVES)) {
					setRightViewToAlternatives();
				} else if (evt.getPropertyName().equals(SMAAModel.PROPERTY_CRITERIA)) {
					setRightViewToCriteria();
				} else if (evt.getPropertyName().equals(SMAAModel.PROPERTY_PREFERENCEINFORMATION)) {
					rebuildRightPanel();				
				}
				expandLeftMenu();
			}
		}
	}

	private void buildNewSimulator() {
		if (simulator != null) {
			simulator.stop();
		}
		simulator = new SMAASimulator(model, 10000);		
		results = simulator.getResults();
		results.addResultsListener(new SimulationProgressListener());
		if (rightViewBuilder instanceof CentralWeightsView) {
			setRightViewToCentralWeights();
		} else if (rightViewBuilder instanceof RankAcceptabilitiesView) {
			setRightViewToRankAcceptabilities();
		}
		simulationProgress.setValue(0);
		simulator.restart();
	}	

	private class SimulationProgressListener implements SMAAResultsListener {
		public void resultsChanged() {
			int amount = results.getRankAccIteration() * 100 / simulator.getTotalIterations();
			simulationProgress.setValue(amount);
			if (amount < 100) {
				simulationProgress.setString("Simulating: " + Integer.toString(amount) + "% done");
			} else {
				simulationProgress.setString("Simulation complete.");
			}
		}
	}

}
