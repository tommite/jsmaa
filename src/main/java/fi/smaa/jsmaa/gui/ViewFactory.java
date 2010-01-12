package fi.smaa.jsmaa.gui;

import fi.smaa.common.gui.ViewBuilder;

/**
 * Abstract factory for getting (right pane) views for objects.
 * 
 * @author Tommi Tervonen
 */
public interface ViewFactory {
	public ViewBuilder getView(Object o);
}
