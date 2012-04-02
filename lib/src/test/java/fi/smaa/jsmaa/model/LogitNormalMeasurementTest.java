/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

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
package fi.smaa.jsmaa.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.RandomUtil;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.LogitNormalMeasurement;

public class LogitNormalMeasurementTest {
	private LogitNormalMeasurement d_m;
	private RandomUtil random;
	
	@Before
	public void setUp() {
		d_m = new LogitNormalMeasurement(0.1, 0.2);
		random = new RandomUtil();

	}
	
	@Test
	public void testGetRange() {
		Interval rng = d_m.getRange();
		assertEquals(0.0, rng.getStart(), 0.00001);		
		assertEquals(1.0, rng.getEnd(), 0.00001);
	}

	@Test
	public void testEquals() {
		GaussianMeasurement m2 = new GaussianMeasurement(0.1, 0.2);
		assertFalse(d_m.equals(m2));
		assertTrue(d_m.equals(new LogitNormalMeasurement(0.1, 0.2)));
	}
	
	@Test
	public void testDeepCopy() {
		LogitNormalMeasurement m2 = d_m.deepCopy();
		assertNotSame(d_m, m2);
		assertEquals(d_m.getMean(), m2.getMean(), 0.000000001);
		assertEquals(d_m.getStDev(), m2.getStDev(), 0.000000001);
	}	
	
	@Test
	public void testSample() {
		LogitNormalMeasurement m = new LogitNormalMeasurement(0.0, 0.0);
		assertEquals(0.5, m.sample(random), 0.0000001);
	}
}
