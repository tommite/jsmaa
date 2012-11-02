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
import fi.smaa.jsmaa.model.InvalidValuePointException;
import fi.smaa.jsmaa.model.Point2D;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class UtilityFunctionDatasetTest {

	private ScaleCriterion crit;
	private UtilityFunctionDataset ds;

	@Before
	public void setUp() throws InvalidValuePointException {
		crit = new ScaleCriterion("crit", false);
		crit.setScale(new Interval(0.0, 1.0));
		crit.addValuePoint(new Point2D(0.2, 0.5));
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
		assertEquals(3, ds.getItemCount(0));
	}

	@Test
	public void testGetX() {
		assertEquals(new Double(crit.getScale().getStart()), ds.getX(0, 0));
		assertEquals(new Double(0.2), ds.getX(0,  1)); 
		assertEquals(new Double(crit.getScale().getEnd()), ds.getX(0, 2));
	}
	
	@Test
	public void testGetY() {
		assertEquals(new Double(1.0), ds.getY(0, 0));
		assertEquals(new Double(0.5), ds.getY(0, 1)); 
		assertEquals(new Double(0.0), ds.getY(0, 2));
		
		crit.setAscending(true);
		
		assertEquals(new Double(0.0), ds.getY(0, 0));
		assertEquals(new Double(1.0), ds.getY(0, 1));
	}

	@Test
	public void testResultChangeFires() {
		DatasetChangeListener list = createMock(DatasetChangeListener.class);
		ds.addChangeListener(list);
		list.datasetChanged((DatasetChangeEvent) JUnitUtil.eqEventObject(new DatasetChangeEvent(ds, ds)));
		list.datasetChanged((DatasetChangeEvent) JUnitUtil.eqEventObject(new DatasetChangeEvent(ds, ds)));
		replay(list);
		crit.setScale(new Interval(0.0, 2.0));
		verify(list);
	}
	
	
}
