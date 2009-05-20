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

import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

import nl.rug.escher.common.gui.LayoutUtil;
import nl.rug.escher.common.gui.ViewBuilder;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.ButtonBarBuilder2;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.Alternative;
import fi.smaa.Criterion;
import fi.smaa.SMAAResults;
import fi.smaa.SMAAResultsListener;

public class CentralWeightsView implements ViewBuilder {
	
	private SMAAResults results;
	private MainApp main;
	private JButton simulateButton;
	private JLabel iterationsLabel;
	private JLabel[][] valCells;	

	public CentralWeightsView(SMAAResults results, MainApp main) {
		this.results = results;
		this.main = main;
	}
	
	public void setResults(SMAAResults results) {
		this.results = results;
	}

	public JComponent buildPanel() {
		int numAlts = results.getAlternatives().size();
		int numCrit = results.getCriteria().size();
		
		FormLayout layout = new FormLayout(
				"pref",
				"p, 3dlu, p, 3dlu, p");
		
		for (int i=0;i<numAlts;i++) {
			LayoutUtil.addRow(layout);
		}
		for (int i=0;i<numCrit;i++) {
			LayoutUtil.addColumn(layout);
		}
		
		int fullWidth = 1 + numCrit * 2;;

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("Central weight vectors", cc.xyw(1, 1, fullWidth));
		
		buildCriteriaLabels(builder);
		buildAlternativeLabels(builder);
		buildCentralWeightPart(builder);

		iterationsLabel = new JLabel("0");
		builder.add(iterationsLabel, cc.xy(3, numAlts*2 + 5));
		
		ButtonBarBuilder2 bbuilder = new ButtonBarBuilder2();
		simulateButton = new JButton("simulate");
		bbuilder.addButton(simulateButton);
		simulateButton.addActionListener(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				main.startSimulation();
			}			
		});
		builder.add(bbuilder.getPanel(), cc.xy(1, numAlts*2 + 5));
		
		results.addResultsListener(new MyResultsListener());
		
		fireResultsChanged();		
		return builder.getPanel();
	}
	
	
	private void buildCentralWeightPart(PanelBuilder builder) {
		CellConstraints cc = new CellConstraints();
		int numAlternatives = results.getAlternatives().size();
		int numCriteria = results.getCriteria().size();
		valCells = new JLabel[numAlternatives][numCriteria];
		int startRow = 5;
		int startCol = 3;
		for (int altIndex=0;altIndex<numAlternatives;altIndex++) {
			for (int critIndex=0;critIndex<numCriteria;critIndex++) {
				JLabel label = new JLabel("NA");
				valCells[altIndex][critIndex] = label;
				builder.add(label, cc.xy(startCol + critIndex * 2, startRow + altIndex * 2));
			}
		}
	}

	private void buildAlternativeLabels(PanelBuilder builder) {
		CellConstraints cc = new CellConstraints();
		int col = 1;
		int row = 5;
		for (Alternative alt : results.getAlternatives()) {
			builder.add(BasicComponentFactory.createLabel(
					new PresentationModel<Alternative>(alt).getModel(Alternative.PROPERTY_NAME)),
					cc.xy(col, row));
			row += 2;
		}
	}

	private void buildCriteriaLabels(PanelBuilder builder) {
		CellConstraints cc = new CellConstraints();
		int col = 3;
		int row = 3;
		for (Criterion c : results.getCriteria()) {
			builder.add(BasicComponentFactory.createLabel(
					new PresentationModel<Criterion>(c).getModel(Criterion.PROPERTY_NAME)),
					cc.xy(col, row));
			col += 2;
		}
	}


	private class MyResultsListener implements SMAAResultsListener {
		public void resultsChanged() {
			fireResultsChanged();
		}
	}
	
	public void fireResultsChanged() {
		iterationsLabel.setText(results.getIteration().toString());
		
		Map<Alternative, List<Double>> cws = results.getCentralWeightVectors();
		
		NumberFormat format = DecimalFormat.getInstance();
		format.setMaximumFractionDigits(2);
		
		for (int altIndex=0;altIndex<results.getAlternatives().size();altIndex++) {
			Alternative alt = results.getAlternatives().get(altIndex);
			List<Double> cw = cws.get(alt);
			for (int critIndex=0;critIndex<results.getCriteria().size();critIndex++) {
				Double val = cw.get(critIndex);
				String stringVal = val == null ? "NA" : val.equals(Double.NaN) ? "NA " : format.format(val);
				valCells[altIndex][critIndex].setText(stringVal);
			}
		}
	}

}
