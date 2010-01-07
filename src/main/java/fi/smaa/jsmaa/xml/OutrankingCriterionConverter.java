package fi.smaa.jsmaa.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fi.smaa.jsmaa.model.OutrankingCriterion;

public class OutrankingCriterionConverter implements Converter {

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		
		context.convertAnother(this, new CardinalCriterionConverter());
		OutrankingCriterion c = (OutrankingCriterion) source;
		OutrankingCriterion oc = (OutrankingCriterion) c;
		writer.startNode("indifTH");
		writer.startNode(XmlConfigurator.getLabel(oc.getIndifMeasurement().getClass()));
		context.convertAnother(oc.getIndifMeasurement());
		writer.endNode();
		writer.endNode();
		
		writer.startNode("prefTH");
		writer.startNode(XmlConfigurator.getLabel(oc.getPrefMeasurement().getClass()));
		context.convertAnother(oc.getPrefMeasurement());
		writer.endNode();
		writer.endNode();		
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(OutrankingCriterion.class);
	}

}
