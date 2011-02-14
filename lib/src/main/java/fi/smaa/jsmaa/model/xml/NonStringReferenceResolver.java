package fi.smaa.jsmaa.model.xml;

import javolution.xml.XMLFormat;
import javolution.xml.XMLReferenceResolver;
import javolution.xml.stream.XMLStreamException;

public class NonStringReferenceResolver extends XMLReferenceResolver {
	@Override
	public void createReference(Object obj, XMLFormat.InputElement xml) throws XMLStreamException {
		if (obj instanceof String) {
			return; 
		}
		super.createReference(obj, xml);
	}
}
