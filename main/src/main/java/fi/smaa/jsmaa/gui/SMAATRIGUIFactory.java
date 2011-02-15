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

import java.awt.BorderLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.drugis.common.ImageLoader;
import org.drugis.common.gui.ViewBuilder;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

import fi.smaa.jsmaa.gui.components.LambdaPanel;
import fi.smaa.jsmaa.gui.components.ResultsCellColorRenderer;
import fi.smaa.jsmaa.gui.components.ResultsTable;
import fi.smaa.jsmaa.gui.jfreechart.CategoryAcceptabilitiesDataset;
import fi.smaa.jsmaa.gui.presentation.CategoryAcceptabilityTableModel;
import fi.smaa.jsmaa.gui.presentation.LeftTreeModelSMAATRI;
import fi.smaa.jsmaa.gui.views.AlternativeInfoView;
import fi.smaa.jsmaa.gui.views.ResultsView;
import fi.smaa.jsmaa.gui.views.TechnicalParameterView;
import fi.smaa.jsmaa.gui.views.ViewWithHeader;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAATRIModel;
import fi.smaa.jsmaa.simulator.SMAATRIResults;

@SuppressWarnings("serial")
public class SMAATRIGUIFactory extends AbstractGUIFactory<LeftTreeModelSMAATRI, SMAATRIModel> {

	private CategoryAcceptabilityTableModel categoryAcceptabilityTM;
	private CategoryAcceptabilitiesDataset categoryAcceptabilityDataset;

	@SuppressWarnings("unchecked")
	public SMAATRIGUIFactory(Window parent, SMAATRIModel smaaModel, MenuDirector director) {
		super(parent, smaaModel, director);
		SMAATRIResults emptyResults = new SMAATRIResults(Collections.EMPTY_LIST, Collections.EMPTY_LIST, 1);
		categoryAcceptabilityTM = new CategoryAcceptabilityTableModel(emptyResults);
		categoryAcceptabilityDataset = new CategoryAcceptabilitiesDataset(emptyResults);		
	}
	
	synchronized public void setResults(SMAATRIResults results) {
		categoryAcceptabilityTM.setResults(results);
		categoryAcceptabilityDataset.setResults(results);
	}
	
	protected LeftTreeModelSMAATRI buildTreeModel() {
		return new LeftTreeModelSMAATRI(smaaModel);
	}
	
	@Override
	protected JToolBar buildBottomToolBar() {
		JToolBar tb = super.buildBottomToolBar();
		tb.add(new LambdaPanel(smaaModel), BorderLayout.EAST);
		return tb;
	}
	
	@Override
	protected JToolBar buildTopToolBar() {
		JToolBar bar = super.buildTopToolBar();

		JButton addCatButton = new JButton(ImageLoader.getIcon(FileNames.ICON_ADD));
		addCatButton.setToolTipText("Add category");
		addCatButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addCategory();
			}
		});
		bar.add(addCatButton);
		return bar;
	}	
	
	@Override
	public ViewBuilder buildView(Object o) {
		if (o instanceof Alternative && smaaModel.getCategories().contains(o)) {
			return new ViewBuilder() {
				@Override
				public JComponent buildPanel() {
					return new JPanel();
				}
			};
		} else if (o == treeModel.getModelNode()) {
			return new TechnicalParameterView(smaaModel);	
		} else if (o == treeModel.getCatAccNode()) {
			final JFreeChart chart = ChartFactory.createStackedBarChart(
			        "", "Alternative", "Category Acceptability",
			        categoryAcceptabilityDataset, PlotOrientation.VERTICAL, true, true, false);
			chart.getCategoryPlot().getRangeAxis().setUpperBound(1.0);
			ResultsTable table = new ResultsTable(categoryAcceptabilityTM);		
			table.setAutoCreateRowSorter(true);			
			table.setDefaultRenderer(Object.class, new ResultsCellColorRenderer(1.0));		
			return new ViewWithHeader("Category acceptability indices", new ResultsView(parent, table, chart, FileNames.ICON_SCRIPT));
		} else if (o == treeModel.getCategoriesNode()) {
			return new ViewWithHeader("Categories (in ascending order, top = worst)", new AlternativeInfoView(smaaModel.getCategories()));
		} else {
			return super.buildView(o);
		}		
	}

	@Override
	protected JMenuItem buildAddCriterionItem() {
		JMenuItem item = new JMenuItem("Add new");
		item.setIcon(ImageLoader.getIcon(FileNames.ICON_ADDCRITERION));
		item.addActionListener(new AddOutrankingCriterionAction());				
		return item;
	}
	
	@Override
	protected JButton buildToolBarAddCriterionButton() {
		JButton button = new JButton(ImageLoader.getIcon(FileNames.ICON_ADDCRITERION));
		button.setToolTipText("Add criterion");
		button.addActionListener(new AddOutrankingCriterionAction());
		return button;
	}	
	
	@Override
	protected List<JMenuItem> getEntityMenuList() {
		List<JMenuItem> list = super.getEntityMenuList();
		list.add(buildCategoryMenu());
		return list;
	}

	private JMenu buildCategoryMenu() {
		JMenu categoryMenu = new JMenu("Categories");
		categoryMenu.setMnemonic('t');
		JMenuItem showItem = new JMenuItem("Show");
		showItem.setMnemonic('s');
		JMenuItem addCatButton = createAddCatMenuItem();
		
		showItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				Focuser.focus(tree, treeModel, treeModel.getCategoriesNode());
			}			
		});
				
		categoryMenu.add(showItem);
		categoryMenu.addSeparator();
		categoryMenu.add(addCatButton);
		return categoryMenu;		
	}

	private JMenuItem createAddCatMenuItem() {
		JMenuItem item = new JMenuItem("Add new");
		item.setMnemonic('n');
		item.setIcon(ImageLoader.getIcon(FileNames.ICON_ADD));
		item.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				addCategory();
			}
		});
		return item;
	}	

	protected void addCategory() {
		Collection<Alternative> cats = smaaModel.getCategories();

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

	@Override
	protected JMenu buildResultsMenu() {
		JMenu resultsMenu = new JMenu("Results");
		resultsMenu.setMnemonic('r');
		JMenuItem racsItem = new JMenuItem("Category acceptability indices", 
				ImageLoader.getIcon(FileNames.ICON_RANKACCEPTABILITIES));
		racsItem.setMnemonic('r');
				
		racsItem.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				Focuser.focus(tree, treeModel, treeModel.getCatAccNode());
			}			
		});
		
		resultsMenu.add(racsItem);
		return resultsMenu;
	}

	private void addCategoryAndStartRename(Alternative a) {
		smaaModel.addCategory(a);
		Focuser.focus(tree, treeModel, a);
		tree.startEditingAtPath(treeModel.getPathForCategory(a));			
	}	
	
	@Override
	protected void confirmDeleteAlternative(Alternative alternative) {
		if (!smaaModel.getCategories().contains(alternative)) {
			super.confirmDeleteAlternative(alternative);
		} else {
			String typeName = "category";
			int conf = JOptionPane.showConfirmDialog(parent, 
					"Do you really want to delete " + typeName + " " + alternative + "?",
					"Confirm deletion",					
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
					ImageLoader.getIcon(FileNames.ICON_DELETE));
			if (conf == JOptionPane.YES_OPTION) {
				smaaModel.deleteCategory(alternative);
			}
		}
	}
	
	private class AddOutrankingCriterionAction extends AbstractAction {
		@Override
		public void actionPerformed(ActionEvent e) {
			addCriterionAndStartRename(new OutrankingCriterion(generateNextCriterionName(), true, 
					new ExactMeasurement(0.0), new ExactMeasurement(1.0)));
		}		
	}
}