/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper 2012.

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
package fi.smaa.jsmaa.gui.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.gui.RankSelectorGroup;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.IndependentMeasurements;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.Rank;

public class OrdinalCriterionMeasurementsPM {
	
	private RankSelectorGroup selectorGroup;
	private IndependentMeasurements matrix;

	public OrdinalCriterionMeasurementsPM(OrdinalCriterion c, IndependentMeasurements matrix) {
		this.matrix = matrix;
		List<Rank> ranks = new ArrayList<Rank>();
		for (Alternative a : matrix.getAlternatives()) {
			ranks.add((Rank) matrix.getMeasurement(c, a));
		}
		selectorGroup = new RankSelectorGroup(ranks);
	}	
	
	public List<JComboBox> getSelectors() {
		return selectorGroup.getSelectors();
	}
	
	public List<ValueModel> getNameModels() {
		List<ValueModel> l = new ArrayList<ValueModel>();
		for (Alternative a : matrix.getAlternatives()) {
			l.add(new PresentationModel<Alternative>(a).getModel(Alternative.PROPERTY_NAME));
		}
		return l;
	}
}
