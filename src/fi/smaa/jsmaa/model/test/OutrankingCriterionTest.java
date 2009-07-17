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
import fi.smaa.jsmaa.model.OutrankingCriterion;

public class OutrankingCriterionTest {

	private OutrankingCriterion crit;
	
	@Before
	public void setUp() {
		crit = new OutrankingCriterion("crit", true, 0.0, 0.5);
	}
	
	@Test
	public void testSetIndifferenceThreshold() {
		JUnitUtil.testSetter(crit, OutrankingCriterion.PROPERTY_INDIFFERENCE_THRESHOLD, 0.0, 1.0);		
	}
	
	@Test
	public void testSetPreferenceThreshold() {
		JUnitUtil.testSetter(crit, OutrankingCriterion.PROPERTY_PREFERENCE_THRESHOLD, 0.5, 1.0);		
	}
	
	@Test
	public void testGetTypeLabel() {
		assertEquals("Outranking", crit.getTypeLabel());
	}
	
	@Test
	public void testDeepCopy() {
		OutrankingCriterion c = crit.deepCopy();
		assertEquals(crit.getIndifferenceThreshold(), c.getIndifferenceThreshold(), 0.000001);
		assertEquals(crit.getPreferenceThreshold(), c.getPreferenceThreshold(), 0.000001);
		assertEquals(crit.getName(), c.getName());
		assertEquals(crit.getAscending(), c.getAscending());
	}
}
