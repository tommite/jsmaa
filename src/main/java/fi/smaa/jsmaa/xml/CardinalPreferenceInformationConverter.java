package fi.smaa.jsmaa.xml;

import fi.smaa.jsmaa.model.CardinalPreferenceInformation;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.Measurement;
import fi.smaa.jsmaa.model.PreferenceInformation;

public class CardinalPreferenceInformationConverter extends AbstractPreferenceInformationConverter {

	@Override
	protected Measurement getPreferenceForCriterion(PreferenceInformation p, Criterion c) {
		CardinalPreferenceInformation cp = (CardinalPreferenceInformation) p;
		
		return cp.getMeasurement(c);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(CardinalPreferenceInformation.class);
	}	

}
