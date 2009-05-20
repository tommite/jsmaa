package fi.smaa;

import com.jgoodies.binding.beans.Model;

public class Alternative extends Model {
	private String name;
	
	public final static String PROPERTY_NAME = "name";

	public Alternative() {
	}
	
	public Alternative(String name) {
		this.name = name;
	}
		
	public String getName() {
		return name;
	}
	
	public void setName(String _name) {
		String oldVal = this.name;
		this.name = _name;
		firePropertyChange(PROPERTY_NAME, oldVal, _name);
	}	
			
	@Override
	public String toString() {
		return name;
	}

}