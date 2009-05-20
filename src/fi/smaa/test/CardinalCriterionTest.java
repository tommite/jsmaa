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

import nl.rug.escher.common.JUnitUtil;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.CardinalCriterion;
import fi.smaa.common.Interval;

public class CardinalCriterionTest {
	
	private CardinalCriterion criterion;
	
	@Before
	public void setUp() {
		criterion = new CardinalCriterion() {
			public void sample(double[] target) {
			}
			public String getTypeLabel() {
				return null;
			}
			public Interval getScale() {
				return new Interval(0.0, 1.0);
			}
			protected void updateMeasurements() {
			}
			public String measurementsToString() {
				return null;
			}
		};
		criterion.setName("name");
	}	

	@Test
	public void testSetAscending() {
		JUnitUtil.testSetter(criterion, CardinalCriterion.PROPERTY_ASCENDING, true, false);
	}

}
