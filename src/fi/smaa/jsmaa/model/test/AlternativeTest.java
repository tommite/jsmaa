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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import nl.rug.escher.common.JUnitUtil;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.common.Interval;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.Measurement;

public class AlternativeTest {
	
	private Alternative a;
	private Criterion c;
	
	@Before
	public void setUp() {
		c = new CardinalCriterion("unif");		
		a = new Alternative("alt");		
	}
	
	@Test
	public void testSetName() {
		JUnitUtil.testSetter(a, Alternative.PROPERTY_NAME, "alt", "hello");
	}
	
	@Test
	public void testConstructor()  {
		assertEquals("alt", a.getName());
	}
		
	@Test
	public void testToString() {
		assertEquals("alt", a.toString());
	}
	
	@Test
	public void testEquals() {
		Alternative a1 = new Alternative("a1");
		Alternative a2 = new Alternative("a1");
		
		assertTrue(a1.equals(a2));
		Alternative a3 = new Alternative("a3");
		assertFalse(a1.equals(a3));
	}
	
	@Test
	public void testDeepCopy() {
		a.setMeasurements(Collections.singletonMap(c, (Measurement) new Interval(0.0, 1.0)));
		Alternative a2 = a.deepCopy();
		assertEquals(a.getName(), a2.getName());
		assertEquals(a.getMeasurements(), a2.getMeasurements());
	}
	
	@Test
	public void testSetMeasurements() {
		Map<Criterion, Measurement> meas = new HashMap<Criterion, Measurement>();
		meas.put(c, new Interval(0.0, 1.0));
		JUnitUtil.testSetter(a, Alternative.PROPERTY_MEASUREMENTS, 
				new HashMap<Criterion, Measurement>(), meas);
	}

}
