package fi.smaa.jsmaa.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import fi.smaa.common.JUnitUtil;

public class BetaMeasurementTest {

	@Test
	public void testNullConstructor() {
		BetaMeasurement meas = new BetaMeasurement();				
		assertEquals(2.0, meas.getAlpha().doubleValue(), 0.0001);
		assertEquals(2.0, meas.getBeta().doubleValue(), 0.0001);
		assertEquals(0.0, meas.getMin().doubleValue(), 0.0001);
		assertEquals(1.0, meas.getMax().doubleValue(), 0.0001);		
	}
	
	@Test
	public void testParamConstructor() {
		BetaMeasurement meas = new BetaMeasurement(1.0, 2.0, 3.0, 4.0);				
		assertEquals(1.0, meas.getAlpha(), 0.0001);
		assertEquals(2.0, meas.getBeta(), 0.0001);
		assertEquals(3.0, meas.getMin(), 0.0001);
		assertEquals(4.0, meas.getMax(), 0.0001);		
	}
	
	@Test
	public void testSetAlpha() {
		JUnitUtil.testSetter(new BetaMeasurement(), BetaMeasurement.PROPERTY_ALPHA, 2.0, 3.0);
	}
	
	@Test
	public void testSetBeta() {
		JUnitUtil.testSetter(new BetaMeasurement(), BetaMeasurement.PROPERTY_BETA, 2.0, 3.0);
	}
	
	@Test
	public void testSetMin() {
		JUnitUtil.testSetter(new BetaMeasurement(), BetaMeasurement.PROPERTY_MIN, 0.0, 0.5);
	}
	
	@Test
	public void testSetMax() {
		JUnitUtil.testSetter(new BetaMeasurement(), BetaMeasurement.PROPERTY_MAX, 1.0, 2.0);
	}		
	
	
	@Test
	public void testGetRange() {
		BetaMeasurement meas = new BetaMeasurement(52.0, 46.0, 1.0, 3.0);
		Interval range = meas.getRange();
		double targetMin = (0.4319303 + 1.0) * (3.0 - 1.0);
		double targetMax = (0.6281102 + 1.0) * (3.0 - 1.0);
		assertEquals(targetMin, range.getStart(), 0.01);
		assertEquals(targetMax, range.getEnd(), 0.01);
	}
	
	@Test
	public void testDeepCopy() {
		BetaMeasurement m = new BetaMeasurement(0.1, 0.2, 0.3, 0.4);
		BetaMeasurement m2 = m.deepCopy();
		assertFalse(m == m2);
		assertEquals(m.getAlpha(), m2.getAlpha(), 0.000000001);
		assertEquals(m.getBeta(), m2.getBeta(), 0.000000001);
		assertEquals(m.getMin(), m2.getMin(), 0.000000001);
		assertEquals(m.getMax(), m2.getMax(), 0.000000001);
	}
	
	@Test
	public void testSample() {
		BetaMeasurement m = new BetaMeasurement(1.0, 1.0, 3.0, 3.01);
		assertEquals(3.005, m.sample(), 0.005);
	}	
	
	@Test
	public void testEquals() {
		BetaMeasurement m = new BetaMeasurement(1.0, 2.0, 3.0, 4.0);
		assertEquals(new BetaMeasurement(1.0, 2.0, 3.0, 4.0), m);
		assertFalse(m.equals(new BetaMeasurement(1.0, 3.0, 3.0, 4.0)));
		assertFalse(m.equals(new BetaMeasurement(2.0, 2.0, 3.0, 4.0)));
		assertFalse(m.equals(new BetaMeasurement(1.0, 2.0, 3.5, 4.0)));
		assertFalse(m.equals(new BetaMeasurement(1.0, 2.0, 3.0, 5.0)));
		assertFalse(m.equals("m"));
	}
}
