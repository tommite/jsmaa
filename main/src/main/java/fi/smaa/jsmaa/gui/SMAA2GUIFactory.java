/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

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

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.drugis.common.gui.ViewBuilder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

import fi.smaa.jsmaa.gui.components.CentralWeightsCellRenderer;
import fi.smaa.jsmaa.gui.components.ResultsCellColorRenderer;
import fi.smaa.jsmaa.gui.components.ResultsTable;
import fi.smaa.jsmaa.gui.jfreechart.CentralWeightsDataset;
import fi.smaa.jsmaa.gui.jfreechart.RankAcceptabilitiesDataset;
import fi.smaa.jsmaa.gui.presentation.CentralWeightTableModel;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModel;
import fi.smaa.jsmaa.gui.presentation.RankAcceptabilityTableModel;
import fi.smaa.jsmaa.gui.views.ResultsView;
import fi.smaa.jsmaa.gui.views.ViewWithHeader;
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
	protected SMAA2GUIFactory(Window parent, SMAAModel smaaModel, MenuDirector director) {
		super(parent, smaaModel, director);
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
			JFreeChart chart = null;
			if (isChartDrawable()) {
				chart = ChartFactory.createLineChart(
				        "", "Criterion", "Central Weight",
				        centralWeightsDataset, PlotOrientation.VERTICAL, true, true, false);
				LineAndShapeRenderer renderer = new LineAndShapeRenderer(true, true);
				chart.getCategoryPlot().setRenderer(renderer);
			}
			ResultsTable table = new ResultsTable(centralWeightsTM);
			table.setAutoCreateRowSorter(true);			
			table.setDefaultRenderer(Object.class, new CentralWeightsCellRenderer(1.0));
			table.getTableHeader().setToolTipText("CF = Confidence Factor");
			return new ViewWithHeader("Central weight vectors", new ResultsView(parent, table, chart, FileNames.ICON_SCRIPT));
		} else if (o == treeModel.getRankAcceptabilitiesNode()) {
			JFreeChart chart = null;
			if (isChartDrawable()) {
				chart = ChartFactory.createStackedBarChart(
				        "", "Alternative", "Rank Acceptability",
				        rankAcceptabilitiesDataset, PlotOrientation.VERTICAL, true, true, false);
				chart.getCategoryPlot().getRangeAxis().setUpperBound(1.0);	
			}
			ResultsTable table = new ResultsTable(rankAcceptabilitiesTM);
			table.setAutoCreateRowSorter(true);			
			table.setDefaultRenderer(Object.class, new ResultsCellColorRenderer(1.0));
			table.getTableHeader().setToolTipText("Ranks in descending order, 1 is the best, 2 the second best, etc.");
			return new ViewWithHeader("Rank acceptability indices", new ResultsView(parent, table, chart, FileNames.ICON_SCRIPT));
		} else {
			return super.buildView(o);
		}	
	}

	@Override
	protected JMenuItem buildAddCriterionItem() {
		JMenu menu = new JMenu("Add new");
		menu.setIcon(ImageFactory.IMAGELOADER.getIcon(FileNames.ICON_ADDCRITERION));
		addUtilityAddItemsToMenu(menu);
		return menu;
	}
	
	@Override
	protected JButton buildToolBarAddCriterionButton() {
		JButton button = new JButton(ImageFactory.IMAGELOADER.getIcon(FileNames.ICON_ADDCRITERION));
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
		cardCrit.setIcon(ImageFactory.IMAGELOADER.getIcon(FileNames.ICON_CARDINALCRITERION));		

		cardCrit.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addCriterionAndStartRename(new ScaleCriterion(generateNextCriterionName()));
			}			
		});
		return cardCrit;
	}	
	
	private JMenuItem createAddOrdinalCriterionItem() {
		JMenuItem ordCrit = new JMenuItem("Ordinal");
		ordCrit.setIcon(ImageFactory.IMAGELOADER.getIcon(FileNames.ICON_ORDINALCRITERION));		

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
				ImageFactory.IMAGELOADER.getIcon(FileNames.ICON_CENTRALWEIGHTS));
		cwItem.setMnemonic('c');
		JMenuItem racsItem = new JMenuItem("Rank acceptability indices", 
				ImageFactory.IMAGELOADER.getIcon(FileNames.ICON_RANKACCEPTABILITIES));
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
