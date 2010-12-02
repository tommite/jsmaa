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
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.drugis.common.ImageLoader;
import org.drugis.common.gui.GUIHelper;
import org.drugis.common.gui.ViewBuilder;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jidesoft.swing.JideButton;

import fi.smaa.jsmaa.gui.GNUPlotDialog;
import fi.smaa.jsmaa.gui.jfreechart.PlotConverter;
import fi.smaa.jsmaa.gui.jfreechart.PlotConverterFactory;
import fi.smaa.jsmaa.gui.jfreechart.SMAADataSet;

public class ResultsView implements ViewBuilder {

	private JTable table;
	private JFreeChart chart;
	private Window parent;
	private String scriptIcon;
	private boolean showChart;

	public ResultsView(Window parent, JTable table, JFreeChart chart, String scriptIcon, boolean showChart) {
		this.chart = chart;
		this.table = table;
		this.parent = parent;
		this.scriptIcon = scriptIcon;
		this.showChart = showChart;
	}
	
	public ResultsView(Window parent, JTable table, JFreeChart chart, String scriptIcon) {
		this.chart = chart;
		this.table = table;
		this.parent = parent;
		this.scriptIcon = scriptIcon;
		this.showChart = chart != null;
	}	

	public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"pref",
				"p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p");		
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();	
		
		JScrollPane spane = new JScrollPane(table);
		builder.add(spane, cc.xy(1, 1));
	
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		builder.addSeparator("", cc.xy(1, 3));	
		builder.add(buildFigurePart(), cc.xy(1, 5));
		builder.add(buildExportButton(), cc.xy(1, 7, "left, center"));
		
		return builder.getPanel();
	}

	@SuppressWarnings("serial")
	private Component buildExportButton() {
		JButton exportButton = new JideButton("Export figure dataset as GNUPlot script", ImageLoader.getIcon(scriptIcon));
		exportButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				PlotConverter c = PlotConverterFactory.getConverter((SMAADataSet<?>) ((CategoryPlot) chart.getPlot()).getDataset());
				GNUPlotDialog d = new GNUPlotDialog(null, c);
				GUIHelper.centerWindow(d, parent);
				d.setVisible(true);				
			}			
		});
		return exportButton;
	}

	private JComponent buildFigurePart() {
		if (showChart) {
			ChartPanel chartPanel = new ChartPanel(chart);
			return chartPanel;			
		} else {
			return new JLabel("Cannot show chart - column charts supported for up to 20 alternatives");
		}
	}

}
