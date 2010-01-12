package fi.smaa.jsmaa;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;

import com.jgoodies.binding.beans.Model;

import fi.smaa.jsmaa.model.ModelChangeEvent;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.SMAAModelListener;

@SuppressWarnings("serial")
public class ModelFileManager extends Model {
	
	public static final String PROPERTY_MODEL = "model";
	public static final String PROPERTY_MODELSAVED = "saved";
	public static final String PROPERTY_TITLE = "title";

	private File currentModelFile;
	private boolean modelSaved = true;
	private SMAAModel model;
		
	public boolean getSaved() {
		return modelSaved;
	}
	
	public String getTitle() {
		String file = "Unsaved model";
		
		if (currentModelFile != null) {
			try {
				file = currentModelFile.getCanonicalPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return "JSMAA v" + AppInfo.getAppVersion() + " - " + file + (modelSaved ? "" : "*");
	}

	public SMAAModel getModel() {
		return model;
	}
	
	public void setModel(SMAAModel model) {
		SMAAModel oldVal = this.model;
		this.model = model;
		this.currentModelFile = null;
		model.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				setSaved(false);
			}
		});
		model.addModelListener(new SMAAModelListener() {
			@Override
			public void modelChanged(ModelChangeEvent type) {
				setSaved(false);
			}			
		});
		setSaved(true);
		firePropertyChange(PROPERTY_MODEL, oldVal, this.model);
	}
	
	public void setModelFile(File f) {
		this.currentModelFile = f;
		setSaved(true);
	}
	
	public File getModelFile() {
		return currentModelFile;
	}
	
	public void setSaved(boolean saved) {
		boolean oldVal = this.modelSaved;
		this.modelSaved = saved;
		firePropertyChange(PROPERTY_MODELSAVED, oldVal, this.modelSaved);
		firePropertyChange(PROPERTY_TITLE, null, getTitle());
	}	
}
