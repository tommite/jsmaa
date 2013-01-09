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
package fi.smaa.jsmaa.gui.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.RangeVetoer;
import fi.smaa.jsmaa.model.Interval;

public class RangeVetoerTest {
	
	private RangeVetoer vetoer;

	@Before
	public void setUp() {
		vetoer = new RangeVetoer(new Interval(0.0, 1.0), "error", false);
	}
	
	@Test
	public void testVetoer() {
		assertTrue(vetoer.check(0.0, 0.0));
		assertTrue(vetoer.check(0.0, -1.0));
		assertFalse(vetoer.check(0.0, 0.1));
		assertFalse(vetoer.check(0.0, 1.1));
	}
	
	@Test
	public void testAboveVeto() {
		vetoer = new RangeVetoer(new Interval(0.0, 1.0), "error", true);
		assertFalse(vetoer.check(0.0, 0.0));
		assertFalse(vetoer.check(0.0, -1.0));
		assertFalse(vetoer.check(0.0, 0.1));
		assertTrue(vetoer.check(0.0, 1.1));		
	}
	
	@Test
	public void testErrorMsg() {
		assertEquals("error", vetoer.getErrorMessage(null));
	}
}
