package fi.smaa.jsmaa.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.DropMode;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.jsmaa.AppInfo;
import fi.smaa.jsmaa.DefaultModels;
import fi.smaa.jsmaa.gui.presentation.AbstractLeftTreeModel;
import fi.smaa.jsmaa.gui.presentation.ModelFileManagerPM;
import fi.smaa.jsmaa.model.SMAAModel;

public abstract class AbstractGUIFactory <T extends AbstractLeftTreeModel<M>, M extends SMAAModel> implements GUIFactory{

	protected T treeModel;
	protected M smaaModel;

	protected MenuDirector director;
	protected ModelFileManagerPM fileManagerPM;
	protected Component parent = null;
	protected JToolBar topToolBar;
	protected JMenuBar menuBar;
	protected JTree tree;
	
	private JToolBar bottomToolBar;
	private JProgressBar progressBar;

	public AbstractGUIFactory(M smaaModel, MenuDirector director) {
		fileManagerPM = new ModelFileManagerPM(director.getFileManager());		
		this.smaaModel = smaaModel;
		this.treeModel = buildTreeModel();		
		this.director = director;
		menuBar = buildMenuBar();
		tree = buildTree();
		topToolBar = buildTopToolBar();
		bottomToolBar = buildBottomToolBar();
	}
	
	public T getTreeModel() {
		return treeModel;
	}
	
	public JToolBar getTopToolBar() {
		return topToolBar;
	}	
	
	public JToolBar getBottomToolBar() {
		return bottomToolBar;
	}
	
	public JProgressBar getProgressBar() {
		return progressBar;
	}
	
	public JMenuBar getMenuBar() {
		return menuBar;
	}
	
	public JTree getTree() {
		return tree;
	}	
	
	protected abstract T buildTreeModel();
	protected abstract JMenu buildResultsMenu();
	protected abstract LeftTreeCellRenderer<?> buildCellRenderer();
	
	protected JTree buildTree() {
		JTree tree = new JTree(treeModel);
		tree.setEditable(true);
		LeftTreeCellRenderer<?> renderer = buildCellRenderer();
		tree.setCellEditor(new LeftTreeCellEditor(smaaModel, tree, renderer));
		tree.setCellRenderer(renderer);
		tree.setDragEnabled(true);
		tree.setTransferHandler(new LeftTreeTransferHandler(treeModel, smaaModel));
		tree.setDropMode(DropMode.INSERT);
		return tree;
	}
	
	protected List<JMenuItem> getEntityMenuList() {
		List<JMenuItem> list = new ArrayList<JMenuItem>();
		list.add(buildFileMenu());
		return list;
	}
	
	@SuppressWarnings("serial")
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
		Bindings.bind(saveItem, "enabled", fileManagerPM.getUnsavedModel());
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
	
	protected JToolBar buildTopToolBar() {
		JToolBar bar = new JToolBar();
		bar.setFloatable(false);
		return bar;
	}				

	protected JToolBar buildBottomToolBar() {
		JToolBar bar = new JToolBar();
		progressBar = new JProgressBar();	
		progressBar.setStringPainted(true);		
		bar.add(progressBar);
		bar.setFloatable(false);
		return bar;
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
	
	@SuppressWarnings("serial")
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
}
