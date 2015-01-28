/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.drugis.common.JUnitUtil;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.jfreechart.RankAcceptabilitiesDataset;
import fi.smaa.jsmaa.gui.jfreechart.RankAcceptabilitiesDataset.Rank;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ScaleCriterion;
import fi.smaa.jsmaa.simulator.SMAA2Results;

public class RankAcceptabilitiesDataSetTest {
	
	private RankAcceptabilitiesDataset data;
	private SMAA2Results res;
	private Alternative a1;
	private Alternative a2;
	private Rank r1;
	private Rank r2;
	private List<Alternative> alts;
	private List<Rank> ranks;
	private double[] weights;
	private int[] ranksHits;

	@Before
	public void setUp() {
		a1 = new Alternative("a1");
		a2 = new Alternative("a2");
		alts = new ArrayList<Alternative>();
		alts.add(a1);
		alts.add(a2);
		
		ScaleCriterion c1 = new ScaleCriterion("c1");
		ScaleCriterion c2 = new ScaleCriterion("c1");
		
		List<Criterion> crit = new ArrayList<Criterion>();
		crit.add(c1);
		crit.add(c2);
		
		r1 = new Rank(0);
		r2 = new Rank(1);
		ranks = new ArrayList<Rank>();
		ranks.add(r1);
		ranks.add(r2);
		
		res = new SMAA2Results(alts, crit, 2);
		weights = new double[]{0.2, 0.8};
		ranksHits = new int[] { 1, 0 };
		
		res.update(ranksHits, weights);
		
		data = new RankAcceptabilitiesDataset(res);
	}
	
	@Test
	public void testGetRowIndex() {
		assertEquals(1, data.getRowIndex(r2));
		assertEquals(-1, data.getRowIndex(new Rank(5)));
	}
	
	@Test
	public void testGetRowKey() {
		assertEquals(r2, data.getRowKey(1));		
	}
	
	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void testGetRowKeyThrows() {
		data.getRowKey(4);
	}
	
	@Test
	public void testGetRowKeys() {
		assertEquals(ranks, data.getRowKeys());
	}
	
	@Test
	public void testGetValue() {
		assertEquals(new Double(0.0), data.getValue(r1, a1));
		assertEquals(new Double(1.0), data.getValue(r1, a2));
		assertEquals(new Double(1.0), data.getValue(r2, a1));
		assertEquals(new Double(0.0), data.getValue(r2, a2));
	}
	
	@Test
	public void testGetRowCount() {
		assertEquals(ranks.size(), data.getRowCount());
	}
	
	@Test
	public void testGetValueInts() {
		assertEquals(new Double(0.0), data.getValue(0, 0));
		assertEquals(new Double(1.0), data.getValue(0, 1));
		assertEquals(new Double(1.0), data.getValue(1, 0));
		assertEquals(new Double(0.0), data.getValue(1, 1));
	}
	
	@Test
	public void testAlternativeNameChangeFires() {
		DatasetChangeListener mock = createMock(DatasetChangeListener.class);
		mock.datasetChanged((DatasetChangeEvent) JUnitUtil.eqEventObject(new DatasetChangeEvent(data, data)));
		data.addChangeListener(mock);
		replay(mock);
		a1.setName("new alt");
		verify(mock);
	}
}
