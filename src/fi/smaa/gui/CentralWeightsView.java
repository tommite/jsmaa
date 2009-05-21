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

import java.util.List;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;

import nl.rug.escher.common.gui.LayoutUtil;
import nl.rug.escher.common.gui.ViewBuilder;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.Alternative;
import fi.smaa.Criterion;
import fi.smaa.SMAAResults;

public class CentralWeightsView extends ResultsView implements ViewBuilder {
	
	private JLabel[][] valCells;

	public CentralWeightsView(SMAAResults results, MainApp main) {
		super(results, main);
	}
	
	public JComponent buildPanel() {
		int numAlts = getNumAlternatives();
		int numCrit = getNumCriteria();
		
		FormLayout layout = new FormLayout(
				"pref",
				"p, 3dlu, p");
		
		int[] groupCol = new int[numCrit];
		
		for (int i=0;i<numAlts;i++) {
			LayoutUtil.addRow(layout);
		}
		for (int i=0;i<numCrit;i++) {
			layout.appendColumn(ColumnSpec.decode("5dlu"));
			layout.appendColumn(ColumnSpec.decode("center:pref"));
			groupCol[i] = 3 + 2*i;			
		}
		
		layout.setColumnGroups(new int[][]{groupCol});		
		
		int fullWidth = 1 + numCrit * 2;;

		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("Central weight vectors", cc.xyw(1, 1, fullWidth));
		
		buildCriteriaLabels(builder);
		buildAlternativeLabels(builder, 5, 1, true);
		buildCentralWeightPart(builder);
		
		fireResultsChanged();		
		return builder.getPanel();
	}

	private void buildCentralWeightPart(PanelBuilder builder) {
		CellConstraints cc = new CellConstraints();
		int numAlternatives = getNumAlternatives();
		int numCriteria = getNumCriteria();
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

	
	public void fireResultsChanged() {
		Map<Alternative, List<Double>> cws = results.getCentralWeightVectors();
		
		for (int altIndex=0;altIndex<results.getAlternatives().size();altIndex++) {
			List<Double> cw = cws.get(results.getAlternatives().get(altIndex));
			for (int critIndex=0;critIndex<results.getCriteria().size();critIndex++) {
				valCells[altIndex][critIndex].setText(formatDouble(cw.get(critIndex)));
			}
		}
	}

}
