package fi.smaa.jsmaa.model.xml;

import static org.junit.Assert.assertEquals;
import javolution.xml.stream.XMLStreamException;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.GaussianMeasurement;

public class GaussianMeasurementXMLFormatTest {

	@Test
	public void testMarshallGaussianMeasurement() throws XMLStreamException {
		GaussianMeasurement m = new GaussianMeasurement(1.0, 2.0);
		GaussianMeasurement nm = (GaussianMeasurement) XMLHelper.fromXml(XMLHelper.toXml(m, GaussianMeasurement.class));
		assertEquals(m, nm);
	}
}
