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

import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.common.gui.LayoutUtil;
import fi.smaa.jsmaa.gui.jfreechart.RankAcceptabilitiesDataset;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.simulator.SMAA2Results;

public class RankAcceptabilitiesView extends ResultsView {
	
	private JLabel[][] valCells;
	
	public RankAcceptabilitiesView(SMAA2Results results) {
		super(results);
	}

	@Override
	synchronized protected void fireResultsChanged() {
		Map<Alternative, List<Double>> cws = ((SMAA2Results) results).getRankAcceptabilities();
		
		for (int altIndex=0;altIndex<getNumAlternatives();altIndex++) {
			List<Double> cw = cws.get(results.getAlternatives().get(altIndex));
			for (int rank=0;rank<getNumAlternatives();rank++) {
				if (valCells[altIndex][rank] != null) {
					valCells[altIndex][rank].setText(formatDouble(cw.get(rank)));
				}
			}
		}
	}

	synchronized public JComponent buildPanel() {
		
		FormLayout layout = new FormLayout(
				"pref",
				"p, 3dlu, p, 3dlu, p, 3dlu, p");
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("Rank acceptabilities", cc.xy(1, 1));
		builder.add(buildAcceptabilitiesPart(), cc.xy(1, 3));

		builder.addSeparator("", cc.xy(1, 5));
		builder.add(buildFigurePart(), cc.xy(1, 7));
		
		fireResultsChanged();		
		return builder.getPanel();
	}

	private JComponent buildAcceptabilitiesPart() {
		int numAlts = getNumAlternatives();
		
		FormLayout layout = new FormLayout(
				"pref",
				"p, 3dlu, p, 3dlu, p, 3dlu, p");
		
		int[] groupCol = new int[numAlts];
		
		for (int i=0;i<numAlts;i++) {
			LayoutUtil.addRow(layout);
			layout.appendColumn(ColumnSpec.decode("5dlu"));
			if (i == numAlts -1) {
				layout.appendColumn(ColumnSpec.decode("left:pref:grow"));
			} else {
				layout.appendColumn(ColumnSpec.decode("center:pref"));				
			}
			groupCol[i] = 3 + 2*i;
		}
		
		PanelBuilder builder = new PanelBuilder(layout);
		int startCol = 3;
		
		CellConstraints cc1 = new CellConstraints();
		for (int i=1;i<=getNumAlternatives();i++) {
			JLabel label = new JLabel('r' + new Integer(i).toString());
			label.setToolTipText("Rank " + new Integer(i).toString());
			builder.add(label, cc1.xy(startCol, 1));
				startCol += 2;
		}
		int startRow = 3;
		int startCol1 = 1;
		CellConstraints cc2 = new CellConstraints();
		for (Alternative alt : results.getAlternatives()) {
			builder.add(BasicComponentFactory.createLabel(
					new PresentationModel<Alternative>(alt).getModel(Alternative.PROPERTY_NAME)),
					cc2.xy(startCol1, startRow));
			if (true) {
				startRow += 2;
			} else {
				startCol1 += 2;
			}
		}
		CellConstraints cc3 = new CellConstraints();
		valCells = new JLabel[getNumAlternatives()][getNumAlternatives()];
		
		int startRow1 = 3;
		int startCol2 = 3;
		for (int altIndex=0;altIndex<getNumAlternatives();altIndex++) {
			for (int rank=0;rank<getNumAlternatives();rank++) {
				JLabel label = new JLabel("NA");
				valCells[altIndex][rank] = label;
				builder.add(label, cc3.xy(startCol2 + rank*2, startRow1 + altIndex*2));
			}
		}
		return builder.getPanel();
	}

	private JComponent buildFigurePart() {
		CategoryDataset dataset = new RankAcceptabilitiesDataset((SMAA2Results) results);
		final JFreeChart chart = ChartFactory.createStackedBarChart(
                "", "Alternative", "Rank Acceptability",
                dataset, PlotOrientation.VERTICAL, true, true, false);
		chart.getCategoryPlot().getRangeAxis().setUpperBound(1.0);
		ChartPanel chartPanel = new ChartPanel(chart);
		return chartPanel;
	}
}
