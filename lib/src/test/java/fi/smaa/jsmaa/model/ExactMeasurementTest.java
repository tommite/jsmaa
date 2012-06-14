/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper 2012.

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
import static org.junit.Assert.assertTrue;

import org.drugis.common.JUnitUtil;
import org.junit.Test;

public class ExactMeasurementTest {

	@Test
	public void testConstructor() {
		ExactMeasurement m = new ExactMeasurement(2.0);
		assertEquals(2.0, m.getValue(), 0.0000001);
	}
	
	@Test
	public void testSetValue() {
		JUnitUtil.testSetter(new ExactMeasurement(0.0), ExactMeasurement.PROPERTY_VALUE,
				new Double(0.0), new Double(2.0));
	}
	
	@Test
	public void testEquals() {
		assertFalse(new ExactMeasurement(0.0).equals(new ExactMeasurement(1.0)));
		assertFalse(new ExactMeasurement(0.0).equals(null));
		assertTrue(new ExactMeasurement(0.0).equals(new ExactMeasurement(0.0)));
	}
}
