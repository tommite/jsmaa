package fi.smaa.jsmaa.model.xml;

import static org.junit.Assert.assertEquals;
import javolution.xml.stream.XMLStreamException;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.LogNormalMeasurement;

public class LogNormalMeasurementXMLFormatTest {
	@Test
	public void testMarshalLogNormalMeasurement() throws XMLStreamException {
		LogNormalMeasurement m = new LogNormalMeasurement(0.1, 0.2);
		LogNormalMeasurement nm = (LogNormalMeasurement) XMLHelper.fromXml(XMLHelper.toXml(m, LogNormalMeasurement.class));
		assertEquals(m, nm);
	}
}
