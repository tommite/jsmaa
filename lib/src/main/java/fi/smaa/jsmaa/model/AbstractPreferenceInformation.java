package fi.smaa.jsmaa.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

public abstract class AbstractPreferenceInformation<T extends Measurement> extends AbstractEntity implements PreferenceInformation {

	private static final long serialVersionUID = -7094213141120934341L;
	protected transient MeasurementListener measListener = new MeasurementListener();
	protected List<Criterion> criteria;

	protected AbstractPreferenceInformation(List<Criterion> criteria) {
		this.criteria = criteria;
	}
	
	public abstract T getMeasurement(Criterion c);
	
	public List<Criterion> getCriteria() {
		return criteria;
	}	
	
	private void readObject(ObjectInputStream i) throws IOException, ClassNotFoundException {
		i.defaultReadObject();
		measListener = new MeasurementListener();
	}

	protected void firePreferencesChanged() {
		firePropertyChange(PREFERENCES, null, null);
	}	

	private class MeasurementListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			firePreferencesChanged();
		}		
	}
}