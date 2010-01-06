package fi.smaa.jsmaa.xml;

import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.Measurement;
import fi.smaa.jsmaa.model.OrdinalPreferenceInformation;
import fi.smaa.jsmaa.model.PreferenceInformation;

public class OrdinalPreferenceInformationConverter extends AbstractPreferenceInformationConverter {

	@Override
	protected Measurement getPreferenceForCriterion(PreferenceInformation p, Criterion c) {
		OrdinalPreferenceInformation op = (OrdinalPreferenceInformation) p;
		
		//TODO
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(OrdinalPreferenceInformation.class);
	}
}
