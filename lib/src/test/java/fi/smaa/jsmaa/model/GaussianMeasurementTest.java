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

import org.drugis.common.JUnitUtil;
import org.junit.Test;

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
	
	@Test
	public void testGetRange() {
		GaussianMeasurement meas = new GaussianMeasurement(0.0, 1.0);
		assertEquals(new Interval(-1.96, 1.96), meas.getRange());
	}
	
	@Test
	public void testDeepCopy() {
		GaussianMeasurement m = new GaussianMeasurement(0.1, 0.2);
		GaussianMeasurement m2 = (GaussianMeasurement) m.deepCopy();
		assertFalse(m == m2);
		assertEquals(m.getMean(), m2.getMean(), 0.000000001);
		assertEquals(m.getStDev(), m2.getStDev(), 0.000000001);
	}
	
	@Test
	public void testSample() {
		GaussianMeasurement m = new GaussianMeasurement(1.0, 0.0);
		assertEquals(1.0, m.sample(), 0.0001);
	}	
	
	@Test
	public void testEquals() {
		GaussianMeasurement m = new GaussianMeasurement(1.0, 2.0);
		assertEquals(new GaussianMeasurement(1.0, 2.0), m);
		assertFalse(m.equals(new GaussianMeasurement(1.0, 3.0)));
		assertFalse(m.equals(new GaussianMeasurement(2.0, 1.0)));
		assertFalse(m.equals("m"));
	}
}