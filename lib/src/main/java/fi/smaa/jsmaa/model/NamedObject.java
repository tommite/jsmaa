package fi.smaa.jsmaa.model;

import com.jgoodies.binding.beans.Observable;

public interface NamedObject extends Observable{

	public final static String PROPERTY_NAME = "name";
	
	public void setName(String name);
	public String getName();
}