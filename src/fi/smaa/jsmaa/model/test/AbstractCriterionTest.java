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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import nl.rug.escher.common.JUnitUtil;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.model.AbstractCriterion;
import fi.smaa.jsmaa.model.Criterion;

public class AbstractCriterionTest {
	
	private AbstractCriterion criterion;
	
	@SuppressWarnings("serial")
	private AbstractCriterion createInstance() {
		return new AbstractCriterion("name") {
			public String getTypeLabel() {
				return null;
			}
			public AbstractCriterion deepCopy() {
				return null;
			}			
		};
		
	}
	
	@Before
	public void setUp() {
		criterion = createInstance();
	}
	
	@Test
	public void testConstructor() {
		AbstractCriterion c = createInstance();
		c.setName("c");
		assertEquals("c", c.getName());
	}
	
	@Test
	public void testSetName() {
		JUnitUtil.testSetter(criterion, Criterion.PROPERTY_NAME, "name", "nameCrit");
	}

	
	@Test
	public void testToString() {
		criterion.setName("crit");
		assertEquals("crit", criterion.toString());
	}
	
	@Test
	public void testequals() {
		AbstractCriterion c2 = createInstance();
		assertTrue(criterion.equals(c2));
		c2.setName("newname");
		assertFalse(criterion.equals(c2));
	}	
}
