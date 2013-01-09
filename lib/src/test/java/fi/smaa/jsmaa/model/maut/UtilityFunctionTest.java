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
package fi.smaa.jsmaa.model.maut;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.InvalidValuePointException;
import fi.smaa.jsmaa.model.Point2D;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class UtilityFunctionTest {
	
	private ScaleCriterion crit;
	
	@Before
	public void setUp() {
		crit = new ScaleCriterion("crit");		
		crit.setScale(new Interval(1.0, 2.0));
	}
	
	@Test
	public void testCardinalAscUtility() {
		double u = UtilityFunction.utility(crit, 1.6);
		assertEquals(0.6, u, 0.0001);
	}	

	@Test
	public void testCardinalDescUtility() {
		crit.setAscending(false);
		double u = UtilityFunction.utility(crit, 1.6);
		assertEquals(0.4, u, 0.0001);
	}	
	
	@Test
	public void testCardinalOverScaleUtility() {
		double u = UtilityFunction.utility(crit, 3.0);
		assertEquals(2.0, u, 0.0001);
	}
	
	@Test
	public void testCardinalUnderScaleUtility() {
		double u = UtilityFunction.utility(crit, 0.0);
		assertEquals(-1.0, u, 0.0001);
	}
	
	@Test
	public void testPiecewiseLinearUtilityWithinScale() throws InvalidValuePointException {
		crit.addValuePoint(new Point2D(1.2, 0.5));
		assertEquals(0.25, UtilityFunction.utility(crit, 1.1), 0.0001);
	}
	
	@Test
	public void testPiecewiseLinearUtilityWithinScaleDescending() throws InvalidValuePointException {
		crit.setAscending(false);
		crit.addValuePoint(new Point2D(1.2, 0.5));
		assertEquals(0.75, UtilityFunction.utility(crit, 1.1), 0.0001);
	}
	
}
