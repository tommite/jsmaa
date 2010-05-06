package fi.smaa.jsmaa.gui.views;

import javax.swing.JComponent;
import javax.swing.JLabel;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.adapter.BasicComponentFactory;

import fi.smaa.jsmaa.gui.IntervalFormat;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class DefaultScaleRenderer implements ScaleRenderer {

	public JComponent getScaleComponent(Criterion c) {
		if (c instanceof ScaleCriterion) {
			PresentationModel<ScaleCriterion> cpm = new PresentationModel<ScaleCriterion>((ScaleCriterion) c);				
			return BasicComponentFactory.createLabel(cpm.getModel(ScaleCriterion.PROPERTY_SCALE), new IntervalFormat());
		}
		return new JLabel("NA");
	}
}
