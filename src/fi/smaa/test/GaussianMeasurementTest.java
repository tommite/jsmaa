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

package fi.smaa.test;

import static org.junit.Assert.assertEquals;
import nl.rug.escher.common.JUnitUtil;

import org.junit.Test;

import fi.smaa.GaussianMeasurement;

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
}