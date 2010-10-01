package fi.smaa.jsmaa.model;

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.drugis.common.ObserverManager;

import com.jgoodies.binding.beans.Observable;

public class ConcurrentObserverManager extends ObserverManager {

	public ConcurrentObserverManager(Observable source) {
		super(source);
	}
	
	@Override
	protected Collection<PropertyChangeListener> createCollection() {
		return new ConcurrentLinkedQueue<PropertyChangeListener>();
	}

}
