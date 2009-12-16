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
import fi.smaa.jsmaa.gui.jfreechart.CategoryAcceptabilitiesDataset;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.simulator.SMAATRIResults;

public class CategoryAcceptabilitiesView extends ResultsView {
	
	private JLabel[][] valCells;

	public CategoryAcceptabilitiesView(SMAATRIResults results) {
		super(results);
	}

	@Override
	synchronized protected void fireResultsChanged() {
		SMAATRIResults triRes = (SMAATRIResults) results;
		Map<Alternative, List<Double>> cws = triRes.getCategoryAcceptabilities();
		
		for (int altIndex=0;altIndex<getNumAlternatives();altIndex++) {
			List<Double> cw = cws.get(results.getAlternatives().get(altIndex));
			for (int catIndex=0;catIndex<getNumCategories();catIndex++) {
				if (valCells[altIndex][catIndex] != null) {
					valCells[altIndex][catIndex].setText(formatDouble(cw.get(catIndex)));
				}
			}
		}
	}

	private int getNumCategories() {
		return ((SMAATRIResults) results).getCategories().size();		
	}

	synchronized public JComponent buildPanel() {
		FormLayout layout = new FormLayout(
				"pref",
				"p, 3dlu, p, 3dlu, p, 3dlu, p");		
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();	
		
		builder.addSeparator("Category acceptabilities", cc.xy(1, 1));
		builder.add(buildAcceptabilitiesPart(), cc.xy(1, 3));
	
		builder.addSeparator("", cc.xy(1, 5));	
		builder.add(buildFigurePart(), cc.xy(1, 7));
		
		fireResultsChanged();		
		return builder.getPanel();
	}

	private JComponent buildAcceptabilitiesPart() {
		int numAlts = getNumAlternatives();
		int numCats = getNumCategories();
		
		FormLayout layout = new FormLayout(
				"pref",
				"p");
		
		int[] groupCol = new int[numCats];
		
		for (int i=0;i<numAlts;i++) {
			LayoutUtil.addRow(layout);
		}
		
		for (int i=0;i<numCats;i++) {
			layout.appendColumn(ColumnSpec.decode("5dlu"));
			layout.appendColumn(ColumnSpec.decode("center:pref"));
			groupCol[i] = 3 + 2*i;
		}
		
		layout.setColumnGroups(new int[][]{groupCol});

		PanelBuilder builder = new PanelBuilder(layout);
		
		// cat labels
		CellConstraints cc1 = new CellConstraints();
		SMAATRIResults triRes = (SMAATRIResults) results;
		
		int startCol = 3;
		for (int i=0;i<triRes.getCategories().size();i++) {
			Alternative cat = triRes.getCategories().get(i);
			JLabel label = BasicComponentFactory.createLabel(new PresentationModel<Alternative>(cat).getModel(
					Alternative.PROPERTY_NAME));
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
		valCells = new JLabel[getNumAlternatives()][getNumCategories()];
		
		int startRow1 = 3;
		int startCol2 = 3;
		
		for (int altIndex=0;altIndex<getNumAlternatives();altIndex++) {
			for (int catIndex=0;catIndex<getNumCategories();catIndex++) {
				JLabel label = new JLabel("NA");
				valCells[altIndex][catIndex] = label;
				builder.add(label, cc3.xy(startCol2 + catIndex*2, startRow1 + altIndex*2));
			}
		}
		
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
