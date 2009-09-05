/*
	This file is part of JSMAA.
	(c) Tommi Tervonen, 2009	

    JSMAA is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    JSMAA is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with JSMAA.  If not, see <http://www.gnu.org/licenses/>.
*/

package fi.smaa.jsmaa.model.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;


import org.junit.Test;

import fi.smaa.common.JUnitUtil;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.InvalidIntervalException;

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
	
	@Test
	public void testSample() {
		Interval in = new Interval(0.0, 0.1);
		for (int i=0;i<10;i++) {
			assertTrue(in.sample() <= 0.1);
			assertTrue(in.sample() >= 0.0);
		}
	}
	
	@Test
	public void testIncludesDouble() {
		Interval in = new Interval(0.0, 1.0);
		assertTrue(in.includes(0.5));
		assertFalse(in.includes(-0.5));
		assertFalse(in.includes(1.5));
	}
}
