package fi.smaa.jsmaa.gui.presentation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.CardinalMeasurement;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ImpactMatrix;

@SuppressWarnings("serial")
public class ImpactMatrixPresentationModel extends PresentationModel<ImpactMatrix> {

	public ImpactMatrixPresentationModel(ImpactMatrix bean) {
		super(bean);
	}
	
	public List<Alternative> getAlternatives() {
		return getBean().getAlternatives();
	}
	
	public ValueModel getMeasurementHolder(Alternative a, Criterion criterion) {
		ValueHolder holder = new ValueHolder(getBean().getMeasurement((CardinalCriterion) criterion, a));
		holder.addPropertyChangeListener(new HolderListener(a, criterion));
		return holder;
	}
	
	private class HolderListener implements PropertyChangeListener {
		private Alternative a;
		private Criterion c;
		public HolderListener(Alternative a, Criterion c) {
			this.a = a;
			this.c = c;
		}
		public void propertyChange(PropertyChangeEvent evt) {
			getBean().setMeasurement((CardinalCriterion) c, a, (CardinalMeasurement)evt.getNewValue());
		}
	}
}
