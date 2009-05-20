package fi.smaa.common.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import nl.rug.escher.common.JUnitUtil;

import org.junit.Test;

import fi.smaa.common.Interval;
import fi.smaa.common.InvalidIntervalException;

public class IntervalTest {
	
	@Test
	public void testNullConstructor() {
		Interval i = new Interval();
		assertEquals(0.0, i.getStart().doubleValue(), 0.000001);
		assertEquals(0.0, i.getEnd().doubleValue(), 0.00001);
	}
	
	@Test(expected=InvalidIntervalException.class)
	public void testIllegalConstructor() {
		new Interval(1.0, 0.0);
	}
	
	
	@Test
	public void testParamConstructor() {
		Interval i = new Interval(1.0, 2.0);
		assertEquals(1.0, i.getStart().doubleValue(), 0.000001);
		assertEquals(2.0, i.getEnd().doubleValue(), 0.000001);
	}
	
	@Test
	public void testSetStart() {
		JUnitUtil.testSetter(new Interval(), Interval.PROPERTY_START, 0.0, 1.0);
	}

	@Test
	public void testSetEnd() {
		JUnitUtil.testSetter(new Interval(), Interval.PROPERTY_END, 0.0, 1.0);
	}
	
	@Test
	public void testToString() {
		Interval i = new Interval(0.0, 1.0);
		assertEquals("[0.00 - 1.00]", i.toString());
	}
	
	@Test
	public void testEquals() {
		Interval i = new Interval(0.0, 0.1);
		Interval i2 = new Interval(0.1, 0.1);
		Interval i4 = new Interval(0.0, 0.1);
		
		assertFalse(i.equals(i2));
		assertTrue(i.equals(i4));
		assertFalse(i.equals(null));
		assertFalse(i.equals("yeah"));
	}
	
	@Test
	public void testEnclosingInterval() {
		Interval i = new Interval(-1.0, 0.1);
		Interval i2 = new Interval(0.2, 0.5);
		ArrayList<Interval> intervals = new ArrayList<Interval>();
		intervals.add(i);
		intervals.add(i2);
				
		assertEquals(new Interval(-1.0, 0.5), Interval.enclosingInterval(intervals));
	}
	
	@Test
	public void testGetLength() {
		Interval i = new Interval(-1.0, 2.0);
		assertEquals(3.0, i.getLength(), 0.00001);
	}
	
	@Test
	public void testIncludes() {
		Interval i1 = new Interval(0.0, 1.0);
		Interval i2 = new Interval(-1.0, 0.5);
		Interval i3 = new Interval(0.2, 1.2);
		Interval i4 = new Interval(0.2, 0.8);
		assertFalse(i1.includes(i2));
		assertFalse(i1.includes(i3));
		assertTrue(i1.includes(i4));		
	}
}
