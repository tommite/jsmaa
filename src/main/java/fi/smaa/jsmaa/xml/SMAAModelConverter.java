package fi.smaa.jsmaa.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.SMAAModel;

public class SMAAModelConverter implements Converter {

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		SMAAModel model = (SMAAModel) source;
		writer.addAttribute("version", "1");
		writer.addAttribute("name", model.getName());

		for (Alternative a : model.getAlternatives()) {
			XmlConfigurator.writeNode(a, writer, context);
		}
		for (Criterion c : model.getCriteria()) {
			XmlConfigurator.writeNode(c, writer, context);
		}
		XmlConfigurator.writeNode(model.getImpactMatrix(), writer, context);
		XmlConfigurator.writeNode(model.getPreferenceInformation(), writer, context);
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
		return type.equals(SMAAModel.class);
	}

}
