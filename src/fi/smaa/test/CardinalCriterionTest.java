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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import nl.rug.escher.common.JUnitUtil;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.CardinalCriterion;
import fi.smaa.Measurement;
import fi.smaa.common.Interval;

public class CardinalCriterionTest {
	
	@SuppressWarnings("unchecked")
	private CardinalCriterion criterion;
	
	@Before
	public void setUp() {
		criterion = getInstance();
	}

	@SuppressWarnings("unchecked")
	private CardinalCriterion getInstance() {
		return new CardinalCriterion("name") {
			@Override
			public void sample(double[] target) {
			}
			@Override
			public String getTypeLabel() {
				return null;
			}
			@Override
			public Interval getScale() {
				return new Interval(0.0, 1.0);
			}
			@Override
			protected Measurement createMeasurement() {
				return null;
			}
			@Override
			public Map getMeasurements() {
				return new HashMap();
			}
			@Override
			protected Interval createScale(Map oldMeas) {
				return null;
			}
		};
	}	

	@Test
	public void testSetAscending() {
		JUnitUtil.testSetter(criterion, CardinalCriterion.PROPERTY_ASCENDING, true, false);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testDeepEquals() {
		CardinalCriterion c2 = getInstance();
		c2.setAscending(false);
		assertFalse(criterion.deepEquals(c2));
		c2.setAscending(true);
		assertTrue(criterion.deepEquals(c2));
	}

}
