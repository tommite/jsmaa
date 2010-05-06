package fi.smaa.jsmaa.model.xml;

import static org.junit.Assert.*;
import javolution.xml.stream.XMLStreamException;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.ImpactMatrix;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class ImpactMatrixXMLFormatTest {

	@Test
	public void testMarshalImpactMatrix() throws XMLStreamException {
		Alternative a = new Alternative("a");
		ScaleCriterion c = new ScaleCriterion("c");
		ExactMeasurement m = new ExactMeasurement(1.0);
		
		ImpactMatrix mat = new ImpactMatrix();
		mat.addAlternative(a);
		mat.addCriterion(c);
		mat.setMeasurement(c, a, m);
		
		ImpactMatrix nm = XMLHelper.fromXml(XMLHelper.toXml(mat, ImpactMatrix.class));
		
		assertEquals(1, nm.getCriteria().size());
		assertEquals(1, nm.getAlternatives().size());
		assertEquals(a.getName(), nm.getAlternatives().get(0).getName());
		assertEquals(c.getName(), nm.getCriteria().get(0).getName());
		assertEquals(m, nm.getMeasurement(nm.getCriteria().get(0), nm.getAlternatives().get(0)));
	}
}
