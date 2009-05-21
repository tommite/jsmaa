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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import org.junit.Before;
import org.junit.Test;

import fi.smaa.Alternative;
import fi.smaa.UniformCriterion;
import fi.smaa.UtilityFunction;
import fi.smaa.common.Interval;

public class UtilityFunctionTest {
	
	private UniformCriterion crit;
	
	@Before
	public void setUp() {
		crit = new UniformCriterion("crit");
		ArrayList<Alternative> alts = new ArrayList<Alternative>();
		alts.add(new Alternative ("alt1"));
		alts.add(new Alternative ("alt2"));
		crit.setAlternatives(alts);
		
		Map<Alternative, Interval> meas = new HashMap<Alternative, Interval>();
		meas.put(alts.get(0), new Interval(1.0, 1.5));
		meas.put(alts.get(1), new Interval(1.5, 2.0));
		crit.setMeasurements(meas);
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

	
}
