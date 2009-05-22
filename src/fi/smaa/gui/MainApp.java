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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import nl.rug.escher.common.gui.GUIHelper;
import nl.rug.escher.common.gui.ViewBuilder;

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;

import fi.smaa.Alternative;
import fi.smaa.AlternativeExistsException;
import fi.smaa.Criterion;
import fi.smaa.GaussianCriterion;
import fi.smaa.OrdinalCriterion;
import fi.smaa.SMAAModel;
import fi.smaa.SMAAResults;
import fi.smaa.SMAAResultsListener;
import fi.smaa.SMAASimulator;
import fi.smaa.UniformCriterion;

@SuppressWarnings("unchecked")
public class MainApp {
	
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
		rightPane.setViewportView(rightViewBuilder.buildPanel());
	}

	private void initComponents(JFrame frame) {
	   splitPane = new JSplitPane();
	   splitPane.setResizeWeight(0.0);	   
	   splitPane.setDividerSize(2);
	   splitPane.setDividerLocation(-1);
	   rightPane = new JScrollPane();
	   splitPane.setRightComponent(rightPane);
	   
	   initLeftPanel();
	   setRightViewToCriteria();
	   
	   frame.getContentPane().setLayout(new BorderLayout());
	   frame.getContentPane().add("Center", splitPane);
	   frame.getContentPane().add("South", createToolBar());
	   frame.setJMenuBar(createMenuBar());
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
		rightViewBuilder = new CriterionView(node, model);
		rebuildRightPanel();
	}	
	
	private void initLeftPanel() {
		leftTreeModel = new LeftTreeModel(model);
		leftTree = new JTree(new LeftTreeModel(model));
		leftTree.addTreeSelectionListener(new LeftTreeSelectionListener());
		leftTree.setEditable(false);
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
		
		JMenu fileMenu = createFileMenu();
		JMenu modelMenu = createModelMenu();
		JMenu critMenu = createCriteriaMenu();	
		JMenu altMenu = createAlternativeMenu();
		
		menuBar.add(fileMenu);
		menuBar.add(modelMenu);
		menuBar.add(critMenu);
		menuBar.add(altMenu);
		
		return menuBar;
	}


	private JMenu createFileMenu() {
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('f');
		JMenuItem quitItem = new JMenuItem("Quit");
		quitItem.setMnemonic('q');
		fileMenu.add(quitItem);
		quitItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				quit();
			}
		});
		return fileMenu;
	}


	protected void quit() {
		System.exit(0);
	}


	private JMenu createAlternativeMenu() {
		JMenu alternativeMenu = new JMenu("Alternative");
		alternativeMenu.setMnemonic('a');
		JMenuItem addAltButton = new JMenuItem("New");
		addAltButton.setMnemonic('n');
				
		addAltButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addAlternative();
			}
		});
		alternativeMenu.add(addAltButton);
		return alternativeMenu;
	}


	private JMenu createModelMenu() {
		JMenu modelMenu = new JMenu("Model");
		modelMenu.setMnemonic('m');
		JMenuItem modelRename = new JMenuItem("Rename");
		modelRename.setMnemonic('r');
		modelRename.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				startRenameModel();
			}
		});
		modelMenu.add(modelRename);
		return modelMenu;
	}


	private JMenu createCriteriaMenu() {
		JMenu criteriaMenu = new JMenu("Criterion");
		criteriaMenu.setMnemonic('c');
		JMenu addCritMenu = new JMenu("New");
		addCritMenu.setMnemonic('n');
		JMenuItem addUnifButton = new JMenuItem("Uniform");
		addUnifButton.setMnemonic('u');
		JMenuItem addGaussianButton = new JMenuItem("Gaussian");
		addGaussianButton.setMnemonic('g');
		JMenuItem addOrdinalButton = new JMenuItem("Ordinal");
		addOrdinalButton.setMnemonic('o');
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
		addCritMenu.add(addUnifButton);
		//toolBarAddCritMenu.add(addOrdinalButton);			
		addCritMenu.add(addGaussianButton);
		criteriaMenu.add(addCritMenu);
		return criteriaMenu;
	}


	protected void startRenameModel() {
		// TODO Auto-generated method stub
		
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
				try {
					model.addAlternative(a);
					expandLeftMenu();				
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
			} else if (node == leftTreeModel.getRankAcceptabilitiesNode()) {
				setRightViewToRankAcceptabilities();
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

	private void buildNewSimulator() {
		simulator = SMAASimulator.initSimulator(model, 10000);		
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
			int amount = results.getIteration() * 100 / simulator.getTotalIterations();
			simulationProgress.setValue(amount);
			if (amount < 100) {
				simulationProgress.setString("Simulating: " + Integer.toString(amount) + "% done");
			} else {
				simulationProgress.setString("Simulation complete.");
			}
		}
	}

}
