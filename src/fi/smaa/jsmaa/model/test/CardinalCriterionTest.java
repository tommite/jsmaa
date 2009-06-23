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
import nl.rug.escher.common.JUnitUtil;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.Interval;

public class CardinalCriterionTest {
	
	private CardinalCriterion criterion;
	
	@Before
	public void setUp() {
		criterion = new CardinalCriterion("crit");
	}
	
	@Test
	public void test1Contructor() {
		CardinalCriterion crit = new CardinalCriterion("c");
		assertEquals("c", crit.getName());
		assertEquals(true, crit.getAscending());
	}
	
	@Test
	public void test2Constructor() {
		CardinalCriterion crit = new CardinalCriterion("c", false);
		assertEquals("c", crit.getName());
		assertEquals(false, crit.getAscending());
	}

	@Test
	public void testSetAscending() {
		JUnitUtil.testSetter(criterion, CardinalCriterion.PROPERTY_ASCENDING, true, false);
	}
	
	@Test
	public void testSetScale() {
		Interval oldScale = new Interval(0.0, 0.0);
		Interval newScale = new Interval(0.0, 1.0);
		JUnitUtil.testSetter(criterion, CardinalCriterion.PROPERTY_SCALE, oldScale, newScale);
	}
	
	@Test
	public void testGetTypeLabel() {
		assertEquals("Cardinal", criterion.getTypeLabel());
	}

}
