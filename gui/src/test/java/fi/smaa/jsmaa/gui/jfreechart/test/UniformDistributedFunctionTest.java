package fi.smaa.jsmaa.gui.jfreechart.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.jfreechart.UniformDistributedFunction;
import fi.smaa.jsmaa.model.Interval;

public class UniformDistributedFunctionTest {
	
	private Interval meas;
	private UniformDistributedFunction func;

	@Before
	public void setUp() {
		meas = new Interval(0.0, 1.0);
		func = new UniformDistributedFunction(meas);
	}
			
	@Test
	public void testGetValue() {
		assertEquals(0.0, func.getValue(-1.0), 0.00001);
		assertEquals(0.0, func.getValue(1.1), 0.00001);
		assertEquals(1.0/(1.0 - 0.0), func.getValue(0.2), 0.00001);
	}
}
