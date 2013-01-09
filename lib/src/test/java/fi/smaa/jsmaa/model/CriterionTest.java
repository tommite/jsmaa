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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.drugis.common.JUnitUtil;
import org.junit.Before;
import org.junit.Test;

public class CriterionTest {
	
	private Criterion criterion;
	
	@SuppressWarnings("serial")
	private Criterion createInstance() {
		return new Criterion("name") {
			public String getTypeLabel() {
				return null;
			}
			public Criterion deepCopy() {
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
		Criterion c = createInstance();
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
		Criterion c2 = createInstance();
		assertTrue(criterion.equals(criterion));
		assertFalse(criterion.equals(c2));
	}	
}
