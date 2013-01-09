/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013.

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

import java.util.ArrayList;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.RandomUtil;

public class DiscreteMeasurementTest {

	private RandomUtil random;

	@Before
	public void setUp() {
		random = RandomUtil.createWithFixedSeed();
	}

	public DiscreteMeasurement twoPointDiscreteMeasurement() {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(4, 0.5));
		points.add(new Point2D(5, 0.5));
		DiscreteMeasurement dm = null;
		try {
			dm = new DiscreteMeasurement(points);
		} catch (PointOutsideIntervalException e) {
			Assert.fail(e.getMessage());
		}
		return dm;
	}

	public DiscreteMeasurement onePointDiscreteMeasurement() {
		ArrayList<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(4, 0.5));
		DiscreteMeasurement dm = null;
		try {
			dm = new DiscreteMeasurement(points);
		} catch (PointOutsideIntervalException e) {
			Assert.fail(e.getMessage());
		}
		return dm;
	}

	@Test
	public void testDiscreteMeasurementListOfPoint2D() {
		DiscreteMeasurement dm = twoPointDiscreteMeasurement();
		Assert.assertEquals(new Point2D(4, 0.5), dm.get(0));
		Assert.assertEquals(new Point2D(5, 0.5), dm.get(1));
	}

	@Test
	public void testDiscreteMeasurementListOfPoint2DBadList() {
		boolean exceptionThrown = false;

		ArrayList<Point2D> points = new ArrayList<Point2D>();
		points.add(new Point2D(4, 0.5));
		points.add(new Point2D(5, 0.6));
		try {
			new DiscreteMeasurement(points);
		} catch (PointOutsideIntervalException e) {
			exceptionThrown = true;
		}
		Assert.assertTrue("Point Outside Interval Exception thrown",
				exceptionThrown);

	}

	@Test
	public void testDiscreteMeasurement() {
		DiscreteMeasurement dm = new DiscreteMeasurement();
		assertEquals(0, dm.size());
	}

	@Test
	public void testSampleSuccesful() {
		DiscreteMeasurement dm = twoPointDiscreteMeasurement();

		double sample = dm.sample(random);
		Assert.assertEquals(5.0, sample, 0.1);
	}

	@Test
	public void testSampleIncomplete() {
		DiscreteMeasurement dm = onePointDiscreteMeasurement();

		boolean exceptionThrown = false;
		try {
			dm.sample(random);
		} catch (InvalidIntervalException e) {
			exceptionThrown = true;
		}

		Assert.assertTrue("InvalidIntervalException thrown", exceptionThrown);
	}

	@Test
	public void testDeepCopy() {
		DiscreteMeasurement dm = twoPointDiscreteMeasurement();
		DiscreteMeasurement dm2 = (DiscreteMeasurement) dm.deepCopy();
		Assert.assertFalse(dm == dm2);
		Assert.assertEquals(dm.get(0), dm2.get(0));
		Assert.assertEquals(dm.get(1), dm2.get(1));
	}

	@Test
	public void testAddPoint2DSuccesful() {
		DiscreteMeasurement dm = onePointDiscreteMeasurement();
		Assert.assertTrue(dm.add(new Point2D(4, 0.5)));
	}

	@Test
	public void testAddPoint2DFailed() {
		DiscreteMeasurement dm = twoPointDiscreteMeasurement();
		Assert.assertFalse(dm.add(new Point2D(4, 0.5)));
	}

	@Test
	public void testRemoveObject() {
		DiscreteMeasurement dm = twoPointDiscreteMeasurement();
		Point2D i0 = dm.get(0);
		Point2D removed = dm.remove(0);
		Assert.assertEquals(i0, removed);
		Assert.assertEquals(1, dm.size());
	}

	@Test
	public void testTotalProbability() {
		DiscreteMeasurement dm = twoPointDiscreteMeasurement();
		dm.set(0, new Point2D(4, 0.4));
		dm.remove(1);
		dm.add(new Point2D(5, 0.2));
		dm.add(0, new Point2D(5, 0.4));
		Assert.assertEquals(1.0, dm.getTotalProbability());
	}

}
