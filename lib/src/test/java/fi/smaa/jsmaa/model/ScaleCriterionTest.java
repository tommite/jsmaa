/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

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

import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.beans.PropertyChangeListener;
import java.util.List;

import org.drugis.common.JUnitUtil;
import org.junit.Before;
import org.junit.Test;

public class ScaleCriterionTest {
	
	private ScaleCriterion criterion;
	
	@Before
	public void setUp() {
		criterion = new ScaleCriterion("crit");
	}
	
	@Test
	public void test1Contructor() {
		ScaleCriterion crit = new ScaleCriterion("c");
		assertEquals("c", crit.getName());
		assertEquals(true, crit.getAscending());
	}
	
	@Test
	public void test2Constructor() {
		ScaleCriterion crit = new ScaleCriterion("c", false);
		assertEquals("c", crit.getName());
		assertEquals(false, crit.getAscending());
	}
	
	@Test
	public void testSetScale() {
		Interval oldScale = new Interval(0.0, 0.0);
		Interval newScale = new Interval(0.0, 1.0);
		JUnitUtil.testSetter(criterion, ScaleCriterion.PROPERTY_SCALE, oldScale, newScale);
	}
	
	@Test
	public void testGetTypeLabel() {
		assertEquals("Cardinal", criterion.getTypeLabel());
	}
	
	@Test
	public void testGetDefaultValuePoints() {
		criterion.setScale(new Interval(0.0, 0.2));
		List<Point2D> points = criterion.getValuePoints();
		assertEquals(2, points.size());
		assertEquals(new Point2D(0.0, 0.0), points.get(0));
		assertEquals(new Point2D(0.2, 1.0), points.get(1));
	}
	
	@Test
	public void testAddValuePoint() throws InvalidValuePointException {
		criterion.setScale(new Interval(0.1, 0.8));
		criterion.addValuePoint(new Point2D(0.5, 0.7));
		List<Point2D> points = criterion.getValuePoints();
		assertEquals(3, points.size());
		assertEquals(new Point2D(0.1, 0.0), points.get(0));
		assertEquals(new Point2D(0.5, 0.7), points.get(1));
		assertEquals(new Point2D(0.8, 1.0), points.get(2));
	}
	
	@Test
	public void testDeleteValuePoint() throws InvalidValuePointException {
		criterion.setScale(new Interval(0.1, 0.8));
		criterion.addValuePoint(new Point2D(0.5, 0.7));
		List<Point2D> points = criterion.getValuePoints();
		assertEquals(3, points.size());
		criterion.deleteValuePoint(1);
		assertEquals(2, criterion.getValuePoints().size());
	}
	
	@Test
	public void testDeleteValuePointDirect() throws InvalidValuePointException {
		criterion.setScale(new Interval(0.1, 0.8));
		Point2D pt = new Point2D(0.5, 0.7);
		criterion.addValuePoint(pt);
		List<Point2D> points = criterion.getValuePoints();
		assertEquals(3, points.size());
		criterion.deleteValuePoint(pt);
		assertEquals(2, criterion.getValuePoints().size());
	}	
	
	@Test(expected=PointOutsideIntervalException.class)
	public void testAddValuePointOutsideDomainThrowsException() throws InvalidValuePointException {
		criterion.setScale(new Interval(0.0, 1.0));
		criterion.addValuePoint(new Point2D(1.1, 0.7));		
	}
	
	@Test(expected=PointOutsideIntervalException.class)
	public void testAddValuePointOutsideRangeThrowsException() throws InvalidValuePointException {
		criterion.setScale(new Interval(0.0, 1.0));
		criterion.addValuePoint(new Point2D(0.7, 1.1));		
	}
	
	@Test(expected=FunctionNotMonotonousException.class)
	public void testAddValuePointNonMonotonous() throws InvalidValuePointException {
		criterion.setScale(new Interval(0.0, 1.0));
		criterion.addValuePoint(new Point2D(0.8, 0.7));
		criterion.addValuePoint(new Point2D(0.9, 0.6));
	}
	
	@Test(expected=FunctionNotMonotonousException.class)
	public void testAddValuePointNonMonotonous2() throws InvalidValuePointException {
		criterion.setScale(new Interval(0.0, 1.0));
		criterion.addValuePoint(new Point2D(0.8, 0.7));
		criterion.addValuePoint(new Point2D(0.7, 0.9));
	}
	
	@Test(expected=FunctionNotMonotonousException.class)
	public void testAddValuePointNonMonotonous3() throws InvalidValuePointException {
		criterion.setScale(new Interval(0.0, 1.0));
		criterion.setAscending(false);
		criterion.addValuePoint(new Point2D(0.8, 0.7));
		criterion.addValuePoint(new Point2D(0.9, 0.8));
	}
	
	@Test(expected=FunctionNotMonotonousException.class)
	public void testAddValuePointNonMonotonous4() throws InvalidValuePointException {
		criterion.setScale(new Interval(0.0, 1.0));
		criterion.setAscending(false);
		criterion.addValuePoint(new Point2D(0.8, 0.7));
		criterion.addValuePoint(new Point2D(0.7, 0.5));
	}		

	
	@Test(expected=PointAlreadyExistsException.class)
	public void testAddPointTwiceThrows() throws InvalidValuePointException {
		criterion.setScale(new Interval(0.0, 1.0));
		criterion.addValuePoint(new Point2D(0.8, 0.7));
		criterion.addValuePoint(new Point2D(0.8, 0.7));
	}
	
	@Test
	public void testDescendingFunction() {
		criterion.setScale(new Interval(0.0, 1.0));
		criterion.setAscending(false);
		List<Point2D> pts = criterion.getValuePoints();
		assertEquals(new Point2D(0.0, 1.0), pts.get(0));
		assertEquals(new Point2D(1.0, 0.0), pts.get(1));
	}
	
	@Test
	public void testDescendingPointsRemovedFunction() throws InvalidValuePointException {
		criterion.setScale(new Interval(0.0, 1.0));
		criterion.addValuePoint(new Point2D(0.2, 0.5));
		criterion.addValuePoint(new Point2D(0.4, 0.7));
		criterion.setAscending(false);
		List<Point2D> pts = criterion.getValuePoints();
		assertEquals(new Point2D(0.0, 1.0), pts.get(0));
		assertEquals(new Point2D(1.0, 0.0), pts.get(1));
	}
	
	@Test
	public void testDescendingPointsSort() throws InvalidValuePointException {
		criterion.setAscending(false);
		criterion.setScale(new Interval(0.0, 1.0));
		Point2D pt2 = new Point2D(0.4, 0.5);
		criterion.addValuePoint(pt2);
		Point2D pt1 = new Point2D(0.2, 0.7);
		criterion.addValuePoint(pt1);
		List<Point2D> pts = criterion.getValuePoints();
		assertEquals(pt1, pts.get(1));
		assertEquals(pt2, pts.get(2));
	}
	
	@Test
	public void testSetRangePointsDrop() throws InvalidValuePointException {
		criterion.setScale(new Interval(0.1, 0.8));
		criterion.addValuePoint(new Point2D(0.15, 0.2));
		criterion.addValuePoint(new Point2D(0.5, 0.7));
		criterion.addValuePoint(new Point2D(0.6, 0.9));
		assertEquals(5, criterion.getValuePoints().size());
		criterion.setScale(new Interval(0.2, 0.55));
		List<Point2D> points = criterion.getValuePoints();
		assertEquals(3, points.size());
		assertEquals(new Point2D(0.2, 0.0), points.get(0));
		assertEquals(new Point2D(0.5, 0.7), points.get(1));
		assertEquals(new Point2D(0.55, 1.0), points.get(2));
	}
	
	@Test
	public void testAddPointFires() throws InvalidValuePointException {
		criterion.setScale(new Interval(0.0, 1.0));
		criterion.setAscending(true);
		Point2D newPt = new Point2D(0.4, 0.7);
		PropertyChangeListener mock = JUnitUtil.mockListener(criterion, ScaleCriterion.PROPERTY_VALUEPOINTS, null, null);
		criterion.addPropertyChangeListener(mock);
		criterion.addValuePoint(newPt);
		verify(mock);
	}
	
	@Test
	public void testDeepCopy() throws InvalidValuePointException {
		criterion.setScale(new Interval(0.1, 0.5));
		criterion.setAscending(false);	
		Point2D newPt = new Point2D(0.4, 0.7);
		criterion.addValuePoint(newPt);
		ScaleCriterion newc = criterion.deepCopy();
		assertEquals(3, newc.getValuePoints().size());
		assertEquals(newPt, newc.getValuePoints().get(1));
		assertEquals(false, newc.getAscending());
		assertEquals(new Interval(0.1, 0.5), newc.getScale()); 
	}
}
