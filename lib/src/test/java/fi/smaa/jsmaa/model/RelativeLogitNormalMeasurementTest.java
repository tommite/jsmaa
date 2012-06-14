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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import javolution.xml.stream.XMLStreamException;

import org.drugis.common.stat.Statistics;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.RandomUtil;
import fi.smaa.common.XMLHelper;

public class RelativeLogitNormalMeasurementTest {
	private RelativeLogitNormalMeasurement d_m;
	private RandomUtil random;
	
	@Before
	public void setUp() {
		random = RandomUtil.createWithFixedSeed();
		d_m = new RelativeLogitNormalMeasurement(
					new BaselineGaussianMeasurement(0.25, 0.4),
					new GaussianMeasurement(-0.1, 0.3)
				);
	}
	
	@Test
	public void testGetRange() {
		Interval actual = d_m.getRange();
		Interval expected = (new LogitNormalMeasurement(0.15, 0.5)).getRange();
		assertEquals(expected.getStart(), actual.getStart(), 0.00001);		
		assertEquals(expected.getEnd(), actual.getEnd(), 0.00001);
	}

	@Test
	public void testEquals() {
		RelativeGaussianMeasurementBase m2 = new RelativeLogitNormalMeasurement(
				new BaselineGaussianMeasurement(0.3, 0.4),
				new GaussianMeasurement(-0.1, 0.3)
			);
		assertFalse(d_m.equals(m2));
		RelativeGaussianMeasurementBase m3 = new RelativeLogitNormalMeasurement(
				new BaselineGaussianMeasurement(0.25, 0.4),
				new GaussianMeasurement(-0.1, 0.35)
			);
		assertFalse(d_m.equals(m3));
		RelativeGaussianMeasurementBase m4 = new RelativeLogitNormalMeasurement(
				new BaselineGaussianMeasurement(0.25, 0.4),
				new GaussianMeasurement(-0.1, 0.3)
			);
		assertTrue(d_m.equals(m4));
	}
	
	@Test
	public void testDeepCopy() {
		RelativeGaussianMeasurementBase m2 = d_m.deepCopy();
		assertNotSame(d_m, m2);
		assertNotSame(d_m.getBaseline(), m2.getBaseline());
		assertNotSame(d_m.getRelative(), m2.getRelative());
		assertEquals(d_m, m2);
	}	
	
	@Test
	public void testSample() {
		BaselineGaussianMeasurement baseline = new BaselineGaussianMeasurement(0.0, 1.0);
		baseline.update(random);
		RelativeGaussianMeasurementBase m = new RelativeLogitNormalMeasurement(
				baseline,
				new GaussianMeasurement(0.0, 0.0)
			);
		assertEquals(Statistics.ilogit(baseline.sample(random)), m.sample(random), 0.0000001);
		RelativeGaussianMeasurementBase m2 = new RelativeLogitNormalMeasurement(
				baseline,
				new GaussianMeasurement(1.0, 0.0)
			);
		assertEquals(Statistics.ilogit(baseline.sample(random) + 1.0), m2.sample(random), 0.0000001);
	}
	
	@Test
	public void testConstructorBaselineOnly() {
		RelativeGaussianMeasurementBase expected = new RelativeLogitNormalMeasurement(
				new BaselineGaussianMeasurement(2.0, 1.0),
				new GaussianMeasurement(0.0, 0.0)
			);
		RelativeGaussianMeasurementBase actual = new RelativeLogitNormalMeasurement(
				new BaselineGaussianMeasurement(2.0, 1.0)
			);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testXML() throws XMLStreamException {
		String xml = XMLHelper.toXml(d_m, RelativeLogitNormalMeasurement.class);
		RelativeGaussianMeasurementBase nm = (RelativeGaussianMeasurementBase) XMLHelper.fromXml(xml);
		assertEquals(d_m, nm);
	}
}
