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
import org.jfree.data.general.DatasetGroup;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.jfreechart.CategoryAcceptabilitiesDataset;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Category;
import fi.smaa.jsmaa.simulator.SMAATRIResults;

public class CategoryAcceptabilitiesDatasetTest {
	
	private SMAATRIResults res;
	private CategoryAcceptabilitiesDataset data;
	private Alternative a1;
	private Alternative a2;
	private Category c1;
	private Category c2;
	private List<Alternative> alts;
	private List<Category> cats;
	private Integer[] categoryHits;
	

	@Before
	public void setUp() {
		a1 = new Alternative("a1");
		a2 = new Alternative("a2");
		alts = new ArrayList<Alternative>();
		alts.add(a1);
		alts.add(a2);
		
		c1 = new Category("c1");
		c2 = new Category("c2");
		cats = new ArrayList<Category>();
		cats.add(c1);
		cats.add(c2);
		
		res = new SMAATRIResults(alts, cats, 2);
		categoryHits = new Integer[] {1, 0};
		res.update(categoryHits);
		
		data = new CategoryAcceptabilitiesDataset(res);
	}
	
	@Test
	public void testGetColumnIndex() {
		assertEquals(1, data.getColumnIndex(a2));
		assertEquals(-1, data.getColumnIndex(new Alternative("aaaa")));
	}
	
	@Test
	public void testGetColumnKey() {
		assertEquals(a2, data.getColumnKey(1));
	}
	
	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void testGetColumnKeyThrows() {
		data.getColumnKey(4);
	}
	
	@Test
	public void testGetColumnKeys() {
		assertEquals(alts, data.getColumnKeys());
	}
	
	@Test
	public void testGetRowIndex() {
		assertEquals(1, data.getRowIndex(c2));
		assertEquals(-1, data.getRowIndex(new Alternative("aaaa")));
	}
	
	@Test
	public void testGetRowKey() {
		assertEquals(c2, data.getRowKey(1));		
	}
	
	@Test(expected=ArrayIndexOutOfBoundsException.class)
	public void testGetRowKeyThrows() {
		data.getRowKey(4);
	}
	
	@Test
	public void testGetRowKeys() {
		assertEquals(cats, data.getRowKeys());
	}
	
	@Test
	public void testGetValue() {
		assertEquals(new Double(0.0), data.getValue(c1, a1));
		assertEquals(new Double(1.0), data.getValue(c1, a2));
		assertEquals(new Double(1.0), data.getValue(c2, a1));
		assertEquals(new Double(0.0), data.getValue(c2, a2));
	}
	
	@Test
	public void testGetColumnCount() {
		assertEquals(alts.size(), data.getColumnCount());
	}
	
	@Test
	public void testGetRowCount() {
		assertEquals(cats.size(), data.getRowCount());
	}
	
	@Test
	public void testGetValueInts() {
		assertEquals(new Double(0.0), data.getValue(0, 0));
		assertEquals(new Double(1.0), data.getValue(0, 1));
		assertEquals(new Double(1.0), data.getValue(1, 0));
		assertEquals(new Double(0.0), data.getValue(1, 1));
	}
	
	@Test
	public void testGetSetGroup() {
		DatasetGroup g = new DatasetGroup("group");
		data.setGroup(g);
		assertEquals(g, data.getGroup());
	}
	
	@Test
	public void testResultChangeFires() {
		DatasetChangeListener list = createMock(DatasetChangeListener.class);
		data.addChangeListener(list);
		list.datasetChanged((DatasetChangeEvent) JUnitUtil.eqEventObject(new DatasetChangeEvent(data, data)));
		replay(list);
		res.update(categoryHits);
		verify(list);
	}

	@Test
	public void testCategoryNameChangeFires() {
		DatasetChangeListener mock = createMock(DatasetChangeListener.class);
		mock.datasetChanged((DatasetChangeEvent) JUnitUtil.eqEventObject(new DatasetChangeEvent(data, data)));
		data.addChangeListener(mock);
		replay(mock);
		c1.setName("new cat");
		verify(mock);
	}
	
	@Test
	public void testSetResults() {
		DatasetChangeListener mock = createMock(DatasetChangeListener.class);
		mock.datasetChanged((DatasetChangeEvent) JUnitUtil.eqEventObject(new DatasetChangeEvent(data, data)));
		data.addChangeListener(mock);
		replay(mock);
		data.setResults(new SMAATRIResults(alts, cats, 2));
		verify(mock);		
	}

}
