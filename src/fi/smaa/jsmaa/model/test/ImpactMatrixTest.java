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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.common.Interval;
import fi.smaa.jsmaa.common.InvalidIntervalException;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.ImpactMatrix;
import fi.smaa.jsmaa.model.ImpactMatrixListener;
import fi.smaa.jsmaa.model.NoSuchAlternativeException;
import fi.smaa.jsmaa.model.NoSuchCriterionException;
import fi.smaa.jsmaa.model.NoSuchValueException;

public class ImpactMatrixTest {
	
	private ImpactMatrix m;
	private CardinalCriterion c1;
	private CardinalCriterion c2;
	private Alternative a1;
	private Alternative a2;
	private List<Alternative> alts;
	private List<Criterion> crit;
	
	@Before
	public void setUp() {
		c1 = new CardinalCriterion("c1");
		c2 = new CardinalCriterion("c2");
		a1 = new Alternative("a1");
		a2 = new Alternative("a2");
		alts = new ArrayList<Alternative>();
		crit = new ArrayList<Criterion>();
		alts.add(a1);
		alts.add(a2);
		crit.add(c1);
		crit.add(c2);
		
		m = new ImpactMatrix(); 
	}
	
	@Test
	public void testNullConstructor() {
		ImpactMatrix m = new ImpactMatrix();
		assertTrue(m.getAlternatives().isEmpty());
		assertTrue(m.getCriteria().isEmpty());
	}
	
	@Test
	public void testParameterConstructor() {
		List<Alternative> alts = Collections.singletonList(new Alternative("a"));
		List<Criterion> crit = Collections.singletonList((Criterion) new CardinalCriterion("c"));
		
		ImpactMatrix m = new ImpactMatrix(alts, crit);
		assertEquals(alts, m.getAlternatives());
		assertEquals(crit, m.getCriteria());
	}
	
	@Test
	public void testAddAlternative() {
		Alternative a = new Alternative("a");
		m.addAlternative(a);
		assertEquals(Collections.singletonList(a), m.getAlternatives());
		m.addAlternative(a);
		assertEquals(Collections.singletonList(a), m.getAlternatives());		
	}
	
	@Test
	public void testDeleteAlternative() {
		Alternative a = new Alternative("a");
		m.addAlternative(a);
		m.deleteAlternative(a);
		assertEquals(Collections.EMPTY_LIST, m.getAlternatives());
		m.deleteAlternative(a);
		assertEquals(Collections.EMPTY_LIST, m.getAlternatives());		
		m.addAlternative(new Alternative("b"));
		m.deleteAlternative(a);
		assertEquals(1, m.getAlternatives().size());
	}
	
	@Test
	public void testAddCriterion() {
		Criterion c = new CardinalCriterion("c");
		m.addCriterion(c);
		assertEquals(Collections.singletonList(c), m.getCriteria());
		m.addCriterion(c);
		assertEquals(Collections.singletonList(c), m.getCriteria());			
	}
	
	@Test
	public void testAddCriterionSetsMeasurements() throws NoSuchAlternativeException, NoSuchCriterionException {
		CardinalCriterion c = new CardinalCriterion("cx");
		m.setCriteria(crit);
		m.setAlternatives(alts);		
		m.addCriterion(c);
		assertEquals(new Interval(), m.getMeasurement(c, a1));
	}
	
	@Test
	public void testDeleteCriterion() {
		Criterion c = new CardinalCriterion("c");
		m.addCriterion(c);
		m.deleteCriterion(c);
		assertEquals(Collections.EMPTY_LIST, m.getCriteria());
		m.deleteCriterion(c);
		assertEquals(Collections.EMPTY_LIST, m.getCriteria());		
		m.addCriterion(new CardinalCriterion("b"));
		m.deleteCriterion(c);
		assertEquals(1, m.getCriteria().size());		
	}
	
	@Test
	public void testInitialScalesSetCorrectly() {
		assertEquals(new Interval(0.0, 0.0), c1.getScale());
		assertEquals(new Interval(0.0, 0.0), c2.getScale());
	}
	
	@Test
	public void testSetCardinalMeasurement() throws NoSuchValueException {
		m = new ImpactMatrix(alts, crit);
		m.setMeasurement(c1, a1, new Interval(0.0, 0.2));
		assertEquals(new Interval(0.0, 0.2), m.getMeasurement(c1, a1));
	}
	
	@Test
	public void testScalesSetCorrectly() throws NoSuchValueException {
		m = new ImpactMatrix(alts, crit);
		m.setMeasurement(c1, a1, new Interval(0.0, 2.0));
		m.setMeasurement(c1, a2, new GaussianMeasurement(0.0, 1.0));
		Interval s1 = c1.getScale();
		assertEquals(new Interval(-1.96, 2.0), s1);
	}
	
	
	@Test
	public void testMeasurementListenerFires() throws NoSuchValueException {
		ImpactMatrixListener mock = EasyMock.createMock(ImpactMatrixListener.class);
		mock.measurementChanged();
		EasyMock.expectLastCall().times(1);		
		m = new ImpactMatrix(alts, crit);
		Interval meas = new Interval(0.0, 2.0);
		m.setMeasurement(c1, a1, meas);
		m.addListener(mock);
		EasyMock.replay(mock);
		meas.setEnd(3.0);
		EasyMock.verify(mock);
	}
	
	@Test
	public void testCriterionNameChangePropagates() throws NoSuchValueException {
		m.setCriteria(crit);
		m.setAlternatives(alts);
		m.setMeasurement(c1, a1, new Interval(0.0, 1.0));
		c1.setName("cccc");
		assertEquals(new Interval(0.0, 1.0), m.getMeasurement(c1, a1));
	}
	
	@Test
	public void testCriterionNameChangeDoesntFire() {
		ImpactMatrixListener l = EasyMock.createMock(ImpactMatrixListener.class);
		m.setCriteria(crit);
		m.setAlternatives(alts);		
		m.addListener(l);
		EasyMock.replay(l);
		c1.setName("xxx");
		c2.setName("yyy");
		EasyMock.verify(l);
	}
	
	@Test
	public void testCriterionNameChangeMaintainsMeasurements() throws NoSuchAlternativeException, NoSuchCriterionException {
		m.setCriteria(crit);
		m.setAlternatives(alts);
		m.setMeasurement(c1, a1, new Interval());
		m.setMeasurement(c1, a2, new Interval());
		m.setMeasurement(c2, a1, new Interval());
		m.setMeasurement(c2, a2, new Interval());

		assertEquals(new Interval(), m.getMeasurement(c2, a1));
		assertEquals(new Interval(), m.getMeasurement(c2, a2));
		assertEquals(new Interval(), m.getMeasurement(c1, a1));
		assertEquals(new Interval(), m.getMeasurement(c1, a2));		
	}
	
	@Test
	public void testAlternativeNameChangePropagates() throws NoSuchValueException {
		m.setCriteria(crit);
		m.setAlternatives(alts);
		m.setMeasurement(c1, a1, new Interval(0.0, 1.0));
		a1.setName("aaaa");
		assertEquals(new Interval(0.0, 1.0), m.getMeasurement(c1, a1));
	}
	
	@Test
	public void testScalesChangeOnMeasurementChange() throws NoSuchValueException {
		m = new ImpactMatrix(alts, crit);
		Interval meas = new Interval(0.0, 2.0);
		m.setMeasurement(c1, a1, meas);
		meas.setEnd(10.0);
		assertEquals(new Interval(0.0, 10.0), c1.getScale());
	}
	
	@Test
	public void testMeasurementListenerFiresOnSet() throws NoSuchValueException {
		ImpactMatrixListener mock = EasyMock.createMock(ImpactMatrixListener.class);
		mock.measurementTypeChanged();
		m = new ImpactMatrix(alts, crit);
		Interval meas = new Interval(0.0, 2.0);
		m.addListener(mock);
		EasyMock.replay(mock);
		m.setMeasurement(c1, a1, meas);
		EasyMock.verify(mock);
	}
	
	@Test
	public void testEquals() throws NoSuchAlternativeException, NoSuchCriterionException, InvalidIntervalException {
		m = new ImpactMatrix(alts, crit);
		ImpactMatrix m2 = new ImpactMatrix(alts, crit);
		assertTrue(m.equals(m2));
		
		assertFalse(m.equals(null));
		assertFalse(m.equals("a"));
		List<Criterion> s = new ArrayList<Criterion>();
		s.add(c1);
		ImpactMatrix m3 = new ImpactMatrix(alts, s);
		assertFalse(m.equals(m3));
		List<Alternative> s2 = new ArrayList<Alternative>();
		s2.add(a2);
		ImpactMatrix m4 = new ImpactMatrix(s2, crit);
		assertFalse(m.equals(m4));
		
		m2.setMeasurement(c1, a1, new Interval(0.2, 0.3));
		assertFalse(m.equals(m2));		
	}

	@Test
	public void testDeepCopy() throws NoSuchValueException {
		m = new ImpactMatrix(alts, crit);
		m.setMeasurement(c1, a1, new Interval(0.0, 1.0));
		ImpactMatrix m2 = (ImpactMatrix) m.deepCopy();
		
		assertTrue(m.getAlternatives().size() == m2.getAlternatives().size());
		assertTrue(m.getCriteria().size() == m2.getCriteria().size());
		
		assertTrue(m.getAlternatives() != m2.getAlternatives());
		assertTrue(m.getCriteria() != m2.getCriteria());
	}

}
