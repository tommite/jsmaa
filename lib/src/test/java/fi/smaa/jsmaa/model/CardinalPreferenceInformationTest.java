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
import static org.junit.Assert.assertTrue;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.drugis.common.JUnitUtil;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import fi.smaa.common.RandomUtil;
import fi.smaa.jsmaa.simulator.IterationException;

public class CardinalPreferenceInformationTest {
	
	private CardinalPreferenceInformation info;
	private ScaleCriterion crit;
	private RandomUtil random;
	@Before
	public void setUp() {
		random = new RandomUtil();
		crit = new ScaleCriterion("c");
		List<Criterion> list = new ArrayList<Criterion>();
		list.add(crit);
		info = new CardinalPreferenceInformation(list);
	}
	
	@Test
	public void testGetCriteria() {
		assertEquals(crit, info.getCriteria().get(0));
	}
	
	@Test
	public void testSetMeasurement() {
		PropertyChangeListener list = JUnitUtil.mockListener(info, CardinalPreferenceInformation.PREFERENCES, null, null);
		info.addPropertyChangeListener(list);
		info.setMeasurement(crit, new ExactMeasurement(1.0));
		assertEquals(new ExactMeasurement(1.0), info.getMeasurement(crit));
		verify(list);
	}
	
	@Test
	public void testMeasurementChangeFires() {
		PropertyChangeListener list = JUnitUtil.mockListener(info, CardinalPreferenceInformation.PREFERENCES, null, null);
		ExactMeasurement m = new ExactMeasurement(2.0);
		info.setMeasurement(crit, m);
		info.addPropertyChangeListener(list);		
		m.setValue(3.0);
		verify(list);
	}
	
	@Test
	public void testInitialMeasurementsFire() {
		PropertyChangeListener list = JUnitUtil.mockListener(info, CardinalPreferenceInformation.PREFERENCES, null, null);
		info.addPropertyChangeListener(list);
		((ExactMeasurement) info.getMeasurement(crit)).setValue(1.0);
		verify(list);
	}
	
	@Test(expected=IterationException.class)
	public void testSampleOverUppberBoundWeightsThrows() throws IterationException {
		info.setMeasurement(crit, new Interval(0.0, 0.4));
		info.sampleWeights(random);
	}
	
	@Test(expected=IterationException.class)
	public void testSampleInfeasibleWeightsThrows() throws IterationException {
		info.setMeasurement(crit, new ExactMeasurement(0.2));
		info.sampleWeights(random);
	}	
	
	@Test
	public void testSampleWeights() throws Exception {
		info.setMeasurement(crit, new ExactMeasurement(1.0));
		double[] w = info.sampleWeights(random);
		assertEquals(1, w.length);
		assertEquals(1.0, w[0], 0.0000001);
	}
	
	@Test
	public void testSampleBothExactAndInterval() throws Exception {
		List<Criterion> list = new ArrayList<Criterion>();
		ScaleCriterion c1 = new ScaleCriterion("c1");
		list.add(c1);
		ScaleCriterion c2 = new ScaleCriterion("c2");
		list.add(c2);
		info = new CardinalPreferenceInformation(list);
		info.setMeasurement(c1, new Interval(0.0, 1.0));
		info.setMeasurement(c2, new ExactMeasurement(0.2));
		double[] w = info.sampleWeights(random);
		assertEquals(0.8, w[0], 0.000001);
		assertEquals(0.2, w[1], 0.000001);
	}
	
	@Test
	public void testDeepCopy() {
		info.setMeasurement(crit, new ExactMeasurement(1.0));
		CardinalPreferenceInformation info2 = info.deepCopy();
		
		assertEquals(1, info2.getCriteria().size());
		assertTrue(info2.getCriteria().get(0)instanceof ScaleCriterion);
		assertEquals("c", info2.getCriteria().get(0).getName());
		assertEquals(new ExactMeasurement(1.0), info2.getMeasurement(info.getCriteria().get(0)));
		
		PropertyChangeListener list = JUnitUtil.mockListener(info2, CardinalPreferenceInformation.PREFERENCES, null, null);
		info2.addPropertyChangeListener(list);		
		((ExactMeasurement) info2.getMeasurement(info2.getCriteria().get(0))).setValue(3.0);
		verify(list);		
	}	
	
	@Ignore
	@Test
	public void testSerializationConnectsListeners() throws Exception {
		CardinalPreferenceInformation i2 = JUnitUtil.serializeObject(info);
		PropertyChangeListener list = JUnitUtil.mockListener(i2, CardinalPreferenceInformation.PREFERENCES, null, null);
		i2.addPropertyChangeListener(list);
		((ExactMeasurement)i2.getMeasurement(i2.getCriteria().get(0))).setValue(2.0);
		verify(list);
	}
	
	@Test
	public void testSmaaTRIPaperWeightConstraints() throws Exception {
		ScaleCriterion g11 = new ScaleCriterion("g11");
		ScaleCriterion g12 = new ScaleCriterion("g12");
		ScaleCriterion g13 = new ScaleCriterion("g13");
		ScaleCriterion g14 = new ScaleCriterion("g14");
		ScaleCriterion g15 = new ScaleCriterion("g15");
		ScaleCriterion g21 = new ScaleCriterion("g21");
		ScaleCriterion g22 = new ScaleCriterion("g22");
		ScaleCriterion g23 = new ScaleCriterion("g23");
		ScaleCriterion g24 = new ScaleCriterion("g24");
		ScaleCriterion g251 = new ScaleCriterion("g251");
		List<Criterion> crit = new ArrayList<Criterion>();
		crit.add(g11);
		crit.add(g12);
		crit.add(g13);
		crit.add(g14);
		crit.add(g15);
		crit.add(g21);
		crit.add(g22);
		crit.add(g23);
		crit.add(g24);
		crit.add(g251);
		info = new CardinalPreferenceInformation(crit);
		info.setMeasurement(g11, new Interval(0.06, 0.15));
		info.setMeasurement(g12, new Interval(0.0, 0.04));
		info.setMeasurement(g13, new Interval(0.0, 0.04));
		info.setMeasurement(g14, new Interval(0.0, 0.04));
		info.setMeasurement(g15, new Interval(0.06, 0.15));
		info.setMeasurement(g21, new Interval(0.0, 0.04));
		info.setMeasurement(g22, new Interval(0.0, 0.04));
		info.setMeasurement(g23, new Interval(0.33, 0.54));
		info.setMeasurement(g24, new Interval(0.0, 0.04));
		info.setMeasurement(g251, new Interval(0.15, 0.28));
		
		double[] w = info.sampleWeights(random);
		assertTrue(w[0] >= 0.06);
		assertTrue(w[0] <= 0.15);
		
		assertTrue(w[1] >= 0.0);
		assertTrue(w[1] <= 0.04);

		assertTrue(w[2] >= 0.0);
		assertTrue(w[2] <= 0.04);

		assertTrue(w[3] >= 0.0);
		assertTrue(w[3] <= 0.04);
		
		assertTrue(w[4] >= 0.06);
		assertTrue(w[4] <= 0.15);
		
		assertTrue(w[5] >= 0.0);
		assertTrue(w[5] <= 0.04);

		assertTrue(w[6] >= 0.0);
		assertTrue(w[6] <= 0.04);

		assertTrue(w[7] >= 0.33);
		assertTrue(w[7] <= 0.54);
		
		assertTrue(w[8] >= 0.0);
		assertTrue(w[8] <= 0.04);

		assertTrue(w[9] >= 0.15);
		assertTrue(w[9] <= 0.28);
	}
	
	@Test
	public void testGetMeasurement() {
		assertTrue(info.getMeasurement(crit) instanceof ExactMeasurement);
	}
}
