package fi.smaa.jsmaa.model;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.drugis.common.ObserverManager;

import com.jgoodies.binding.beans.Observable;

public abstract class AbstractEntity implements Observable, Serializable {
	private static final long serialVersionUID = -3889001536692466540L;
	
	transient private ObserverManager d_om;
	
	protected AbstractEntity() {
		init();
	}
	
	protected void init() {
		d_om = new ConcurrentObserverManager(this);
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();
		init();
	}
	
	protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
		d_om.firePropertyChange(propertyName, oldValue, newValue);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		d_om.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		d_om.removePropertyChangeListener(listener);
	}
}
