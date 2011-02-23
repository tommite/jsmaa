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

import org.junit.Test;

public class BaselineGaussianMeasurementTest {
	@Test
	public void testDeepCopy() {
		BaselineGaussianMeasurement m = new BaselineGaussianMeasurement(0.1, 0.2);
		BaselineGaussianMeasurement m2 = (BaselineGaussianMeasurement) m.deepCopy();
		assertFalse(m == m2);
		assertEquals(m.getMean(), m2.getMean(), 0.000000001);
		assertEquals(m.getStDev(), m2.getStDev(), 0.000000001);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testSampleBeforeUpdate() {
		BaselineGaussianMeasurement m = new BaselineGaussianMeasurement(1.0, 0.0);
		m.sample();
	}
	
	@Test
	public void testSample() {
		BaselineGaussianMeasurement m = new BaselineGaussianMeasurement(1.0, 0.0);
		m.update();
		assertEquals(1.0, m.sample(), 0.0001);
	}
	
	@Test
	public void testSample2() {
		BaselineGaussianMeasurement m = new BaselineGaussianMeasurement(0.0, 1.0);
		m.update();
		assertEquals(m.sample(), m.sample(), 0.0001);
	}
	
	@Test
	public void testEquals() {
		BaselineGaussianMeasurement m = new BaselineGaussianMeasurement(1.0, 2.0);
		assertEquals(new BaselineGaussianMeasurement(1.0, 2.0), m);
		assertFalse(m.equals(new GaussianMeasurement(1.0, 2.0)));
		assertFalse(new GaussianMeasurement(1.0, 2.0).equals(m));
		assertFalse(m.equals(new BaselineGaussianMeasurement(1.0, 3.0)));
		assertFalse(m.equals(new BaselineGaussianMeasurement(2.0, 1.0)));
		assertFalse(m.equals("m"));
	}
}