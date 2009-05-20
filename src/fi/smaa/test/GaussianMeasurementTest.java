package fi.smaa.test;

import static org.junit.Assert.assertEquals;
import nl.rug.escher.common.JUnitUtil;

import org.junit.Test;

import fi.smaa.GaussianMeasurement;

public class GaussianMeasurementTest {
	
	@Test
	public void testNullConstructor() {
		GaussianMeasurement meas = new GaussianMeasurement();				
		assertEquals(0.0, meas.getMean().doubleValue(), 0.0001);
		assertEquals(0.0, meas.getStDev().doubleValue(), 0.0001);
	}
	
	@Test
	public void testParamConstructor() {
		GaussianMeasurement meas = new GaussianMeasurement(1.0, 2.0);				
		assertEquals(1.0, meas.getMean(), 0.0001);
		assertEquals(2.0, meas.getStDev(), 0.0001);		
	}
	
	@Test
	public void testSetMean() {
		JUnitUtil.testSetter(new GaussianMeasurement(), GaussianMeasurement.PROPERTY_MEAN, 0.0, 1.0);
	}
	
	@Test
	public void testSetStdev() {
		JUnitUtil.testSetter(new GaussianMeasurement(), GaussianMeasurement.PROPERTY_STDEV, 0.0, 1.0);
	}
}