package fi.smaa.jsmaa.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fi.smaa.jsmaa.model.CardinalPreferenceInformation;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.Measurement;
import fi.smaa.jsmaa.model.PreferenceInformation;

public abstract class AbstractPreferenceInformationConverter implements Converter {

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		CardinalPreferenceInformation p = (CardinalPreferenceInformation) source;
		
		for (Criterion c : p.getCriteria()) {
			writer.startNode("preference");
			writer.startNode("criterion");
			context.convertAnother(c);
			writer.endNode();
			Measurement prefMeas = getPreferenceForCriterion(p, c);
			writer.startNode(XmlConfigurator.getLabel(prefMeas.getClass()));
			context.convertAnother(prefMeas);
			writer.endNode();
			writer.endNode();
		}
	}

	protected abstract Measurement getPreferenceForCriterion(PreferenceInformation p, Criterion c);

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		// TODO Auto-generated method stub
		return null;
	}	
}
