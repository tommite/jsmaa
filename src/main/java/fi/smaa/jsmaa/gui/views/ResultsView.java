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

package fi.smaa.jsmaa.gui.views;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.CategoryDataset;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideButton;

import fi.smaa.common.gui.ImageLoader;
import fi.smaa.common.gui.ViewBuilder;
import fi.smaa.jsmaa.gui.FileNames;
import fi.smaa.jsmaa.gui.GNUPlotDialog;
import fi.smaa.jsmaa.gui.jfreechart.PlotConverter;
import fi.smaa.jsmaa.gui.jfreechart.PlotConverterFactory;

public class ResultsView implements ViewBuilder {

	private JTable table;
	private JFreeChart chart;
	private String title;

	public ResultsView(String title, JTable table, JFreeChart chart) {
		this.title = title;
		this.chart = chart;
		this.table = table;
	}

	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"pref",
				"p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p");		
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();	
		
		builder.addSeparator(title, cc.xy(1, 1));
		JScrollPane spane = new JScrollPane(table);
		builder.add(spane, cc.xy(1, 3));
	
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setAutoCreateRowSorter(true);
		builder.addSeparator("", cc.xy(1, 5));	
		builder.add(buildFigurePart(), cc.xy(1, 7));
		builder.add(buildExportButton(), cc.xy(1, 9, "left, center"));
		
		return builder.getPanel();
	}

	private Component buildExportButton() {
		JButton exportButton = new JideButton("Export figure dataset as GNUPlot script", ImageLoader.getIcon(FileNames.ICON_SCRIPT));
		exportButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				CategoryDataset dataset = (CategoryDataset) ((CategoryPlot) chart.getPlot()).getDataset();
				PlotConverter c = PlotConverterFactory.getConverter(dataset);
				GNUPlotDialog d = new GNUPlotDialog(null, c);
				d.setVisible(true);
			}			
		});
		return exportButton;
	}

	private JComponent buildFigurePart() {		
		ChartPanel chartPanel = new ChartPanel(chart);
		return chartPanel;
	}

}
