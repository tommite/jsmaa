/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper 2012.

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
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.fail;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.drugis.common.JUnitUtil;
import org.junit.Before;
import org.junit.Test;

public class RankSetTest {

	private RankSet<Alternative> rs;
	private Alternative altb;
	private Alternative alta;
	private Alternative altc;

	@Before
	public void setUp() {
		rs = new RankSet<Alternative>();
		
		alta = new Alternative("a");		
		altb = new Alternative("b");		
		altc = new Alternative("c");		
	}
	
	@Test
	public void testParamConstructor() {
		Map<Alternative, Rank> m = new HashMap<Alternative, Rank>();
		m.put(alta, new Rank(3));
		m.put(altb, new Rank(2));
		m.put(altc, new Rank(1));
		
		rs = new RankSet<Alternative>(m);
		assertEquals(new Rank(3), rs.getRank(alta));
		assertEquals(new Rank(2), rs.getRank(altb));
		assertEquals(new Rank(1), rs.getRank(altc));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testParamConstructorThrows() {
		Map<Alternative, Rank> m = new HashMap<Alternative, Rank>();
		m.put(alta, new Rank(3));
		m.put(altc, new Rank(1));
		
		rs = new RankSet<Alternative>(m);		
	}
	
	@Test
	public void testParamConstructorConnectsListeners() {
		Map<Alternative, Rank> m = new HashMap<Alternative, Rank>();
		Rank ra = new Rank(2);
		Rank rb = new Rank(1);		
		m.put(alta, ra);
		m.put(altb, rb);
		
		rs = new RankSet<Alternative>(m);
		
		rb.setRank(2);
		assertEquals(new Integer(1), ra.getRank());
	}
	
	@Test
	public void testAddObject() {
		rs.addObject(alta);
		assertEquals(new Rank(1), rs.getRank(alta));
		assertEquals(null, rs.getRank(altb));
		
		rs.addObject(altb);
		assertEquals(new Rank(2), rs.getRank(altb));		
	}
	
	@Test
	public void testChangeRank() {
		rs.addObject(alta);
		rs.addObject(altb);

		assertEquals(new Integer(2), rs.getRank(altb).getRank());		
		rs.getRank(alta).setRank(2);
		assertEquals(new Integer(1), rs.getRank(altb).getRank());
	}
	
	@Test
	public void testDeleteObject() {
		rs.addObject(alta);
		rs.addObject(altb);
		rs.addObject(altc);
		assertEquals(new Integer(3), rs.getRank(altc).getRank());
		
		rs.deleteObject(alta);
		assertEquals(new Integer(2), rs.getRank(altc).getRank());		
		assertEquals(new Integer(1), rs.getRank(altb).getRank());
		assertEquals(null, rs.getRank(alta));
	}
	
	@Test
	public void testGetObjects() {
		rs.addObject(alta);
		assertEquals(Collections.singleton(alta), rs.getObjects());
	}
	
	@Test
	public void testDeepCopy() {
		rs.addObject(alta);
		rs.addObject(altb);
		rs.getRank(alta).setRank(2);
		
		RankSet<Alternative> newSet = rs.deepCopy();
		
		Rank newA = newSet.getRank(alta);
		Rank newB = newSet.getRank(altb);
		
		assertNotSame(rs.getRank(alta), newA);
		assertNotSame(rs.getRank(altb), newB);
		
		assertEquals(new Integer(2), newA.getRank());
		assertEquals(new Integer(1), newB.getRank());
		
		assertEquals(rs.getObjects(), newSet.getObjects());
	}
	
	@Test
	public void testSetRanksFireSimultaneously() {
		rs.addObject(alta);
		rs.addObject(altb);
		
		PropertyChangeListener l = new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {				
				if (rs.getRank(altb).getRank().equals(rs.getRank(alta))) {
					fail("rank change fires individually - set in inconsistent state");
				}
			}			
		};
		rs.getRank(alta).addPropertyChangeListener(l);
		rs.getRank(altb).addPropertyChangeListener(l);
		
		rs.getRank(alta).setRank(2);
		rs.deleteObject(alta);
	}
	
	@Test
	public void testSerialization() throws Exception {
		rs.addObject(alta);
		rs.addObject(altb);
		rs.getRank(alta).setRank(2);
		
		RankSet<Alternative> newSet = JUnitUtil.serializeObject(rs);

		assertEquals(2, newSet.getObjects().size());
		
		// check that all the objects have correct ranks set
		for (Alternative a : newSet.getObjects()) {
			if (a.getName().equals("a")) {
				assertEquals(new Integer(2), newSet.getRank(a).getRank());
			} else if (a.getName().equals("b")) {
				assertEquals(new Integer(1), newSet.getRank(a).getRank());
			} else {
				fail(); // there should be no other objects
			}
		}
		
		// check that the ranks still change
		Alternative newAltA = null;
		Alternative newAltB = null;		
		for (Alternative a : newSet.getObjects()) {
			if (a.getName().equals("a")) {
				newAltA = a;
			} else if (a.getName().equals("b")) {
				newAltB = a;
			} else {
				fail();
			}
		}
		newSet.getRank(newAltA).setRank(1);
		assertEquals(new Integer(2), newSet.getRank(newAltB).getRank());
	}
}
