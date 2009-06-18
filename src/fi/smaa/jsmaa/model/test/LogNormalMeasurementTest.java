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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.common.Interval;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.LogNormalMeasurement;

public class LogNormalMeasurementTest {
	
	private LogNormalMeasurement m;
	
	@Before
	public void setUp() {
		m = new LogNormalMeasurement(0.1, 0.2);
	}
	
	@Test
	public void testGetRange() {
		Interval rng = m.getRange();
		assertEquals(0.74677, rng.getStart(), 0.0001);		
		assertEquals(1.63558, rng.getEnd(), 0.0001);
	}

	@Test
	public void testEquals() {
		GaussianMeasurement m2 = new GaussianMeasurement(0.1, 0.2);
		assertFalse(m.equals(m2));
	}
	
	@Test
	public void testDeepCopy() {
		LogNormalMeasurement m = new LogNormalMeasurement(0.1, 0.2);
		LogNormalMeasurement m2 = (LogNormalMeasurement) m.deepCopy();
		assertFalse(m == m2);
		assertEquals(m.getMean(), m2.getMean(), 0.000000001);
		assertEquals(m.getStDev(), m2.getStDev(), 0.000000001);
	}	
}
