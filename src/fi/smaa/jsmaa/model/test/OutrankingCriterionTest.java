/*
	This file is part of JSMAA.
	(c) Tommi Tervonen, 2009	

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

package fi.smaa.jsmaa.model.test;

import static org.junit.Assert.*;

import java.beans.PropertyChangeListener;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.JUnitUtil;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.OutrankingCriterion;

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
		Interval ival = new Interval(0.0, 0.1);
		crit.setIndifMeasurement(ival);
		PropertyChangeListener mock = JUnitUtil.mockListener(crit, OutrankingCriterion.PROPERTY_INDIF_MEASUREMENT,
				null, new Interval(0.0, 0.2));
		crit.addPropertyChangeListener(mock);
		ival.setEnd(0.2);
		EasyMock.verify(mock);
	}
	
	
	@Test
	public void testPrefMeasurementChange() {
		Interval ival = new Interval(0.0, 0.1);
		crit.setPrefMeasurement(ival);
		PropertyChangeListener mock = JUnitUtil.mockListener(crit, OutrankingCriterion.PROPERTY_PREF_MEASUREMENT,
				null, new Interval(0.0, 0.2));
		crit.addPropertyChangeListener(mock);
		ival.setEnd(0.2);
		EasyMock.verify(mock);
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
}
