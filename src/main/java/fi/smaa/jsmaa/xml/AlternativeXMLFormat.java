package fi.smaa.jsmaa.xml;

import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import fi.smaa.jsmaa.model.Alternative;

public class AlternativeXMLFormat extends XMLFormat<Alternative> {

	@Override
	public boolean isReferenceable() {
		return true;
	}

	@Override
	public Alternative newInstance(Class<Alternative> cls, InputElement ie) throws XMLStreamException {
		return new Alternative(ie.getAttribute("name", "unnamed"));
	}

	@Override
	public void read(InputElement ie, Alternative alt) throws XMLStreamException {
		alt.setName(ie.getAttribute("name", "unnamed"));
	}

	@Override
	public void write(Alternative alt, OutputElement oe) throws XMLStreamException {
		oe.setAttribute("name", alt.getName());
	}		
}