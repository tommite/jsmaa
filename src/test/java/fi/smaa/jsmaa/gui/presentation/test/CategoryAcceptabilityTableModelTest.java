package fi.smaa.jsmaa.gui.presentation.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.JUnitUtil;
import fi.smaa.jsmaa.gui.presentation.CategoryAcceptabilityTableModel;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.simulator.SMAATRIResults;

import static org.easymock.EasyMock.*;

public class CategoryAcceptabilityTableModelTest {
	
	private SMAATRIResults res;
	private CategoryAcceptabilityTableModel model;
	private Alternative a1;
	private Alternative a2;
	
	@Before
	public void setUp() {
		List<Alternative> alts = new ArrayList<Alternative>();
		a1 = new Alternative("a1");
		a2 = new Alternative("a2");		
		alts.add(a1);
		alts.add(a2);
		List<Alternative> cats = new ArrayList<Alternative>();
		cats.add(new Alternative("c1"));
		cats.add(new Alternative("c2"));
		cats.add(new Alternative("c3"));
		
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


}
