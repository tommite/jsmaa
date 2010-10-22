package fi.smaa.jsmaa.gui.jfreechart.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.jfreechart.LogNormalDistributionFunction;
import fi.smaa.jsmaa.model.LogNormalMeasurement;

public class LogNormalDistributionFunctionTest {

	private LogNormalMeasurement meas;
	private LogNormalDistributionFunction func;

	@Before
	public void setUp() {
		// mean 1, std 0.2
		meas = new LogNormalMeasurement(0.0, 0.2);
		func = new LogNormalDistributionFunction(meas);
	}
			
	@Test
	public void testGetValue() {
		assertEquals(0.0, func.getValue(0.0), 0.00001);
		assertEquals(1.994711, func.getValue(1.0), 0.00001);
	}
}
