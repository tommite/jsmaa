package fi.smaa.jsmaa.gui.jfreechart.test;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.drugis.common.JUnitUtil;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.gui.jfreechart.UtilityFunctionDataset;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class UtilityFunctionDatasetTest {

	private ScaleCriterion crit;
	private UtilityFunctionDataset ds;

	@Before
	public void setUp() {
		crit = new ScaleCriterion("crit", false);
		ds = new UtilityFunctionDataset(crit);
	}
	
	@Test
	public void testGetCriterion() {
		assertEquals(crit, ds.getCriterion());
	}

	@Test
	public void testGetSeriesCount() {
		assertEquals(1, ds.getSeriesCount());
	}

	@Test
	public void testGetSeriesKey() {
		assertEquals("value", ds.getSeriesKey(0));
	}

	@Test
	public void testGetItemCount() {
		assertEquals(2, ds.getItemCount(0));
	}

	@Test
	public void testGetX() {
		assertEquals(new Double(crit.getScale().getStart()), ds.getX(0, 0));
		assertEquals(new Double(crit.getScale().getEnd()), ds.getX(0, 1));
	}
	
	@Test
	public void testGetY() {
		assertEquals(new Double(1.0), ds.getY(0, 0));
		assertEquals(new Double(0.0), ds.getY(0, 1));
		
		crit.setAscending(true);
		
		assertEquals(new Double(0.0), ds.getY(0, 0));
		assertEquals(new Double(1.0), ds.getY(0, 1));
	}

	@Test
	public void testResultChangeFires() {
		DatasetChangeListener list = createMock(DatasetChangeListener.class);
		ds.addChangeListener(list);
		list.datasetChanged((DatasetChangeEvent) JUnitUtil.eqEventObject(new DatasetChangeEvent(ds, ds)));
		replay(list);
		crit.setScale(new Interval(0.0, 2.0));
		verify(list);
	}
	
	
}
