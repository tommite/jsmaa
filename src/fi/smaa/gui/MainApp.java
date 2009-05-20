package fi.smaa.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import nl.rug.escher.common.gui.GUIHelper;
import nl.rug.escher.common.gui.ViewBuilder;

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;

import fi.smaa.Alternative;
import fi.smaa.Criterion;
import fi.smaa.GaussianCriterion;
import fi.smaa.OrdinalCriterion;
import fi.smaa.SMAAModel;
import fi.smaa.SMAAResults;
import fi.smaa.SMAASimulator;
import fi.smaa.UniformCriterion;

public class MainApp {
	
	private JFrame frame;
	private JSplitPane splitPane;
	private JTree leftTree;
	private SMAAModel model;
	private SMAAResults results;
	private SMAASimulator simulator;
	private ViewBuilder rightViewBuilder;
	private LeftTreeModel leftTreeModel;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MainApp app = new MainApp();
		app.startGui();
	}


	private void startGui() {
		GUIHelper.initializeLookAndFeel();

	   	model = new SMAAModel("model");
		frame = new JFrame("SMAA");
		frame.setPreferredSize(new Dimension(800, 500));
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		initComponents(frame);
		model.addPropertyChangeListener(new SMAAModelListener());
		addAlternative();
		addAlternative();
		addAlternative();
		addUniformCriterion();
		addGaussianCriterion();
		//addOrdinalCriterion();
		expandLeftMenu();
		frame.pack();
		frame.setVisible(true);	
	}
		
	private void rebuildRightPanel() {
		splitPane.setRightComponent(rightViewBuilder.buildPanel());
	}

	private void initComponents(JFrame frame) {
	   splitPane = new JSplitPane();
	   
	   splitPane.setResizeWeight(0.0);	   
	   splitPane.setDividerSize(1);
	   splitPane.setDividerLocation(-1);
	   
	   initLeftPanel();
	   setRightViewToCriteria();
	   
	   frame.getContentPane().setLayout(new BorderLayout());
	   frame.getContentPane().add("Center", splitPane);
	   frame.setJMenuBar(createMenuBar());
	}
	
	private void setRightViewToCentralWeights() {		
		rightViewBuilder = new CentralWeightsView(results, this);		
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
		rightViewBuilder = new CriterionView(node, model);
		rebuildRightPanel();
	}	
	
	private void initLeftPanel() {
		leftTreeModel = new LeftTreeModel(model);
		leftTree = new JTree(new LeftTreeModel(model));
		leftTree.addTreeSelectionListener(new LeftTreeSelectionListener());
		leftTree.setEditable(true);
		splitPane.setLeftComponent(leftTree);
	}
	
	private void expandLeftMenu() {
		leftTree.expandPath(new TreePath(new Object[]{leftTreeModel.getRoot(), leftTreeModel.getAlternativesNode()}));
		leftTree.expandPath(new TreePath(new Object[]{leftTreeModel.getRoot(), leftTreeModel.getCriteriaNode()}));
		leftTree.expandPath(new TreePath(new Object[]{leftTreeModel.getRoot(), leftTreeModel.getResultsNode()}));
	}


	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);
		
		JButton toolBarAddAltButton = new JButton("New alternative");
		JMenu toolBarAddCritMenu = new JMenu("New criterion");
		JMenuItem addUnifButton = new JMenuItem("Uniform");
		JMenuItem addGaussianButton = new JMenuItem("Gaussian");
		JMenuItem addOrdinalButton = new JMenuItem("Ordinal");
				
		toolBarAddAltButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addAlternative();
			}
		});
		addUnifButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addUniformCriterion();
			}			
		});
		addGaussianButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addGaussianCriterion();
			}			
		});
		addOrdinalButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addOrdinalCriterion();
			}			
		});

		
		toolBarAddCritMenu.add(addUnifButton);
		toolBarAddCritMenu.add(addGaussianButton);
		//toolBarAddCritMenu.add(addOrdinalButton);
		menuBar.add(toolBarAddAltButton);
		menuBar.add(toolBarAddCritMenu);
		
		return menuBar;
	}


	protected void addGaussianCriterion() {
		model.addCriterion(new GaussianCriterion(generateNextCriterionName()));
		expandLeftMenu();						
	}

	protected void addUniformCriterion() {
		model.addCriterion(new UniformCriterion(generateNextCriterionName()));
		expandLeftMenu();				
	}
	
	protected void addOrdinalCriterion() {
		model.addCriterion(new OrdinalCriterion(generateNextCriterionName()));
		expandLeftMenu();				
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
				model.addAlternative(a);
				expandLeftMenu();
				return;
			}
			index++;
		}
	}
	
	private class LeftTreeSelectionListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {
			Object node = e.getPath().getLastPathComponent();
			if (node == leftTreeModel.getAlternativesNode()) {
				setRightViewToAlternatives();
			} else if (node == leftTreeModel.getCriteriaNode()){
				setRightViewToCriteria();
			} else if (node instanceof Criterion) {
				setRightViewToCriterion((Criterion)node);
			} else if (node instanceof Alternative) {
				// do something? maybe not
			} else if (node == leftTreeModel.getCentralWeightsNode()) {
				setRightViewToCentralWeights();
			}
		}		
	}

	private class SMAAModelListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(SMAAModel.PROPERTY_ALTERNATIVES) ||
					evt.getPropertyName().equals(SMAAModel.PROPERTY_CRITERIA)) {	
				buildNewSimulator();
				rebuildRightPanel();
				expandLeftMenu();
			}
		}
	}

	public void startSimulation() {
		simulator.restart();		
	}

	private void buildNewSimulator() {
		simulator = new SMAASimulator(model, 10000);
		results = simulator.getResults();
		if (rightViewBuilder instanceof CentralWeightsView) {
			((CentralWeightsView)rightViewBuilder).setResults(results);
		}
	}

}
