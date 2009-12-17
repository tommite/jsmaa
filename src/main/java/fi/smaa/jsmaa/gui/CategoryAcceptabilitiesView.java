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

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.components.EnhancedTableHeader;
import fi.smaa.jsmaa.gui.jfreechart.CategoryAcceptabilitiesDataset;
import fi.smaa.jsmaa.gui.presentation.CategoryAcceptabilityTableModel;
import fi.smaa.jsmaa.simulator.SMAATRIResults;

public class CategoryAcceptabilitiesView implements ViewBuilder {

	private CategoryAcceptabilityTableModel model;
	private SMAATRIResults results;

	public CategoryAcceptabilitiesView(SMAATRIResults results) {
		this.results = results;
		model = new CategoryAcceptabilityTableModel((SMAATRIResults)results);		
	}

	synchronized public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"pref",
				"p, 3dlu, p, 3dlu, p, 3dlu, p");		
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();	
		
		builder.addSeparator("Category acceptabilities", cc.xy(1, 1));
		
		JTable table = new JTable(model);
		table.setTableHeader(new EnhancedTableHeader(table.getColumnModel(), table));
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		
		table.setDefaultRenderer(table.getColumnClass(0), renderer);
		DefaultTableCellRenderer headerRend = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
		headerRend.setHorizontalAlignment(SwingConstants.CENTER);
		
		
		JScrollPane spane = new JScrollPane(table);
		builder.add(spane, cc.xy(1, 3));
	
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		builder.addSeparator("", cc.xy(1, 5));	
		builder.add(buildFigurePart(), cc.xy(1, 7));
		
		return builder.getPanel();
	}

	private JComponent buildFigurePart() {
		CategoryDataset dataset = new CategoryAcceptabilitiesDataset((SMAATRIResults) results);
		final JFreeChart chart = ChartFactory.createStackedBarChart(
                "", "Alternative", "Category Acceptability",
                dataset, PlotOrientation.VERTICAL, true, true, false);
		chart.getCategoryPlot().getRangeAxis().setUpperBound(1.0);		
		ChartPanel chartPanel = new ChartPanel(chart);
		return chartPanel;
	}

}
