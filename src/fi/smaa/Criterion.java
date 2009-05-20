package fi.smaa;

import java.util.ArrayList;
import java.util.List;

import com.jgoodies.binding.beans.Model;

public abstract class Criterion extends Model {
	
	public final static String PROPERTY_NAME = "name";	
	public final static String PROPERTY_TYPELABEL = "typeLabel";
	public final static String PROPERTY_ALTERNATIVES = "alternatives";
	
	private String name;
	protected List<Alternative> alternatives = new ArrayList<Alternative>();

	protected Criterion() {		
	}
	
	protected Criterion(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public abstract void sample(double[] target);
	
	public void setName(String name) {
		Object oldVal = this.name;
		this.name = name;
		firePropertyChange(PROPERTY_NAME, oldVal, name);		
	}

	public String getName() {
		return name;
	}

	public List<Alternative> getAlternatives() {
		return alternatives;
	}
	
	
	/**
	 * 
	 * @param alternatives
	 * @throws NullPointerException if alternatives == null
	 */
	public void setAlternatives(List<Alternative> alternatives) throws NullPointerException {
		if (alternatives == null) {
			throw new NullPointerException();
		}
		Object oldVal = this.alternatives;
		this.alternatives = alternatives;
		firePropertyChange(PROPERTY_ALTERNATIVES, oldVal, this.alternatives);
		updateMeasurements();
	}
	
	protected abstract void updateMeasurements();
	
	public abstract String getTypeLabel();
	
	public abstract String measurementsToString();
}