package fi.smaa.jsmaa.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import javolution.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.XMLHelper;

public class RelativeLogitNormalMeasurementTest {
	private RelativeLogitNormalMeasurement d_m;
	
	@Before
	public void setUp() {
		d_m = new RelativeLogitNormalMeasurement(
					new ReferenceableGaussianMeasurement(0.25, 0.4),
					new GaussianMeasurement(-0.1, 0.3)
				);
	}
	
	@Test
	public void testGetRange() {
		Interval actual = d_m.getRange();
		Interval expected = (new LogitNormalMeasurement(0.15, 0.5)).getRange();
		assertEquals(expected.getStart(), actual.getStart(), 0.00001);		
		assertEquals(expected.getEnd(), actual.getEnd(), 0.00001);
	}

	@Test
	public void testEquals() {
		RelativeGaussianMeasurementBase m2 = new RelativeLogitNormalMeasurement(
				new ReferenceableGaussianMeasurement(0.3, 0.4),
				new GaussianMeasurement(-0.1, 0.3)
			);
		assertFalse(d_m.equals(m2));
		RelativeGaussianMeasurementBase m3 = new RelativeLogitNormalMeasurement(
				new ReferenceableGaussianMeasurement(0.25, 0.4),
				new GaussianMeasurement(-0.1, 0.35)
			);
		assertFalse(d_m.equals(m3));
		RelativeGaussianMeasurementBase m4 = new RelativeLogitNormalMeasurement(
				new ReferenceableGaussianMeasurement(0.25, 0.4),
				new GaussianMeasurement(-0.1, 0.3)
			);
		assertTrue(d_m.equals(m4));
	}
	
	@Test
	public void testDeepCopy() {
		RelativeGaussianMeasurementBase m2 = d_m.deepCopy();
		assertNotSame(d_m, m2);
		assertNotSame(d_m.getBaseline(), m2.getBaseline());
		assertNotSame(d_m.getRelative(), m2.getRelative());
		assertEquals(d_m, m2);
	}	
	
	@Test
	public void testSample() {
		RelativeGaussianMeasurementBase m = new RelativeLogitNormalMeasurement(
				new ReferenceableGaussianMeasurement(0.0, 0.0),
				new GaussianMeasurement(0.0, 0.0)
			);
		assertEquals(0.5, m.sample(), 0.0000001);
	}
	
	@Test
	public void testConstructorBaselineOnly() {
		RelativeGaussianMeasurementBase expected = new RelativeLogitNormalMeasurement(
				new ReferenceableGaussianMeasurement(2.0, 1.0),
				new GaussianMeasurement(0.0, 0.0)
			);
		RelativeGaussianMeasurementBase actual = new RelativeLogitNormalMeasurement(
				new ReferenceableGaussianMeasurement(2.0, 1.0)
			);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testXML() throws XMLStreamException {
		String xml = XMLHelper.toXml(d_m, RelativeLogitNormalMeasurement.class);
		RelativeGaussianMeasurementBase nm = (RelativeGaussianMeasurementBase) XMLHelper.fromXml(xml);
		assertEquals(d_m, nm);
	}
}
