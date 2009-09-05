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

import fi.smaa.common.JUnitUtil;
import fi.smaa.jsmaa.model.ScaleCriterion;
import fi.smaa.jsmaa.model.Interval;

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

}
