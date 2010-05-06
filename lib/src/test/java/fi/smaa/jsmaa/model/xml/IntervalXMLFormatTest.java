package fi.smaa.jsmaa.model.xml;

import static org.junit.Assert.assertEquals;
import javolution.xml.stream.XMLStreamException;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.Interval;

public class IntervalXMLFormatTest {
	@Test
	public void testMarshallInterval() throws XMLStreamException {
		Interval i = new Interval(1.0, 2.0);
		Interval ni = (Interval) XMLHelper.fromXml(XMLHelper.toXml(i, Interval.class));
		assertEquals(i, ni);
	}
}
