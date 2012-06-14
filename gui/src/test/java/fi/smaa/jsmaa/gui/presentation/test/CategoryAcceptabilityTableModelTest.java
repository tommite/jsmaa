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
package fi.smaa.jsmaa.gui.presentation.test;

import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.drugis.common.JUnitUtil;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.presentation.CategoryAcceptabilityTableModel;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Category;
import fi.smaa.jsmaa.simulator.SMAATRIResults;

public class CategoryAcceptabilityTableModelTest {
	
	private SMAATRIResults res;
	private CategoryAcceptabilityTableModel model;
	private Alternative a1;
	private Alternative a2;
	private Category cat1;
	private List<Alternative> alts;
	private List<Category> cats;
	
	@Before
	public void setUp() {
		alts = new ArrayList<Alternative>();
		a1 = new Alternative("a1");
		a2 = new Alternative("a2");		
		alts.add(a1);
		alts.add(a2);
		cats = new ArrayList<Category>();
		cat1 = new Category("c1");
		cats.add(cat1);
		cats.add(new Category("c2"));
		cats.add(new Category("c3"));
		
		res = new SMAATRIResults(alts, cats, 1);
		
		Integer[] catranks = new Integer[] { 0, 1};
		res.update(catranks);
		
		model = new CategoryAcceptabilityTableModel(res);
	}

	@Test
	public void testGetRowCount() {
		assertEquals(2, model.getRowCount());
	}
	
	@Test
	public void testGetColumnCount() {
		assertEquals(4, model.getColumnCount());
	}
	
	@Test
	public void testGetValueAt() {
		// first alternative
		assertEquals(a1, model.getValueAt(0, 0));
		assertEquals(new Double(1.0), model.getValueAt(0, 1));
		assertEquals(new Double(0.0), model.getValueAt(0, 2));
		assertEquals(new Double(0.0), model.getValueAt(0, 3));
		// second alternative
		assertEquals(a2, model.getValueAt(1, 0));		
		assertEquals(new Double(0.0), model.getValueAt(1, 1));
		assertEquals(new Double(1.0), model.getValueAt(1, 2));
		assertEquals(new Double(0.0), model.getValueAt(1, 3));	
	}
	
	@Test
	public void testGetColumnName() {
		assertEquals("Alternative", model.getColumnName(0));		
		assertEquals("c1", model.getColumnName(1));
		assertEquals("c2", model.getColumnName(2));
		assertEquals("c3", model.getColumnName(3));
	}
	
	@Test
	public void testTableUpdates() {
		Integer[] catranks = new Integer[] { 1, 0};
		
		TableModelListener mock = JUnitUtil.mockTableModelListener(new TableModelEvent(model));
		model.addTableModelListener(mock);
		res.update(catranks);
		verify(mock);
		
		// first alternative
		assertEquals(new Double(0.5), model.getValueAt(0, 1));
		assertEquals(new Double(0.5), model.getValueAt(0, 2));
		assertEquals(new Double(0.0), model.getValueAt(0, 3));
		// second alternative
		assertEquals(new Double(0.5), model.getValueAt(1, 1));
		assertEquals(new Double(0.5), model.getValueAt(1, 2));
		assertEquals(new Double(0.0), model.getValueAt(1, 3));			
	}
	
	@Test
	public void testAlternativeNameChanged() {
		TableModelListener mock = JUnitUtil.mockTableModelListener(new TableModelEvent(model));
		model.addTableModelListener(mock);
		a1.setName("new alt");
		verify(mock);
	}
	
	@Test
	public void testCategoryNameChanged() {
		TableModelListener mock = JUnitUtil.mockTableModelListener(new TableModelEvent(model));
		model.addTableModelListener(mock);
		cat1.setName("new cat");
		verify(mock);
	}	
	
	@Test
	public void testSetResults() {
		TableModelListener mock = JUnitUtil.mockTableModelListener(new TableModelEvent(model));
		model.addTableModelListener(mock);
		model.setResults(new SMAATRIResults(alts, cats, 1));
		verify(mock);
	}

}
