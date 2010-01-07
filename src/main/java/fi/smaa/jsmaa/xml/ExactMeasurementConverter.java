package fi.smaa.jsmaa.xml;

import com.thoughtworks.xstream.converters.SingleValueConverter;

import fi.smaa.jsmaa.model.ExactMeasurement;

public class ExactMeasurementConverter implements SingleValueConverter {

	@SuppressWarnings("unchecked")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(ExactMeasurement.class);
	}

	@Override
	public Object fromString(String str) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString(Object obj) {
		return ((ExactMeasurement)obj).getValue().toString();
	}

}
