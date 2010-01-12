package fi.smaa.jsmaa.gui;

import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.model.SMAAModel;

public interface GUIDirector {
	public void open();
	/**
	 * Saves the current model.
	 * @return true, is succeeded, false if not.
	 */
	public boolean save();
	/**
	 * Saves the current model with a name.
	 * @return true, if succeeded, false if not.
	 */
	public boolean saveAs();
	public void quit();
	public void newModel(SMAAModel model);
	public ValueModel getModelSavedModel();
}
