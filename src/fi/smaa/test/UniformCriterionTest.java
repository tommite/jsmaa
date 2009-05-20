package fi.smaa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nl.rug.escher.common.JUnitUtil;

import static org.easymock.EasyMock.*;
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
	public void testSample() {
		criterion.setAlternatives(alts);
		//criterion.set
		double[] tgt = new double[1];
	//	c.sample(tgt);
		assertNotNull(tgt[0]);
		assertTrue(tgt[0] >= 0.0);
		assertTrue(tgt[0] <= 1.0);
	}
	
	@Test
	public void testSetIntervals() {
		criterion.setAlternatives(alts);
		Object oldVal = criterion.getIntervals();
		JUnitUtil.testSetter(criterion, UniformCriterion.PROPERTY_INTERVALS, oldVal, meas);
	}
		
	@Test
	public void testGetTypeLabel() {
		assertEquals("Uniform", criterion.getTypeLabel());
	}
	
	@Test
	public void testGetScale() {
		criterion.setAlternatives(alts);
		criterion.setIntervals(meas);
		assertEquals(new Interval(0.0, 1.0), criterion.getScale());
	}
	
	@Test
	public void testSetIntervalsFiresScaleChange() {
		criterion.setAlternatives(alts);
		Interval oldVal = criterion.getScale();
		Interval newVal = new Interval(0.0, 1.0);
		PropertyChangeListener mock = JUnitUtil.mockListener(criterion, CardinalCriterion.PROPERTY_SCALE,
				oldVal, newVal);
		criterion.addPropertyChangeListener(mock);
		criterion.setIntervals(meas);		
		
		verify(mock);
	}
	
	@Test
	public void testIntervalChangeFiresScaleChange() {
		criterion.setAlternatives(alts);
		criterion.setIntervals(meas);
		Interval oldVal = null;
		Interval newVal = new Interval(0.0, 5.0);

		PropertyChangeListener mock = JUnitUtil.mockListener(criterion, CardinalCriterion.PROPERTY_SCALE,
				oldVal, newVal);
		criterion.addPropertyChangeListener(mock);
		
		criterion.getIntervals().get(alts.get(0)).setEnd(5.0);
		
		verify(mock);		
	}
	
	@Test
	public void testUpdateMeasurements() {
		criterion.setAlternatives(alts);
		assertEquals(new Interval(), criterion.getIntervals().get(alts.get(0)));
		assertEquals(new Interval(), criterion.getIntervals().get(alts.get(1)));
	}
	
}
