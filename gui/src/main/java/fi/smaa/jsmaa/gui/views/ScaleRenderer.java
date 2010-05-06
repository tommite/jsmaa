package fi.smaa.jsmaa.gui.views;

import javax.swing.JComponent;

import fi.smaa.jsmaa.model.Criterion;

public interface ScaleRenderer {
	JComponent getScaleComponent(Criterion c);
}
