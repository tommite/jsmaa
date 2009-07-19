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

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;

import fi.smaa.common.gui.LayoutUtil;
import fi.smaa.jsmaa.SMAATRIResults;
import fi.smaa.jsmaa.model.Alternative;

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
		int numAlts = getNumAlternatives();
		int numCats = getNumCategories();
		
		FormLayout layout = new FormLayout(
				"pref",
				"p, 3dlu, p");
		
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

		int fullWidth = 1 + numCats * 2;
		
		PanelBuilder builder = new PanelBuilder(layout);
		builder.setDefaultDialogBorder();
		CellConstraints cc = new CellConstraints();
		
		builder.addSeparator("Category acceptabilities", cc.xyw(1, 1, fullWidth));
		
		buildCategoryLabels(builder, 3, 3);
		buildAlternativeLabels(builder, 5, 1, true);
		buildCategoryAcceptabilitiesPart(builder);
		
		fireResultsChanged();		
		return builder.getPanel();
	}

	private void buildCategoryAcceptabilitiesPart(PanelBuilder builder) {
		CellConstraints cc = new CellConstraints();
		valCells = new JLabel[getNumAlternatives()][getNumCategories()];
		
		int startRow = 5;
		int startCol = 3;
		for (int altIndex=0;altIndex<getNumAlternatives();altIndex++) {
			for (int catIndex=0;catIndex<getNumCategories();catIndex++) {
				JLabel label = new JLabel("NA");
				valCells[altIndex][catIndex] = label;
				builder.add(label, cc.xy(startCol + catIndex*2, startRow + altIndex*2));
			}
		}
		
	}

	private void buildCategoryLabels(PanelBuilder builder, int row, int startCol) {
		CellConstraints cc = new CellConstraints();
		SMAATRIResults triRes = (SMAATRIResults) results;		
		for (int i=0;i<triRes.getCategories().size();i++) {
			Alternative cat = triRes.getCategories().get(i);
			JLabel label = BasicComponentFactory.createLabel(new PresentationModel<Alternative>(cat).getModel(
					Alternative.PROPERTY_NAME));
			builder.add(label, cc.xy(startCol, row));
				startCol += 2;
		}		
	}	

}
