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
package fi.smaa.jsmaa.model;

import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.beans.PropertyChangeListener;

import org.drugis.common.JUnitUtil;
import org.junit.Before;
import org.junit.Test;

public class OutrankingCriterionTest {

	private OutrankingCriterion crit;
	
	@Before
	public void setUp() {
		crit = new OutrankingCriterion("crit", true, new Interval(0.0, 0.0),
				new Interval(0.5, 0.5));
	}
	
	@Test
	public void testSetIndifMeasurement() {
		JUnitUtil.testSetter(crit, OutrankingCriterion.PROPERTY_INDIF_MEASUREMENT, 
				new Interval(0.0, 0.0), new Interval(1.0, 1.0));
	}
	
	@Test
	public void testSetPrefMeasurement() {
		JUnitUtil.testSetter(crit, OutrankingCriterion.PROPERTY_PREF_MEASUREMENT,
				new Interval(0.5, 0.5), new Interval(1.0, 1.0));
	}
	
	@Test
	public void testIndifMeasurementChange() {
		Interval ival = (Interval) crit.getIndifMeasurement();
		PropertyChangeListener mock = JUnitUtil.mockListener(crit, OutrankingCriterion.PROPERTY_INDIF_MEASUREMENT,
				null, new Interval(0.0, 0.2));
		crit.addPropertyChangeListener(mock);
		ival.setEnd(0.2);
		verify(mock);
	}
	
	
	@Test
	public void testPrefMeasurementChange() {
		Interval ival = (Interval) crit.getPrefMeasurement();
		PropertyChangeListener mock = JUnitUtil.mockListener(crit, OutrankingCriterion.PROPERTY_PREF_MEASUREMENT,
				null, new Interval(0.5, 1.0));
		crit.addPropertyChangeListener(mock);
		ival.setEnd(1.0);
		verify(mock);
	}	
	
	@Test
	public void testGetTypeLabel() {
		assertEquals("Outranking", crit.getTypeLabel());
	}
	
	@Test
	public void testDeepCopy() {
		OutrankingCriterion c = crit.deepCopy();
		assertEquals(crit.getIndifMeasurement(), c.getIndifMeasurement());
		assertEquals(crit.getPrefMeasurement(), c.getPrefMeasurement());
		assertEquals(crit.getName(), c.getName());
		assertEquals(crit.getAscending(), c.getAscending());
	}
	
	@Test
	public void testSerializationReconnectsPrefListener() throws Exception {
		OutrankingCriterion c = JUnitUtil.serializeObject(crit);
		
		Interval ival = (Interval) c.getPrefMeasurement();
		PropertyChangeListener mock = JUnitUtil.mockListener(c, OutrankingCriterion.PROPERTY_PREF_MEASUREMENT, 
				null, new Interval(0.5, 1.0));		
		c.addPropertyChangeListener(mock);
		ival.setEnd(1.0);
		verify(mock);
	}
	
	@Test
	public void testSerializationReconnectsIndifListener() throws Exception {
		OutrankingCriterion c = JUnitUtil.serializeObject(crit);
		
		Interval ival = (Interval) c.getIndifMeasurement();
		PropertyChangeListener mock = JUnitUtil.mockListener(c, OutrankingCriterion.PROPERTY_INDIF_MEASUREMENT, 
				null, new Interval(0.0, 1.0));		
		c.addPropertyChangeListener(mock);
		ival.setEnd(1.0);
		verify(mock);
	}
	
	
}
