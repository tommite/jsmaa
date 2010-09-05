package fi.smaa.jsmaa.model.xml;

import static org.junit.Assert.assertEquals;
import javolution.xml.stream.XMLStreamException;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.BetaMeasurement;

public class BetaMeasurementXMLFormatTest {

	@Test
	public void testMarshallBetaMeasurement() throws XMLStreamException {
		BetaMeasurement m = new BetaMeasurement(1.0, 2.0, 3.0, 4.0);
		BetaMeasurement nm = (BetaMeasurement) XMLHelper.fromXml(XMLHelper.toXml(m, BetaMeasurement.class));
		assertEquals(m, nm);
	}
}
