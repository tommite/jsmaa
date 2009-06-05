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
import static org.junit.Assert.assertTrue;
import nl.rug.escher.common.JUnitUtil;

import org.junit.Test;

import fi.smaa.jsmaa.model.Alternative;

public class AlternativeTest {
	
	@Test
	public void testSetName() {
		JUnitUtil.testSetter(new Alternative("alt"), Alternative.PROPERTY_NAME, "alt", "hello");
	}
	
	@Test
	public void testConstructor() {
		Alternative a = new Alternative("test");
		assertEquals("test", a.getName());
	}
	
	@Test
	public void testToString() {
		Alternative a = new Alternative("alt");
		assertEquals("alt", a.toString());
	}
	
	@Test
	public void testEquals() {
		Alternative a1 = new Alternative("a1");
		Alternative a2 = new Alternative("a1");
		
		assertTrue(a1.equals(a2));
	}

}
