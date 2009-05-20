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
import nl.rug.escher.common.JUnitUtil;

import org.junit.Test;

import fi.smaa.Rank;

public class RankTest {

	@Test
	public void testSetRank() {
		JUnitUtil.testSetter(new Rank(1), Rank.PROPERTY_RANK, 1, new Integer(2));
	}
	
	@Test
	public void testEquals() {
		assertTrue(new Rank(2).equals(new Rank(2)));
		assertFalse(new Rank(1).equals(new Rank(2)));
		assertFalse(new Rank(1).equals(null));
		assertFalse(new Rank(1).equals("blaa"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testErrorConstructor() {
		new Rank(0);
	}
}
