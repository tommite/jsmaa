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
package fi.smaa.common;

import static org.junit.Assert.*;


import org.junit.Test;

import fi.smaa.common.ValueIndexPair;


public class ValueIndexPairTest {

	@Test
	public void testCompare() {
		ValueIndexPair p1 = new ValueIndexPair(0, 0.0);
		ValueIndexPair p2 = new ValueIndexPair(0, -1.0);
		ValueIndexPair p3 = new ValueIndexPair(0, 1.0);
		ValueIndexPair p4 = new ValueIndexPair(1, 0.0);
		
		assertEquals(-1, p1.compareTo(p2));
		assertEquals(1, p1.compareTo(p3));
		assertEquals(0, p1.compareTo(p4));
	}
}
