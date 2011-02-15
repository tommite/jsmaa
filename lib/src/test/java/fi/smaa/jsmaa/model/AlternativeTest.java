/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

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

import org.drugis.common.JUnitUtil;
import org.junit.Before;
import org.junit.Test;

public class AlternativeTest {
	
	private Alternative a;
	
	@Before
	public void setUp() {
		a = new Alternative("alt");		
	}
	
	@Test
	public void testSetName() {
		JUnitUtil.testSetter(a, Alternative.PROPERTY_NAME, "alt", "hello");
	}
	
	@Test
	public void testConstructor()  {
		assertEquals("alt", a.getName());
	}
		
	@Test
	public void testToString() {
		assertEquals("alt", a.toString());
	}
		
	@Test
	public void testEquals() {
		Alternative a2 = new Alternative("a2");
		assertEquals(a, a);
		assertFalse(a.equals(a2));
		assertFalse(a.equals("alt"));
	}
	
	@Test
	public void testCompareTo() {
		Alternative a = new Alternative("a");
		Alternative a2 = new Alternative("a");
		assertEquals(0, a.compareTo(a2));
	}
	
	@Test
	public void testDeepCopy() {
		Alternative a = new Alternative("a");
		assertFalse(a.equals(a.deepCopy()));
		assertEquals(a.getName(), a.deepCopy().getName());
	}

}
