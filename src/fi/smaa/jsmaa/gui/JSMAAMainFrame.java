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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;

import fi.smaa.common.ImageLoader;
import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.DefaultModels;
import fi.smaa.jsmaa.SMAA2Results;
import fi.smaa.jsmaa.SMAA2SimulationThread;
import fi.smaa.jsmaa.SMAAResults;
import fi.smaa.jsmaa.SMAAResultsListener;
import fi.smaa.jsmaa.SMAASimulator;
import fi.smaa.jsmaa.SMAATRIResults;
import fi.smaa.jsmaa.SMAATRISimulationThread;
import fi.smaa.jsmaa.SimulationThread;
import fi.smaa.jsmaa.model.AbstractCriterion;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.ScaleCriterion;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.ModelChangeEvent;
import fi.smaa.jsmaa.model.SMAAModelListener;
import fi.smaa.jsmaa.model.SMAATRIModel;

@SuppressWarnings("serial")
public class JSMAAMainFrame extends JFrame {
	
	public static final String VERSION = "0.3";
	private static final Object JSMAA_MODELFILE_EXTENSION = "jsmaa";
	private static final String PROPERTY_MODELUNSAVED = "modelUnsaved";
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
	private SMAAModelListener modelListener = new MySMAAModelListener();
	private Queue<BuildSimulatorRun> buildQueue
		= new LinkedList<BuildSimulatorRun>();
	private Thread buildSimulatorThread;
	
	public JSMAAMainFrame(SMAAModel model) {
		super("SMAA");
		this.model = model;
		startGui();
	}
	
	public Boolean getModelUnsaved() {
		return modelUnsaved;
	}

	private void startGui() {		
		initFrame();
		initComponents();
		initWithModel(model);
		setModelUnsaved(false);
		updateFrameTitle();
		pack();	
	}
	
	public void initWithModel(SMAAModel model) {
		this.model = model;
		initLeftPanel();		
		model.addModelListener(modelListener);
		model.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				setModelUnsaved(true);
			}
		});
		if (model instanceof SMAATRIModel) {
			setJMenuBar(createSMAATRIMenuBar());
		} else {
			setJMenuBar(createSMAA2MenuBar());
		}
		pack();
		buildNewSimulator();
		setRightViewToCriteria();
		leftTreeFocusCriteria();
		expandLeftMenu();
	}	
	
	public void setRightViewToCriteria() {
		rightViewBuilder = new CriteriaListView(model);
		rebuildRightPanel();
	}
	
	public void setRightViewToAlternatives() {
		rightViewBuilder = new AlternativeInfoView(model);
		rebuildRightPanel();
	}
	
	public void setRightViewToCriterion(Criterion node) {
		if (model instanceof SMAATRIModel) {
			rightViewBuilder = new CriterionViewWithProfiles(node, (SMAATRIModel) model);			
		} else {
			rightViewBuilder = new CriterionView(node, model);
		}
		rebuildRightPanel();
	}	
	
	public void setRightViewToPreferences() {
		rightViewBuilder = new PreferenceInformationView(
				new SMAAModelPreferencePresentationModel(model));
		rebuildRightPanel();
	}	

	private void updateFrameTitle() {
		String appString = getFrameTitleBase();
		String file = "Unsaved model";
		
		if (currentModelFile != null) {
			try {
				file = currentModelFile.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String modelSavedStar = modelUnsaved ? "*" : "";
		setTitle(appString + " - " + file + modelSavedStar);
	}

	private String getFrameTitleBase() {
		String appString = "JSMAA v" + VERSION;
		return appString;
	}

	private void initFrame() {		
		setPreferredSize(new Dimension(800, 500));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);		
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
	   
	   getContentPane().setLayout(new BorderLayout());
	   getContentPane().add("Center", splitPane);
	   getContentPane().add("South", createToolBar());
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
		rightViewBuilder = new CentralWeightsView((SMAA2Results) results);
		rebuildRightPanel();
	}
	
	private void setRightViewToRankAcceptabilities() {
		rightViewBuilder = new RankAcceptabilitiesView((SMAA2Results) results);
		rebuildRightPanel();
	}
		
	private void initLeftPanel() {
		if (model instanceof SMAATRIModel) {
			leftTreeModel = new LeftTreeModelSMAATRI((SMAATRIModel) model);			
		} else {
			leftTreeModel = new LeftTreeModel(model);
		}
		leftTree = new JTree(leftTreeModel);
		leftTree.addTreeSelectionListener(new LeftTreeSelectionListener());
		leftTree.setEditable(true);
		JScrollPane leftScrollPane = new JScrollPane();
		leftScrollPane.setViewportView(leftTree);
		splitPane.setLeftComponent(leftScrollPane);
		LeftTreeCellRenderer renderer = new LeftTreeCellRenderer(leftTreeModel, imageLoader);
		leftTree.setCellEditor(new LeftTreeCellEditor(model, leftTree, renderer));
		leftTree.setCellRenderer(renderer);
		
		final JPopupMenu leftTreeEditPopupMenu = new JPopupMenu();
		final JMenuItem leftTreeRenameItem = createRenameMenuItem();
		leftTreeEditPopupMenu.add(leftTreeRenameItem);
		final JMenuItem leftTreeDeleteItem = createDeleteMenuItem();
		leftTreeEditPopupMenu.add(leftTreeDeleteItem);
		
		final JPopupMenu leftTreeAltsPopupMenu = new JPopupMenu();
		leftTreeAltsPopupMenu.add(createAddAltMenuItem());
		
		final JPopupMenu leftTreeCritPopupMenu = new JPopupMenu();
		leftTreeCritPopupMenu.add(createAddCardCritMenuItem());
		
		leftTree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				if (evt.isPopupTrigger()) {
					int selRow = leftTree.getRowForLocation(evt.getX(), evt.getY());
					if (selRow != -1) {
						Object obj = leftTree.getPathForLocation(evt.getX(), evt.getY()).getLastPathComponent();
						leftTree.setSelectionRow(selRow);						
						if (obj instanceof Alternative ||
								obj instanceof AbstractCriterion ||
								obj instanceof SMAAModel) {
							leftTreeDeleteItem.setEnabled(!(obj instanceof SMAAModel));
							leftTreeEditPopupMenu.show((Component) evt.getSource(), 
									evt.getX(), evt.getY());
						} else if (obj == leftTreeModel.getAlternativesNode()) {
							leftTreeAltsPopupMenu.show((Component) evt.getSource(),
									evt.getX(), evt.getY());
						} else if (obj == leftTreeModel.getCriteriaNode()) {
							leftTreeCritPopupMenu.show((Component) evt.getSource(),
									evt.getX(), evt.getY());
						}
					}
				}
			}
		});
	}
		
	private void expandLeftMenu() {
		leftTree.expandPath(new TreePath(new Object[]{leftTreeModel.getRoot(), leftTreeModel.getAlternativesNode()}));
		leftTree.expandPath(new TreePath(new Object[]{leftTreeModel.getRoot(), leftTreeModel.getCriteriaNode()}));
		leftTree.expandPath(new TreePath(new Object[]{leftTreeModel.getRoot(), leftTreeModel.getResultsNode()}));
		if (leftTreeModel instanceof LeftTreeModelSMAATRI) {
			LeftTreeModelSMAATRI sltModel = (LeftTreeModelSMAATRI) leftTreeModel;
			leftTree.expandPath(new TreePath(new Object[]{sltModel.getRoot(), sltModel.getCategoriesNode()}));			
		}
	}
	
	public void setMinimalFrame() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
		
		menuBar.add(createFileMenu(true));
		menuBar.add(createResultsMenu());
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(createHelpMenu());
		setJMenuBar(menuBar);
		setTitle(getFrameTitleBase());
	}
	
	private JMenuBar createSMAATRIMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
		
		menuBar.add(createFileMenu(false));
		menuBar.add(createEditMenu());
		menuBar.add(createCriteriaMenu());
		menuBar.add(createAlternativeMenu());
		menuBar.add(createCategoriesMenu());
		menuBar.add(createResultsSMAATRIMenu());
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(createHelpMenu());

		return menuBar;
	}	

	private JMenuBar createSMAA2MenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
		
		menuBar.add(createFileMenu(false));
		menuBar.add(createEditMenu());
		menuBar.add(createCriteriaMenu());
		menuBar.add(createAlternativeMenu());
		menuBar.add(createResultsMenu());
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(createHelpMenu());
		return menuBar;
	}

	private JMenu createCategoriesMenu() {
		JMenu categoryMenu = new JMenu("Categories");
		categoryMenu.setMnemonic('t');
		JMenuItem showItem = new JMenuItem("Show");
		showItem.setMnemonic('s');
		JMenuItem addCatButton = createAddCatMenuItem();
		
		showItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				//
			}			
		});
				
		categoryMenu.add(showItem);
		categoryMenu.addSeparator();
		categoryMenu.add(addCatButton);
		return categoryMenu;
	}

	private JMenu createHelpMenu() {
		JMenu menu = new JMenu("Help");
		menu.setMnemonic('h');
		JMenuItem aboutItem = new JMenuItem("About", getIcon(FileNames.ICON_HOME));
		aboutItem.setMnemonic('a');
		aboutItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				showAboutDialog();
			}			
		});
		menu.add(aboutItem);
		return menu;
	}

	private void showAboutDialog() {
		String title = "About JSMAA";
		String msg = getFrameTitleBase();
		msg += "\nJSMAA is open source and licensed under GPLv3.\n";
		msg += "\t- and can be distributed freely!\n";
		msg += "(c) 2009 Tommi Tervonen <t dot p dot tervonen at rug dot nl>";
		JOptionPane.showMessageDialog(this, msg, title,
				JOptionPane.INFORMATION_MESSAGE, getIcon(FileNames.ICON_HOME));
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

	private JMenu createResultsSMAATRIMenu() {
		JMenu resultsMenu = new JMenu("Results");
		resultsMenu.setMnemonic('r');
		JMenuItem racsItem = new JMenuItem("Category acceptability indices", 
				getIcon(FileNames.ICON_RANKACCEPTABILITIES));
		racsItem.setMnemonic('r');
				
		racsItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				setRightViewToCategoryAcceptabilities();
			}			
		});
		
		resultsMenu.add(racsItem);
		return resultsMenu;
	}
	

	private JMenu createEditMenu() {
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic('e');
		
		editRenameItem = createRenameMenuItem();		
		editRenameItem.setEnabled(false);
		
		editDeleteItem = createDeleteMenuItem();
		editDeleteItem.setEnabled(false);		
		editMenu.add(editRenameItem);
		editMenu.add(editDeleteItem);
		return editMenu;
	}

	private JMenuItem createDeleteMenuItem() {
		JMenuItem item = new JMenuItem("Delete", getIcon(FileNames.ICON_DELETE));
		item.setMnemonic('d');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		item.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				menuDeleteClicked();
			}
		});
		return item;
	}

	private JMenuItem createRenameMenuItem() {
		JMenuItem item = new JMenuItem("Rename", getIcon(FileNames.ICON_RENAME));
		item.setMnemonic('r');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		item.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				menuRenameClicked();
			}			
		});		
		return item;
	}


	public Icon getIcon(String name) {
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
		} else if (selection instanceof AbstractCriterion) {
			confirmDeleteCriterion((Criterion)selection);
		}
	}


	private void confirmDeleteCriterion(Criterion criterion) {
		int conf = JOptionPane.showConfirmDialog(this, 
				"Do you really want to delete criterion " + criterion + "?",
				"Confirm deletion",					
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				getIcon(FileNames.ICON_DELETE));
		if (conf == JOptionPane.YES_OPTION) {
			model.deleteCriterion(criterion);
		}
	}


	private void confirmDeleteAlternative(Alternative alternative) {
		int conf = JOptionPane.showConfirmDialog(this, 
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

	private void menuRenameClicked() {
		leftTree.startEditingAtPath(leftTree.getSelectionPath());
	}

	private JMenu createFileMenu(boolean minimal) {
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('f');

		JMenu newItem = createFileNewMenu();
		
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.setMnemonic('s');
		saveItem.setIcon(getIcon(FileNames.ICON_SAVEFILE));
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		Bindings.bind(saveItem, "enabled", new PresentationModel<JSMAAMainFrame>(this).getModel(PROPERTY_MODELUNSAVED));
		JMenuItem saveAsItem = new JMenuItem("Save As");
		saveAsItem.setMnemonic('a');
		saveAsItem.setIcon(getIcon(FileNames.ICON_SAVEAS));
		
		JMenuItem openItem = new JMenuItem("Open");
		openItem.setMnemonic('o');
		openItem.setIcon(getIcon(FileNames.ICON_OPENFILE));
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));		
		JMenuItem quitItem = createQuitItem();
		
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
				openFile();
			}
		});
		
		if (!minimal) {
			fileMenu.add(newItem);
			fileMenu.add(openItem);			
		}
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);
		fileMenu.addSeparator();
		fileMenu.add(quitItem);		
		return fileMenu;
	}

	private JMenu createFileNewMenu() {
		JMenu newMenu = new JMenu("New model");
		newMenu.setMnemonic('n');
		newMenu.setIcon(getIcon(FileNames.ICON_FILENEW));
		
		JMenuItem newSMAA2Item = new JMenuItem("SMAA-2");
		newSMAA2Item.setMnemonic('2');
		newSMAA2Item.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				newModel(DefaultModels.getSMAA2Model());
			}
		});
		JMenuItem newSMAATRIItem = new JMenuItem("SMAA-TRI");
		newSMAATRIItem.setMnemonic('t');
		newSMAATRIItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				newModel(DefaultModels.getSMAATRIModel());
			}
		});
		
		newMenu.add(newSMAA2Item);
		newMenu.add(newSMAATRIItem);
		return newMenu;
	}
	
	private JMenuItem createQuitItem() {
		JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.setMnemonic('q');
		quitItem.setIcon(getIcon(FileNames.ICON_STOP));
		quitItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				quitItemAction();
			}
		});
		return quitItem;
	}
	
	protected void quitItemAction() {
		for (WindowListener w : getWindowListeners()) {
			w.windowClosing(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}

	private void newModel(SMAAModel newModel) {
		if (!checkSaveCurrentModel()) {
			return;
		}
		this.model = newModel;
		initWithModel(newModel);
		setCurrentModelFile(null);
		setModelUnsaved(false);
		updateFrameTitle();		
		expandLeftMenu();
	}

	private boolean checkSaveCurrentModel() {
		if (modelUnsaved) {
			int conf = JOptionPane.showConfirmDialog(this, 
					"Current model not saved. Do you want do save changes?",
					"Save changed",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
					getIcon(FileNames.ICON_STOP));
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

	private boolean saveAs() {
		JFileChooser chooser = getFileChooser();
		int retVal = chooser.showSaveDialog(this);
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
			JOptionPane.showMessageDialog(this, "Error saving model to " + getCanonicalPath(file) + 
					", " + e.getMessage(), "Save error", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}

	public boolean save() {
		if (currentModelFile == null) {
			return saveAs();
		} else {
			return trySaveModel(currentModelFile);
		}
	}
	
	private void openFile() {
		if (!checkSaveCurrentModel()) {
			return;
		}
		JFileChooser chooser = getFileChooser();
		int retVal = chooser.showOpenDialog(this);
		if (retVal == JFileChooser.APPROVE_OPTION) {
			try {
				loadModel(chooser.getSelectedFile());
				expandLeftMenu();	
				leftTreeFocusCriteria();
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(this,
						"Error loading model: "+ e.getMessage(), 
						"Load error", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {				
				showErrorIncompatibleModel(chooser);
			} catch (ClassNotFoundException e) {
				showErrorIncompatibleModel(chooser);				
			}
		}
	}

	private void showErrorIncompatibleModel(JFileChooser chooser) {
		JOptionPane.showMessageDialog(this, "Error loading model from " +
				getCanonicalPath(chooser.getSelectedFile()) + 
				", file doesn't contain a compatible JSMAA model.", "Load error", JOptionPane.ERROR_MESSAGE);
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
		s.close();
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

	private JMenu createAlternativeMenu() {
		JMenu alternativeMenu = new JMenu("Alternatives");
		alternativeMenu.setMnemonic('a');
		JMenuItem showItem = new JMenuItem("Show");
		showItem.setMnemonic('s');
		JMenuItem addAltButton = createAddAltMenuItem();
		
		showItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				setRightViewToAlternatives();
			}			
		});
				
		alternativeMenu.add(showItem);
		alternativeMenu.addSeparator();
		alternativeMenu.add(addAltButton);
		return alternativeMenu;
	}

	private JMenuItem createAddAltMenuItem() {
		JMenuItem item = new JMenuItem("Add new");
		item.setMnemonic('n');
		item.setIcon(getIcon(FileNames.ICON_ADD));
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));		
		item.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addAlternative();
			}
		});
		return item;
	}

	private JMenuItem createAddCatMenuItem() {
		JMenuItem item = new JMenuItem("Add new");
		item.setMnemonic('n');
		item.setIcon(getIcon(FileNames.ICON_ADD));
		item.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addCategory();
			}
		});
		return item;
	}	

	private JMenu createCriteriaMenu() {
		JMenu criteriaMenu = new JMenu("Criteria");
		criteriaMenu.setMnemonic('c');
		JMenuItem showItem = new JMenuItem("Show");
		showItem.setMnemonic('s');
		showItem.setIcon(getIcon(FileNames.ICON_CRITERIALIST));
		showItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				setRightViewToCriteria();
			}
		});		
		
		JMenuItem addCardItem = createAddCardCritMenuItem();
		
		JMenuItem addOrdinalItem = new JMenuItem("Add ordinal");
		addOrdinalItem.setMnemonic('o');
		addOrdinalItem.setIcon(getIcon(FileNames.ICON_ADD));		
		addOrdinalItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addOrdinalCriterion();
			}			
		});
		
		criteriaMenu.add(showItem);
		criteriaMenu.addSeparator();
		criteriaMenu.add(addCardItem);
		//toolBarAddCritMenu.add(addOrdinalButton);			
		return criteriaMenu;
	}

	private JMenuItem createAddCardCritMenuItem() {
		JMenuItem item = new JMenuItem("Add new");
		item.setMnemonic('c');
		item.setIcon(getIcon(FileNames.ICON_CARDINALCRITERION));
		item.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addCardinalCriterion();
			}			
		});
		return item;
	}

	private void addCriterionAndStartRename(Criterion c) {
		model.addCriterion(c);
		leftTree.setSelectionPath(leftTreeModel.getPathForCriterion(c));
		leftTree.startEditingAtPath(leftTreeModel.getPathForCriterion(c));
	}
	
	private void addAlternativeAndStartRename(Alternative a) {
		model.addAlternative(a);
		leftTree.setSelectionPath(leftTreeModel.getPathForAlternative(a));
		leftTree.startEditingAtPath(leftTreeModel.getPathForAlternative(a));			
	}
	
	private void addCategoryAndStartRename(Alternative a) {
		((SMAATRIModel) model).addCategory(a);
		leftTree.setSelectionPath(((LeftTreeModelSMAATRI) leftTreeModel).getPathForCategory(a));
		leftTree.startEditingAtPath(((LeftTreeModelSMAATRI) leftTreeModel).getPathForCategory(a));			
	}	

	protected void addCardinalCriterion() {
		ScaleCriterion c = new ScaleCriterion(generateNextCriterionName());
		addCriterionAndStartRename(c);
	}
	
	protected void addOrdinalCriterion() {
		model.addCriterion(new OrdinalCriterion(generateNextCriterionName()));
	}

	private String generateNextCriterionName() {
		Collection<Criterion> crit = model.getCriteria();
		
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
		Collection<Alternative> alts = model.getAlternatives();
		
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
				addAlternativeAndStartRename(a);
				return;
			}
			index++;
		}
	}
	
	protected void addCategory() {
		Collection<Alternative> cats = ((SMAATRIModel) model).getCategories();
		
		int index = 1;
		while (true) {
			Alternative newCat = new Alternative("Category " + index);
			boolean found = false; 
			for (Alternative cat : cats) {
				if (cat.getName().equals(newCat.getName())) {
					found = true;
					break;
				}
			}
			if (!found) {
				addCategoryAndStartRename(newCat);
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
			} else if (node instanceof AbstractCriterion) {
				setRightViewToCriterion((AbstractCriterion)node);
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
			} else if (leftTreeModel instanceof LeftTreeModelSMAATRI &&
				((LeftTreeModelSMAATRI) leftTreeModel).getCatAccNode() == node) {
				setRightViewToCategoryAcceptabilities();
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

	private class MySMAAModelListener implements SMAAModelListener {
		
		public void modelChanged(ModelChangeEvent type) {
			setModelUnsaved(true);
			buildNewSimulator();
			if (type == ModelChangeEvent.ALTERNATIVES) {
				setRightViewToAlternatives();
			} else if (type == ModelChangeEvent.CRITERIA) {
				setRightViewToCriteria();
			} else if (type != ModelChangeEvent.MEASUREMENT) {
				rebuildRightPanel();
			}
			
			expandLeftMenu();				
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
			SMAAModel newModel = model.deepCopy();
			
			connectAlternativeNameAdapters(model, newModel);
			connectCriteriaNameAdapters(model, newModel);
			
			SimulationThread thread = null;
			if (newModel instanceof SMAATRIModel) {
				thread = new SMAATRISimulationThread((SMAATRIModel) newModel, 10000);				
			} else {
				thread = new SMAA2SimulationThread(newModel, 10000);
			}
			simulator = new SMAASimulator(newModel, thread);
			results = thread.getResults();
			results.addResultsListener(new SimulationProgressListener());
			if (newModel instanceof SMAATRIModel) {
				if (rightViewBuilder instanceof CentralWeightsView) {
					setRightViewToCategoryAcceptabilities();
				} else if (rightViewBuilder instanceof RankAcceptabilitiesView) {
					setRightViewToCategoryAcceptabilities();					
				}
			} else {
				if (rightViewBuilder instanceof CentralWeightsView) {
					setRightViewToCentralWeights();
				} else if (rightViewBuilder instanceof RankAcceptabilitiesView) {
					setRightViewToRankAcceptabilities();
				}
			}
			simulationProgress.setValue(0);
			simulator.restart();
			checkStartNewSimulator();
		}
	}

	private void connectAlternativeNameAdapters(SMAAModel model,
			SMAAModel newModel) {
		assert(model.getAlternatives().size() == newModel.getAlternatives().size());
		for (int i=0;i<model.getAlternatives().size();i++) {
			Alternative mAlt = model.getAlternatives().get(i);
			Alternative nmAlt = newModel.getAlternatives().get(i);
			mAlt.addPropertyChangeListener(new AlternativeNameUpdater(nmAlt));
		}
	}	
	
	public void setRightViewToCategoryAcceptabilities() {
		rightViewBuilder = new CategoryAcceptabilitiesView((SMAATRIResults)results);
		rebuildRightPanel();
	}

	private void connectCriteriaNameAdapters(SMAAModel model,
			SMAAModel newModel) {
		assert(model.getCriteria().size() == newModel.getCriteria().size());
		for (int i=0;i<model.getCriteria().size();i++) {
			Criterion mCrit = model.getCriteria().get(i);
			Criterion nmCrit = newModel.getCriteria().get(i);
			mCrit.addPropertyChangeListener(new CriterionNameUpdater(nmCrit));
		}
	}		
	
	private class CriterionNameUpdater implements PropertyChangeListener {

		private Criterion toUpdate;
		public CriterionNameUpdater(Criterion toUpdate) {
			this.toUpdate = toUpdate;
		}
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(Criterion.PROPERTY_NAME)){ 
				setModelUnsaved(true);
				toUpdate.setName((String) evt.getNewValue());
			}
			
		}
	
	}
	
	private class AlternativeNameUpdater implements PropertyChangeListener {
		
		private Alternative toUpdate;
		public AlternativeNameUpdater(Alternative toUpdate) {
			this.toUpdate = toUpdate;
		}
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(Alternative.PROPERTY_NAME)) {
				setModelUnsaved(true);				
				toUpdate.setName((String) evt.getNewValue());
			}
		}
	}
		
	private class SimulationProgressListener implements SMAAResultsListener {
		public void resultsChanged() {
			int amount = simulator.getCurrentIteration() * 100 / simulator.getTotalIterations();
			simulationProgress.setValue(amount);
			if (amount < 100) {
				simulationProgress.setString("Simulating: " + Integer.toString(amount) + "% done");
			} else {
				simulationProgress.setString("Simulation complete.");
			}
		}
	}
}
