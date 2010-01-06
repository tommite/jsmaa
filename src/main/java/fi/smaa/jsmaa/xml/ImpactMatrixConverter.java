package fi.smaa.jsmaa.xml;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ImpactMatrix;

public class ImpactMatrixConverter implements Converter {

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		ImpactMatrix m = (ImpactMatrix) source;

		for (Criterion c : m.getCriteria()) {
			for (Alternative a : m.getAlternatives()) {
				writer.startNode("measurement");
				writer.startNode("alternative");
				context.convertAnother(a);
				writer.endNode();
				writer.startNode("criterion");
				context.convertAnother(c);
				writer.endNode();
				writer.startNode(XmlConfigurator.getLabel(m.getMeasurement(c, a).getClass()));
				context.convertAnother(m.getMeasurement(c, a));
				writer.endNode();
				writer.endNode();
			}
		}
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
		return type.equals(ImpactMatrix.class);
	}

}
