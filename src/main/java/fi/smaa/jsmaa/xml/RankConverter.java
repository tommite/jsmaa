package fi.smaa.jsmaa.xml;

import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import fi.smaa.jsmaa.model.Rank;

public class RankConverter extends AbstractSingleValueConverter {

	@SuppressWarnings("unchecked")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(Rank.class);
	}

	@Override
	public Object fromString(String str) {
		return new Rank(Integer.decode(str));
	}

}
