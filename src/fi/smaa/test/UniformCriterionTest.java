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

package fi.smaa.test;

import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.rug.escher.common.JUnitUtil;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.Alternative;
import fi.smaa.CardinalCriterion;
import fi.smaa.UniformCriterion;
import fi.smaa.common.Interval;

public class UniformCriterionTest {
	
	private UniformCriterion criterion;
	private ArrayList<Alternative> alts;
	private Map<Alternative, Interval> meas;
	
	@Before
	public void setUp() {
		criterion = new UniformCriterion("crit");
		alts = new ArrayList<Alternative>();
		alts.add(new Alternative("alt1"));
		alts.add(new Alternative("alt2"));
		
		meas = new HashMap<Alternative, Interval>();
		meas.put(alts.get(0), new Interval(0.0, 0.5));
		meas.put(alts.get(1), new Interval(0.5, 1.0));
	}

	@Test
	public void testGetTypeLabel() {
		assertEquals("Uniform", criterion.getTypeLabel());
	}
	
	@Test
	public void testGetScale() {
		criterion.setAlternatives(alts);
		criterion.setMeasurements(meas);
		assertEquals(new Interval(0.0, 1.0), criterion.getScale());
	}
	
	@Test
	public void testSetIntervalsFiresScaleChange() {
		criterion.setAlternatives(alts);
		Interval newVal = new Interval(0.0, 1.0);
		PropertyChangeListener mock = JUnitUtil.mockListener(criterion, CardinalCriterion.PROPERTY_SCALE,
				null, newVal);
		criterion.addPropertyChangeListener(mock);
		criterion.setMeasurements(meas);		
		verify(mock);
	}
	
	@Test
	public void testIntervalChangeFiresScaleChange() {
		criterion.setAlternatives(alts);
		criterion.setMeasurements(meas);
		Interval oldVal = null;
		Interval newVal = new Interval(0.0, 5.0);

		PropertyChangeListener mock = JUnitUtil.mockListener(criterion, CardinalCriterion.PROPERTY_SCALE,
				oldVal, newVal);
		criterion.addPropertyChangeListener(mock);
		
		criterion.getMeasurements().get(alts.get(0)).setEnd(5.0);
		
		verify(mock);		
	}
		
}
