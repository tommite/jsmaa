package fi.smaa.jsmaa.model.xml;

import static org.junit.Assert.*;
import javolution.xml.stream.XMLStreamException;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.ExactMeasurement;

public class ExactMeasurementXMLFormatTest {
	@Test
	public void testMarshallExactMeasurement() throws XMLStreamException {
		ExactMeasurement meas = new ExactMeasurement(2.0);
		ExactMeasurement nMeas = (ExactMeasurement) XMLHelper.fromXml(XMLHelper.toXml(meas, ExactMeasurement.class));
		assertEquals(meas, nMeas);
	}
}
