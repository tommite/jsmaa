/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013.

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
		firePropertyChange(PROPERTY_MODEL, oldVal, this.model);
		setSaved(true);		
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
