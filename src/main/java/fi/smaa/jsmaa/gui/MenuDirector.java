package fi.smaa.jsmaa.gui;

import fi.smaa.jsmaa.ModelFileManager;

public interface MenuDirector {
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
	public void newModel(ModelType type);
	public ModelFileManager getFileManager();
	
	public enum ModelType {
		SMAA2,
		SMAATRI,
		SMAACEA
	};
}
