package fi.smaa.jsmaa.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.AppInfo;
import fi.smaa.jsmaa.DefaultModels;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModel;
import fi.smaa.jsmaa.gui.presentation.PreferencePresentationModel;
import fi.smaa.jsmaa.gui.views.AlternativeInfoView;
import fi.smaa.jsmaa.gui.views.AlternativeView;
import fi.smaa.jsmaa.gui.views.CriteriaListView;
import fi.smaa.jsmaa.gui.views.CriterionView;
import fi.smaa.jsmaa.gui.views.PreferenceInformationView;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.SMAAModel;


@SuppressWarnings("serial")
public abstract class AbstractGUIFactory<T extends LeftTreeModel, M extends SMAAModel> implements GUIFactory{

	protected T treeModel;
	protected M smaaModel;
	
	private JMenuItem editRenameItem;
	private JMenuItem editDeleteItem;
	
	protected JToolBar topToolBar;
	protected JMenuBar menuBar;
	protected JTree tree;
	protected Component parent = null;
	protected MenuDirector director;
	
	protected AbstractGUIFactory(M smaaModel, MenuDirector director) {
		this.smaaModel = smaaModel;
		this.treeModel = buildTreeModel();		
		this.director = director;
		menuBar = buildMenuBar();
		tree = buildTree();
		topToolBar = buildTopToolBar();
	}
	
	public T getTreeModel() {
		return treeModel;
	}
	
	public JToolBar getTopToolBar() {
		return topToolBar;
	}

	protected JToolBar buildTopToolBar() {
		JToolBar bar = new JToolBar();
		bar.setFloatable(false);

		JButton topBarSaveButton = new JButton(ImageLoader.getIcon(FileNames.ICON_SAVEFILE));
		topBarSaveButton.setToolTipText("Add alternative");
		topBarSaveButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				director.save();
			}
		});
		bar.add(topBarSaveButton);
		//Bindings.bind(topBarSaveButton, "enabled", new PresentationModel<JSMAAMainFrame>(this).getModel(PROPERTY_MODELUNSAVED));		
		bar.addSeparator();

		JButton addButton = new JButton(ImageLoader.getIcon(FileNames.ICON_ADDALTERNATIVE));
		addButton.setToolTipText("Add alternative");
		addButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addAlternative();
			}
		});
		bar.add(addButton);
		JButton addCritButton = buildToolBarAddCriterionButton();
		bar.add(addCritButton);
		return bar;
	}

	protected abstract JButton buildToolBarAddCriterionButton();
	protected abstract T buildTreeModel();
	protected abstract JMenu buildResultsMenu();	
	protected abstract JMenuItem buildAddCriterionItem();		
	
	public ViewBuilder buildView(Object o) {
		if (o == treeModel.getAlternativesNode()) {
			return new AlternativeInfoView(smaaModel.getAlternatives(), "Alternatives");			
		} else if (o == treeModel.getCriteriaNode()){
			return new CriteriaListView(smaaModel);
		} else if (o instanceof Criterion) {
			return new CriterionView(((Criterion)o), smaaModel);
		} else if (o instanceof Alternative) {
			return new AlternativeView((Alternative) o);
		} else if (o == treeModel.getPreferencesNode()) {
			return new PreferenceInformationView(new PreferencePresentationModel(smaaModel));
		} else if (o == treeModel.getResultsNode()) {
			return new ViewBuilder() {
				@Override
				public JComponent buildPanel() {
					return new JPanel();
				}
			};
		} else if (o == treeModel.getModelNode()) {
			return new ViewBuilder() {
				@Override
				public JComponent buildPanel() {
					return new JPanel();
				}
			};			
		} else {
			throw new IllegalArgumentException("no view known for object "+ o);
		}	
	}

	@Override
	public JMenuBar getMenuBar() {
		return menuBar;
	}
	
	@Override
	public JTree getTree() {
		return tree;
	}
	
	private JMenuBar buildMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);		
	
		for (JMenuItem l : getEntityMenuList()) {
			menuBar.add(l);
		}
		
		menuBar.add(buildResultsMenu());
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(buildHelpMenu());
		return menuBar;
	}
	
	
	protected List<JMenuItem> getEntityMenuList() {
		List<JMenuItem> list = new ArrayList<JMenuItem>();
		list.add(buildFileMenu());
		list.add(buildEditMenu());
		list.add(buildCriteriaMenu());
		list.add(buildAlternativeMenu());
		return list;
	}
	
	private JTree buildTree() {
		final JTree tree = new JTree(treeModel);
		tree.setEditable(true);
		LeftTreeCellRenderer renderer = new LeftTreeCellRenderer(treeModel);
		tree.setCellEditor(new LeftTreeCellEditor(smaaModel, tree, renderer));
		tree.setCellRenderer(renderer);
		tree.setDragEnabled(true);
		tree.setTransferHandler(new LeftTreeTransferHandler(treeModel, smaaModel));
		tree.setDropMode(DropMode.INSERT);
		
		final JPopupMenu leftTreeEditPopupMenu = new JPopupMenu();
		final JMenuItem leftTreeRenameItem = buildRenameItem();
		leftTreeEditPopupMenu.add(leftTreeRenameItem);
		final JMenuItem leftTreeDeleteItem = buildDeleteItem();
		leftTreeEditPopupMenu.add(leftTreeDeleteItem);
		
		final JPopupMenu leftTreeAltsPopupMenu = new JPopupMenu();
		leftTreeAltsPopupMenu.add(buildAddAlternativeItem());
		
		final JPopupMenu leftTreeCritPopupMenu = new JPopupMenu();
		leftTreeCritPopupMenu.add(buildAddCriterionItem());
		
		tree.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				if (evt.isPopupTrigger()) {
					int selRow = tree.getRowForLocation(evt.getX(), evt.getY());
					if (selRow != -1) {
						Object obj = tree.getPathForLocation(evt.getX(), evt.getY()).getLastPathComponent();
						tree.setSelectionRow(selRow);						
						if (obj instanceof Alternative ||
								obj instanceof Criterion ||
								obj instanceof SMAAModel) {
							leftTreeDeleteItem.setEnabled(!(obj instanceof SMAAModel));
							leftTreeEditPopupMenu.show((Component) evt.getSource(), 
									evt.getX(), evt.getY());
						} else if (obj == treeModel.getAlternativesNode()) {
							leftTreeAltsPopupMenu.show((Component) evt.getSource(),
									evt.getX(), evt.getY());
						} else if (obj == treeModel.getCriteriaNode()) {
							leftTreeCritPopupMenu.show((Component) evt.getSource(),
									evt.getX(), evt.getY());
						}
					}
				}
			}
		});
		return tree;
	}	
	
	private JMenu buildHelpMenu() {
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setMnemonic('h');
		JMenuItem aboutItem = new JMenuItem("About", ImageLoader.getIcon(FileNames.ICON_HOME));
		aboutItem.setMnemonic('a');
		aboutItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				showAboutDialog();
			}			
		});
		helpMenu.add(aboutItem);
		return helpMenu;
	}

	private void showAboutDialog() {
		String title = "About "+ AppInfo.getAppName();
		String msg = "JSMAA v" + AppInfo.getAppVersion();
		msg += "\n"+AppInfo.getAppName()+" is open source and licensed under GPLv3.\n";
		msg += "\t- and can be distributed freely!\n";
		msg += "(c) 2009 Tommi Tervonen <t dot p dot tervonen at rug dot nl>";
		JOptionPane.showMessageDialog(parent, msg, title,
				JOptionPane.INFORMATION_MESSAGE, ImageLoader.getIcon(FileNames.ICON_HOME));
	}

	private JMenu buildAlternativeMenu() {
		JMenu alternativeMenu = new JMenu("Alternatives");
		alternativeMenu.setMnemonic('a');
		JMenuItem showItem = new JMenuItem("Show");
		showItem.setMnemonic('s');
		JMenuItem addAlternativeItem = buildAddAlternativeItem();
		showItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				Focuser.focus(tree, treeModel, treeModel.getAlternativesNode());
			}			
		});			
		alternativeMenu.add(showItem);
		alternativeMenu.addSeparator();
		alternativeMenu.add(addAlternativeItem);
		return alternativeMenu;
	}
	
	private JMenuItem buildAddAlternativeItem() {
		JMenuItem item = new JMenuItem("Add new");
		item.setMnemonic('n');
		item.setIcon(ImageLoader.getIcon(FileNames.ICON_ADDALTERNATIVE));
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));		
		item.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addAlternative();
			}
		});		
		return item;
	}

	private JMenu buildCriteriaMenu() {
		JMenu criteriaMenu = new JMenu("Criteria");
		criteriaMenu.setMnemonic('c');
		JMenuItem showItem = new JMenuItem("Show");
		showItem.setMnemonic('s');
		showItem.setIcon(ImageLoader.getIcon(FileNames.ICON_CRITERIALIST));
		showItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				Focuser.focus(tree, treeModel, treeModel.getCriteriaNode());
			}
		});		
		JMenuItem addCardItem = buildAddCriterionItem();
		criteriaMenu.add(showItem);
		criteriaMenu.addSeparator();
		criteriaMenu.add(addCardItem);
		return criteriaMenu;
	}

	private JMenu buildEditMenu() {
		JMenu editMenu = new JMenu("Edit");
		editMenu.setMnemonic('e');
		
		editRenameItem = buildRenameItem();
		editRenameItem.setEnabled(false);
		
		editDeleteItem = buildDeleteItem();
		editDeleteItem.setEnabled(false);
		editMenu.add(editRenameItem);
		editMenu.add(editDeleteItem);
		return editMenu;
	}
	
	private JMenuItem buildDeleteItem() {
		JMenuItem item = new JMenuItem("Delete", ImageLoader.getIcon(FileNames.ICON_DELETE));
		item.setMnemonic('d');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		item.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				menuDeleteClicked();
			}
		});		
		return item;
	}
	
	private JMenuItem buildRenameItem() {
		JMenuItem item = new JMenuItem("Rename", ImageLoader.getIcon(FileNames.ICON_RENAME));
		item.setMnemonic('r');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		item.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				menuRenameClicked();
			}			
		});	
		return item;
	}
	
	private JMenu buildFileMenu() {
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('f');
		JMenu newMenu = new JMenu("New model");
		newMenu.setMnemonic('n');
		newMenu.setIcon(ImageLoader.getIcon(FileNames.ICON_FILENEW));
		
		JMenuItem newSMAA2Item = new JMenuItem("SMAA-2");
		newSMAA2Item.setMnemonic('2');
		newSMAA2Item.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				director.newModel(DefaultModels.getSMAA2Model());
			}
		});
		JMenuItem newSMAATRIItem = new JMenuItem("SMAA-TRI");
		newSMAATRIItem.setMnemonic('t');
		newSMAATRIItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				director.newModel(DefaultModels.getSMAATRIModel());
			}
		});
		
		newMenu.add(newSMAA2Item);
		newMenu.add(newSMAATRIItem);
		JMenu newItem = newMenu;
		
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.setMnemonic('s');
		saveItem.setIcon(ImageLoader.getIcon(FileNames.ICON_SAVEFILE));
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		//Bindings.bind(saveItem, "enabled", new PresentationModel<JSMAAMainFrame>(main).getModel(JSMAAMainFrame.PROPERTY_MODELUNSAVED));
		JMenuItem saveAsItem = new JMenuItem("Save As");
		saveAsItem.setMnemonic('a');
		saveAsItem.setIcon(ImageLoader.getIcon(FileNames.ICON_SAVEAS));
		
		JMenuItem openItem = new JMenuItem("Open");
		openItem.setMnemonic('o');
		openItem.setIcon(ImageLoader.getIcon(FileNames.ICON_OPENFILE));
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.setMnemonic('q');
		quitItem.setIcon(ImageLoader.getIcon(FileNames.ICON_STOP));
		quitItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				director.quit();
			}
		});		
		saveItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				director.save();
			}
		});
		saveAsItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				director.saveAs();
			}
		});
		openItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				director.open();
			}
		});
		
		fileMenu.add(newItem);
		fileMenu.add(openItem);			
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);
		fileMenu.addSeparator();
		fileMenu.add(quitItem);
		
		return fileMenu;
	}
	
	protected String generateNextCriterionName() {
		Collection<Criterion> crit = smaaModel.getCriteria();
		
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
	
	protected void addCriterionAndStartRename(Criterion c) {
		smaaModel.addCriterion(c);
		tree.setSelectionPath(treeModel.getPathForCriterion(c));
		tree.startEditingAtPath(treeModel.getPathForCriterion(c));
	}
	
	protected void addAlternativeAndStartRename(Alternative a) {
		smaaModel.addAlternative(a);
		tree.setSelectionPath(treeModel.getPathForAlternative(a));
		tree.startEditingAtPath(treeModel.getPathForAlternative(a));			
	}	
	
	private Object getLeftMenuSelection() {
		return tree.getSelectionPath().getLastPathComponent();
	}	

	public void menuDeleteClicked() {
		Object selection = getLeftMenuSelection();
		if (selection instanceof Alternative) {
			confirmDeleteAlternative((Alternative) selection);
		} else if (selection instanceof Criterion) {
			confirmDeleteCriterion((Criterion)selection);
		}
	}
	
	private void confirmDeleteCriterion(Criterion criterion) {
		int conf = JOptionPane.showConfirmDialog(parent, 
				"Do you really want to delete criterion " + criterion + "?",
				"Confirm deletion",					
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				ImageLoader.getIcon(FileNames.ICON_DELETE));
		if (conf == JOptionPane.YES_OPTION) {
			smaaModel.deleteCriterion(criterion);
		}
	}

	protected void confirmDeleteAlternative(Alternative alternative) {
		String typeName = "alternative";
		int conf = JOptionPane.showConfirmDialog(parent, 
				"Do you really want to delete " + typeName + " " + alternative + "?",
				"Confirm deletion",					
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
				ImageLoader.getIcon(FileNames.ICON_DELETE));
		if (conf == JOptionPane.YES_OPTION) {
			smaaModel.deleteAlternative(alternative);
		}
	}
	
	protected void menuRenameClicked() {
		tree.startEditingAtPath(tree.getSelectionPath());
	}

	private void addAlternative() {
		Collection<Alternative> alts = smaaModel.getAlternatives();

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
}
