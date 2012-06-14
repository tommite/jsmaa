/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.

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
package fi.smaa.jsmaa.gui.jfreechart.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.jfreechart.CentralWeightsDataset;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ScaleCriterion;
import fi.smaa.jsmaa.simulator.SMAA2Results;

public class CentralWeightsDatasetTest {
	
	private CentralWeightsDataset data;
	private SMAA2Results res;
	private Alternative a1;
	private Alternative a2;
	private List<Alternative> alts;
	private double[] weights;
	private int[] ranksHits;
	private ScaleCriterion c1;
	private ScaleCriterion c2;
	private List<Criterion> crit;

	@Before
	public void setUp() {
		a1 = new Alternative("a1");
		a2 = new Alternative("a2");
		alts = new ArrayList<Alternative>();
		alts.add(a1);
		alts.add(a2);
		
		c1 = new ScaleCriterion("c1");
		c2 = new ScaleCriterion("c2");
		
		crit = new ArrayList<Criterion>();
		crit.add(c1);
		crit.add(c2);
				
		res = new SMAA2Results(alts, crit, 1);
		weights = new double[]{0.2, 0.8};
		ranksHits = new int[] { 1, 0 };
		
		res.update(ranksHits, weights);
		
		data = new CentralWeightsDataset(res);
	}
	
	@Test
	public void testGetRowIndex() {
		assertEquals(0, data.getRowIndex(a2));
		assertEquals(-1, data.getRowIndex(a1));
	}
	
	@Test
	public void testGetRowKey() {
		assertEquals(a2, data.getRowKey(0));		
	}
	
	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void testGetRowKeyThrows() {
		data.getRowKey(4);
	}
	
	@Test
	public void testGetRowKeys() {
		assertEquals(Collections.singletonList(a2), data.getRowKeys());
	}
	
	@Test
	public void testGetValue() {
		assertEquals(new Double(0.2), data.getValue(a2, c1));
		assertEquals(new Double(0.8), data.getValue(a2, c2));
	}
	
	@Test
	public void testGetRowCount() {
		assertEquals(1, data.getRowCount());
	}
	
	@Test
	public void testGetValueInts() {
		assertEquals(new Double(0.2), data.getValue(0, 0));
		assertEquals(new Double(0.8), data.getValue(0, 1));
	}
	
	@Test
	public void testGetColumnIndex() {
		assertEquals(1, data.getColumnIndex(c2));
		assertEquals(-1, data.getColumnIndex(new ScaleCriterion("cccc")));
	}
	
	@Test
	public void testGetColumnKey() {
		assertEquals(c2, data.getColumnKey(1));
	}
	
	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void testGetColumnKeyThrows() {
		data.getColumnKey(5);
	}
	
	@Test
	public void testGetColumnKeys() {
		assertEquals(crit, data.getColumnKeys());
	}
	
	@Test
	public void testGetColumnCount() {
		assertEquals(crit.size(), data.getColumnCount());
	}
}
