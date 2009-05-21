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

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;

import fi.smaa.Alternative;
import fi.smaa.SMAAResults;
import fi.smaa.SMAAResultsListener;

public abstract class ResultsView {

	protected SMAAResults results;
	protected NumberFormat format;	
	
	public ResultsView(SMAAResults results) {
		this.results = results;
		setupFormat();
		results.addResultsListener(new MyResultsListener());		
	}

	public void buildAlternativeLabels(PanelBuilder builder, int startRow, int startCol, boolean vertical) {
		CellConstraints cc = new CellConstraints();
		for (Alternative alt : results.getAlternatives()) {
			builder.add(BasicComponentFactory.createLabel(
					new PresentationModel<Alternative>(alt).getModel(Alternative.PROPERTY_NAME)),
					cc.xy(startCol, startRow));
			if (vertical) {
				startRow += 2;
			} else {
				startCol += 2;
			}
		}
	}

	public void setResults(SMAAResults results) {
		this.results = results;
		results.addResultsListener(new MyResultsListener());		
	}

	protected class MyResultsListener implements SMAAResultsListener {
		public void resultsChanged() {
			fireResultsChanged();
		}
	}
	
	protected abstract void fireResultsChanged();

	protected int getNumAlternatives() {
		int numAlts = results.getAlternatives().size();
		return numAlts;
	}

	protected int getNumCriteria() {
		int numCrit = results.getCriteria().size();
		return numCrit;
	}

	protected void setupFormat() {
		format = DecimalFormat.getInstance();
		format.setMaximumFractionDigits(2);
	}

	protected String formatDouble(Double val) {
		String stringVal = val == null ? "NA" : val.equals(Double.NaN) ? "NA " : format.format(val);
		return stringVal;
	}
	
}