package fi.smaa.jsmaa.model.xml;

import static org.junit.Assert.assertEquals;
import javolution.xml.stream.XMLStreamException;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.Alternative;

public class AlternativeXMLFormatTest {

	@Test
	public void testMarshalAlternative() throws XMLStreamException {
		Alternative a = new Alternative("a");
		String xml = XMLHelper.toXml(a, Alternative.class);
		Alternative na = (Alternative) XMLHelper.fromXml(xml);
		assertEquals("a", na.getName());
	}
}
