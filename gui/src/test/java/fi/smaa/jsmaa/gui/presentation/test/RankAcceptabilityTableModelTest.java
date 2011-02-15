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
package fi.smaa.jsmaa.gui.presentation.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.presentation.RankAcceptabilityTableModel;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ScaleCriterion;
import fi.smaa.jsmaa.simulator.SMAA2Results;

public class RankAcceptabilityTableModelTest {

	private SMAA2Results res;
	private RankAcceptabilityTableModel model;
	private Alternative a1;
	private Alternative a2;
	
	@Before
	public void setUp() {
		List<Alternative> alts = new ArrayList<Alternative>();
		a1 = new Alternative("a1");
		a2 = new Alternative("a2");		
		alts.add(a1);
		alts.add(a2);
		List<Criterion> crit = new ArrayList<Criterion>();
		crit.add(new ScaleCriterion("c1"));
		crit.add(new ScaleCriterion("c2"));
		crit.add(new ScaleCriterion("c3"));
		
		res = new SMAA2Results(alts, crit, 1);
		
		Integer[] ranks = new Integer[] { 0, 1};
		double[] weights = new double[]{0.5, 0.5, 0.0};
		
		res.update(ranks, weights);
		
		model = new RankAcceptabilityTableModel(res);
	}
	
	@Test
	public void testGetColumnCount() {
		assertEquals(3, model.getColumnCount());
	}
	
	@Test
	public void testGetValueAt() {
		// first alternative
		assertEquals(a1, model.getValueAt(0, 0));
		assertEquals(new Double(1.0), model.getValueAt(0, 1));
		assertEquals(new Double(0.0), model.getValueAt(0, 2));
		// second alternative
		assertEquals(a2, model.getValueAt(1, 0));		
		assertEquals(new Double(0.0), model.getValueAt(1, 1));
		assertEquals(new Double(1.0), model.getValueAt(1, 2));
	}
	
	@Test
	public void testGetColumnName() {
		assertEquals("Alternative", model.getColumnName(0));		
		assertEquals("Rank 1", model.getColumnName(1));
		assertEquals("Rank 2", model.getColumnName(2));
	}
}
