package fi.smaa.jsmaa.model.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import javolution.xml.stream.XMLStreamException;

import org.junit.Test;

import fi.smaa.common.XMLHelper;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.ImpactMatrix;
import fi.smaa.jsmaa.model.ReferenceableGaussianMeasurement;
import fi.smaa.jsmaa.model.RelativeLogitNormalMeasurement;
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
	
	@Test
	public void testMarshalImpactMatrixWithRelativeMeasurements() throws XMLStreamException {
		Alternative a = new Alternative("a");
		Alternative b = new Alternative("b");
		ScaleCriterion c = new ScaleCriterion("c");
			
		ImpactMatrix mat = new ImpactMatrix();
		mat.addAlternative(a);
		mat.addAlternative(b);
		mat.addCriterion(c);
		ReferenceableGaussianMeasurement baseline = mat.getBaseline(c);
		baseline.setMean(1.0);
		baseline.setStDev(0.3);
		RelativeLogitNormalMeasurement ma = new RelativeLogitNormalMeasurement(baseline);
		ma.getRelative().setMean(0.5);
		ma.getRelative().setStDev(0.4);
		mat.setMeasurement(c, a, ma);
		RelativeLogitNormalMeasurement mb = new RelativeLogitNormalMeasurement(baseline);
		mb.getRelative().setMean(-0.1);
		mb.getRelative().setStDev(0.5);
		mat.setMeasurement(c, b, mb);
		
		String xml = XMLHelper.toXml(mat, ImpactMatrix.class);
		System.out.println(xml);
		ImpactMatrix nm = XMLHelper.fromXml(xml);
		
		assertEquals(1, nm.getCriteria().size());
		assertEquals(2, nm.getAlternatives().size());
		assertEquals(a.getName(), nm.getAlternatives().get(0).getName());
		assertEquals(b.getName(), nm.getAlternatives().get(1).getName());
		assertEquals(c.getName(), nm.getCriteria().get(0).getName());
		assertEquals(baseline, nm.getBaseline(nm.getCriteria().get(0)));
		assertEquals(ma, nm.getMeasurement(nm.getCriteria().get(0), nm.getAlternatives().get(0)));
		assertEquals(mb, nm.getMeasurement(nm.getCriteria().get(0), nm.getAlternatives().get(1)));
		assertSame(nm.getBaseline(nm.getCriteria().get(0)), 
				((RelativeLogitNormalMeasurement)nm.getMeasurement(nm.getCriteria().get(0), nm.getAlternatives().get(0))).getBaseline());
		assertSame(nm.getBaseline(nm.getCriteria().get(0)), 
				((RelativeLogitNormalMeasurement)nm.getMeasurement(nm.getCriteria().get(0), nm.getAlternatives().get(1))).getBaseline());
	}
}
