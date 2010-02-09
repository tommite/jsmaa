package fi.smaa.jsmaa.gui.presentation;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.gui.components.RankSelectorGroup;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.ImpactMatrix;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.Rank;

public class OrdinalCriterionMeasurementsPM {
	
	private RankSelectorGroup selectorGroup;
	private ImpactMatrix matrix;

	public OrdinalCriterionMeasurementsPM(OrdinalCriterion c, ImpactMatrix matrix) {
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
