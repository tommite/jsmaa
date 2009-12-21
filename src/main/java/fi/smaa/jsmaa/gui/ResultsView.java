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

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.common.gui.ViewBuilder;

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
				"p, 3dlu, p, 3dlu, p, 3dlu, p");		
		
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
		
		return builder.getPanel();
	}

	private JComponent buildFigurePart() {		
		ChartPanel chartPanel = new ChartPanel(chart);
		return chartPanel;
	}

}
