package fi.smaa.jsmaa.model.xml;

import static org.junit.Assert.assertEquals;
import javolution.xml.stream.XMLStreamException;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.Rank;

public class CriterionMeasurementPairXMLFormatTest {

	@Test
	public void testMarshallCriterionMeasurementPair() throws XMLStreamException {
		Rank m = new Rank(2);
		OrdinalCriterion c = new OrdinalCriterion("c");
		CriterionMeasurementPair p = new CriterionMeasurementPair(c, m);
		
		CriterionMeasurementPair np = (CriterionMeasurementPair) XMLHelper.fromXml(XMLHelper.toXml(p, CriterionMeasurementPair.class));
		
		assertEquals(c.getName(), np.getCriterion().getName());
		assertEquals(m, np.getMeasurement());
	}
}
