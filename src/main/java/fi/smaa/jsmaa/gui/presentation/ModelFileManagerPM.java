package fi.smaa.jsmaa.gui.presentation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.jgoodies.binding.PresentationModel;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

import fi.smaa.jsmaa.ModelFileManager;

@SuppressWarnings("serial")
public class ModelFileManagerPM extends PresentationModel<ModelFileManager> {

	private ValueHolder unsavedModel;

	public ModelFileManagerPM(ModelFileManager bean) {
		super(bean);
		bean.addPropertyChangeListener(ModelFileManager.PROPERTY_MODELSAVED, new ModelSavedListener());
		unsavedModel = new ValueHolder(!bean.getSaved());
	}

	public ValueModel getUnsavedModel() {
		return unsavedModel;
	}
	
	private class ModelSavedListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent ev) {
			unsavedModel.setValue(!((Boolean)ev.getNewValue()));
		}		
	}
}
