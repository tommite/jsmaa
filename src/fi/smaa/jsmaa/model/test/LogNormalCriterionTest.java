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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.LogNormalCriterion;

public class LogNormalCriterionTest {

	private List<Alternative> alts;
	private LogNormalCriterion crit;
	
	@Before
	public void setUp() {
		crit = new LogNormalCriterion("crit");
		alts = new ArrayList<Alternative>();
		alts.add(new Alternative("alt1"));
		alts.add(new Alternative("alt2"));
	} 
	
	@Test
	public void testGetTypeLabel() {
		assertEquals("LogNormal", crit.getTypeLabel());
	}	
}
