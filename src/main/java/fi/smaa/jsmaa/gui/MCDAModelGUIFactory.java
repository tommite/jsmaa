package fi.smaa.jsmaa.gui;

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.jgoodies.binding.adapter.Bindings;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.components.Focuser;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModelMCDAModel;
import fi.smaa.jsmaa.gui.presentation.PreferencePresentationModel;
import fi.smaa.jsmaa.gui.views.CriterionView;
import fi.smaa.jsmaa.gui.views.PreferenceInformationView;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.SMAA2Model;


@SuppressWarnings("serial")
public abstract class MCDAModelGUIFactory<T extends LeftTreeModelMCDAModel<M>, M extends SMAA2Model> extends AbstractGUIFactory<T, M>{
	
	private JMenuItem editRenameItem;
	private JMenuItem editDeleteItem;		
	
	protected MCDAModelGUIFactory(Window parent, M smaaModel, MenuDirector director) {
		super(parent, smaaModel, director);
	}
		
	protected abstract JButton buildToolBarAddCriterionButton();
	protected abstract JMenuItem buildAddCriterionItem();		
	
	public ViewBuilder buildView(Object o) {
		if (o instanceof Criterion) {
			return new CriterionView(((Criterion)o), smaaModel);
		} else if (o == treeModel.getPreferencesNode()) {
			return new PreferenceInformationView(new PreferencePresentationModel(smaaModel));
		} else {
			return super.buildView(o);
		}	
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
			
	@Override
	protected List<JMenuItem> getEntityMenuList() {
		List<JMenuItem> list = super.getEntityMenuList();
		list.add(buildEditMenu());				
		list.add(buildCriteriaMenu());
		list.add(buildAlternativeMenu());
		return list;
	}	
	
	@Override
	protected JTree buildTree() {
		final JTree tree = super.buildTree();
		
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
								obj instanceof SMAA2Model) {
							leftTreeDeleteItem.setEnabled(!(obj instanceof SMAA2Model));
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
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
			public void valueChanged(TreeSelectionEvent ev) {
				Object node = ev.getPath().getLastPathComponent();
				if (node instanceof Alternative || node instanceof Criterion) {
					editDeleteItem.setEnabled(true);
					editRenameItem.setEnabled(true);
				} else if (node == treeModel.getModelNode()) {
					editDeleteItem.setEnabled(false);
					editRenameItem.setEnabled(true);					
				} else {
					editDeleteItem.setEnabled(false);
					editRenameItem.setEnabled(false);					
				}
			}	
		});
		return tree;
	}	
	
	@Override
	protected JToolBar buildTopToolBar() {
		JToolBar bar = super.buildTopToolBar();
		JButton topBarSaveButton = new JButton(ImageLoader.getIcon(FileNames.ICON_SAVEFILE));
		topBarSaveButton.setToolTipText("Save model");
		topBarSaveButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				director.save();
			}
		});
		bar.add(topBarSaveButton);
		Bindings.bind(topBarSaveButton, "enabled", fileManagerPM.getUnsavedModel());
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
	
	private void addAlternativeAndStartRename(Alternative a) {
		smaaModel.addAlternative(a);
		Focuser.focus(tree, treeModel, a);		
		tree.startEditingAtPath(treeModel.getPathForAlternative(a));			
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
		Focuser.focus(tree, treeModel, c);
		tree.startEditingAtPath(treeModel.getPathForCriterion(c));
	}
		
	private void menuDeleteClicked() {
		Object selection = tree.getSelectionPath().getLastPathComponent();
		if (selection instanceof Alternative) {
			confirmDeleteAlternative((Alternative) selection);
		} else if (selection instanceof Criterion) {
			confirmDeleteCriterion((Criterion)selection);
		}
	}
	
	protected JMenuItem buildDeleteItem() {
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
	
	protected JMenuItem buildRenameItem() {
		JMenuItem item = new JMenuItem("Rename", ImageLoader.getIcon(FileNames.ICON_RENAME));
		item.setMnemonic('r');
		item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
		item.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				tree.startEditingAtPath(tree.getSelectionPath());
			}			
		});	
		return item;
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
}
