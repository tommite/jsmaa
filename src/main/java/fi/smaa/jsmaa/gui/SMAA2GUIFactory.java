package fi.smaa.jsmaa.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.components.ResultsCellRenderer;
import fi.smaa.jsmaa.gui.components.ResultsTable;
import fi.smaa.jsmaa.gui.jfreechart.CentralWeightsDataset;
import fi.smaa.jsmaa.gui.jfreechart.RankAcceptabilitiesDataset;
import fi.smaa.jsmaa.gui.presentation.CentralWeightTableModel;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModel;
import fi.smaa.jsmaa.gui.presentation.RankAcceptabilityTableModel;
import fi.smaa.jsmaa.gui.views.ResultsView;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.ScaleCriterion;
import fi.smaa.jsmaa.simulator.SMAA2Results;


@SuppressWarnings("serial")
public class SMAA2GUIFactory extends AbstractGUIFactory<LeftTreeModel, SMAAModel> {
	
	private CentralWeightsDataset centralWeightsDataset;
	private CentralWeightTableModel centralWeightsTM;
	private RankAcceptabilitiesDataset rankAcceptabilitiesDataset;
	private RankAcceptabilityTableModel rankAcceptabilitiesTM;

	@SuppressWarnings("unchecked")
	protected SMAA2GUIFactory(SMAAModel smaaModel, MenuDirector director) {
		super(smaaModel, director);
		SMAA2Results emptyResults = new SMAA2Results(Collections.EMPTY_LIST, Collections.EMPTY_LIST, 1);
		centralWeightsDataset = new CentralWeightsDataset(emptyResults);
		centralWeightsTM = new CentralWeightTableModel(emptyResults);	
		rankAcceptabilitiesDataset = new RankAcceptabilitiesDataset(emptyResults);	
		rankAcceptabilitiesTM = new RankAcceptabilityTableModel(emptyResults);
	}
	
	synchronized public void setResults(SMAA2Results results) {
		centralWeightsDataset.setResults(results);
		centralWeightsTM.setResults(results);	
		rankAcceptabilitiesDataset.setResults(results);	
		rankAcceptabilitiesTM.setResults(results);		
	}
	
	protected LeftTreeModel buildTreeModel() {
		return new LeftTreeModel(smaaModel);
	}
	
	@Override
	public ViewBuilder buildView(Object o) {
		if (o == treeModel.getCentralWeightsNode()) {
			final JFreeChart chart = ChartFactory.createLineChart(
			        "", "Criterion", "Central Weight",
			        centralWeightsDataset, PlotOrientation.VERTICAL, true, true, false);
			LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, true);
			chart.getCategoryPlot().setRenderer(renderer);
			ResultsTable table = new ResultsTable(centralWeightsTM);
			table.setDefaultRenderer(Object.class, new ResultsCellRenderer(1.0));
			return new ResultsView("Central weight vectors", table, chart);
		} else if (o == treeModel.getRankAcceptabilitiesNode()) {
			final JFreeChart chart = ChartFactory.createStackedBarChart(
			        "", "Alternative", "Rank Acceptability",
			        rankAcceptabilitiesDataset, PlotOrientation.VERTICAL, true, true, false);
			chart.getCategoryPlot().getRangeAxis().setUpperBound(1.0);
			ResultsTable table = new ResultsTable(rankAcceptabilitiesTM);
			table.setDefaultRenderer(Object.class, new ResultsCellRenderer(1.0));		
			return new ResultsView("Rank acceptability indices", table, chart);
		} else {
			return super.buildView(o);
		}	
	}

	@Override
	protected JMenuItem buildAddCriterionItem() {
		JMenu menu = new JMenu("Add new");
		menu.setIcon(ImageLoader.getIcon(FileNames.ICON_ADDCRITERION));
		addUtilityAddItemsToMenu(menu);
		return menu;
	}
	
	@Override
	protected JButton buildToolBarAddCriterionButton() {
		JButton button = new JButton(ImageLoader.getIcon(FileNames.ICON_ADDCRITERION));
		button.setToolTipText("Add criterion");		
		final JPopupMenu addMenu = new JPopupMenu();
		addUtilityAddItemsToMenu(addMenu);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent evt) {
				addMenu.show((Component) evt.getSource(), 
						evt.getX(), evt.getY());
			}
		});			
		return button;
	}
	
	private void addUtilityAddItemsToMenu(Container item) {
		JMenuItem cardCrit = createAddScaleCriterionItem();
		JMenuItem ordCrit = createAddOrdinalCriterionItem();		
		item.add(cardCrit);
		item.add(ordCrit);
	}	
	
	private JMenuItem createAddScaleCriterionItem() {
		JMenuItem cardCrit = new JMenuItem("Cardinal");		
		cardCrit.setIcon(ImageLoader.getIcon(FileNames.ICON_CARDINALCRITERION));		

		cardCrit.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addCriterionAndStartRename(new ScaleCriterion(generateNextCriterionName()));
			}			
		});
		return cardCrit;
	}	
	
	private JMenuItem createAddOrdinalCriterionItem() {
		JMenuItem ordCrit = new JMenuItem("Ordinal");
		ordCrit.setIcon(ImageLoader.getIcon(FileNames.ICON_ORDINALCRITERION));		

		ordCrit.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addCriterionAndStartRename(new OrdinalCriterion(generateNextCriterionName()));
			}			
		});
		return ordCrit;
	}	

	@Override
	protected JMenu buildResultsMenu() {
		JMenu resultsMenu = new JMenu("Results");
		resultsMenu.setMnemonic('r');
		JMenuItem cwItem = new JMenuItem("Central weight vectors", 
				ImageLoader.getIcon(FileNames.ICON_CENTRALWEIGHTS));
		cwItem.setMnemonic('c');
		JMenuItem racsItem = new JMenuItem("Rank acceptability indices", 
				ImageLoader.getIcon(FileNames.ICON_RANKACCEPTABILITIES));
		racsItem.setMnemonic('r');
		
		cwItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				Focuser.focus(tree, treeModel, treeModel.getCentralWeightsNode());
			}
		});
		
		racsItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				Focuser.focus(tree, treeModel, treeModel.getRankAcceptabilitiesNode());
			}			
		});
		
		resultsMenu.add(cwItem);
		resultsMenu.add(racsItem);
		return resultsMenu;
	}
}
