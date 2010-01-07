package fi.smaa.jsmaa.xml;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import fi.smaa.jsmaa.model.Alternative;

public class AlternativeConverter extends AbstractSingleValueConverter {

	@SuppressWarnings("unchecked")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(Alternative.class);
	}

	@Override
	public Object fromString(String str) {
		return new Alternative(str);
	}

}
