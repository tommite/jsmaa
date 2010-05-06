package fi.smaa.jsmaa.model.xml;

import static org.junit.Assert.assertEquals;
import javolution.xml.stream.XMLStreamException;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class CriterionAlternativeMeasurementXMLFormatTest {

	@Test
	public void testMarshallCriterionAlternativeMeasurement() throws XMLStreamException {
		Alternative a = new Alternative("a");
		Criterion c = new ScaleCriterion("c");
		ExactMeasurement me = new ExactMeasurement(1.0);
		CriterionAlternativeMeasurement m = new CriterionAlternativeMeasurement(a, c, me);
		
		CriterionAlternativeMeasurement nm = XMLHelper.fromXml(XMLHelper.toXml(m, CriterionAlternativeMeasurement.class));
		
		assertEquals(a.getName(), nm.getAlternative().getName());
		assertEquals(me, nm.getMeasurement());
		assertEquals(c.getName(), nm.getCriterion().getName());
	}
}
