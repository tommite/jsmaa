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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.drugis.common.JUnitUtil;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class ImpactMatrixTest {
	
	private ImpactMatrix m;
	private ScaleCriterion c1;
	private ScaleCriterion c2;
	private Alternative a1;
	private Alternative a2;
	private List<Alternative> alts;
	private List<Criterion> crit;
	
	@Before
	public void setUp() {
		c1 = new ScaleCriterion("c1");
		c2 = new ScaleCriterion("c2");
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
		FullJointMeasurements m = new ImpactMatrix();
		assertTrue(m.getAlternatives().isEmpty());
		assertTrue(m.getCriteria().isEmpty());
	}
	
	@Test
	public void testParameterConstructor() {
		List<Alternative> alts = Collections.singletonList(new Alternative("a"));
		List<Criterion> crit = Collections.singletonList((Criterion) new ScaleCriterion("c"));
		
		FullJointMeasurements m = new ImpactMatrix(alts, crit);
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
		Criterion c = new ScaleCriterion("c");
		m.addCriterion(c);
		assertEquals(Collections.singletonList(c), m.getCriteria());
		m.addCriterion(c);
		assertEquals(Collections.singletonList(c), m.getCriteria());			
	}
	
	@Test
	public void testAddCriterionSetsMeasurements() {
		ScaleCriterion c = new ScaleCriterion("cx");
		m.addAlternative(a1);
		m.addCriterion(c);
		assertEquals(new Interval(), m.getMeasurement(c, a1));
	}
	
	@Test
	public void testDeleteCriterion() {
		Criterion c = new ScaleCriterion("c");
		m.addCriterion(c);
		m.deleteCriterion(c);
		assertEquals(Collections.EMPTY_LIST, m.getCriteria());
		m.deleteCriterion(c);
		assertEquals(Collections.EMPTY_LIST, m.getCriteria());		
		m.addCriterion(new ScaleCriterion("b"));
		m.deleteCriterion(c);
		assertEquals(1, m.getCriteria().size());		
	}
	
	@Test
	public void testInitialScalesSetCorrectly() {
		assertEquals(new Interval(0.0, 0.0), c1.getScale());
		assertEquals(new Interval(0.0, 0.0), c2.getScale());
	}
	
	@Test
	public void testSetCardinalMeasurement() {
		m = new ImpactMatrix(alts, crit);
		m.setMeasurement(c1, a1, new Interval(0.0, 0.2));
		assertEquals(new Interval(0.0, 0.2), m.getMeasurement(c1, a1));
	}
	
	@Test
	public void testSetBaseline() {
		m = new ImpactMatrix(alts, crit);
		BaselineGaussianMeasurement meas = new BaselineGaussianMeasurement(1.0, 2.0);
		m.setBaseline(c1, meas);
		assertEquals(meas, m.getBaseline(c1));
	}
	
	@Test
	public void testScalesSetCorrectly() {
		m = new ImpactMatrix(alts, crit);
		m.setMeasurement(c1, a1, new Interval(0.0, 2.0));
		m.setMeasurement(c1, a2, new GaussianMeasurement(0.0, 1.0));
		Interval s1 = c1.getScale();
		assertEquals(new Interval(-1.96, 2.0), s1);
	}
	
	
	@Test
	public void testMeasurementListenerFires() {
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
	public void testCriterionNameChangePropagates() {
		m.addAlternative(a1);
		m.addCriterion(c1);
		m.setMeasurement(c1, a1, new Interval(0.0, 1.0));
		c1.setName("cccc");
		assertEquals(new Interval(0.0, 1.0), m.getMeasurement(c1, a1));
	}
	
	@Test
	public void testCriterionNameChangeDoesntFire() {
		ImpactMatrixListener l = EasyMock.createMock(ImpactMatrixListener.class);
		m.addCriterion(c1);
		m.addCriterion(c2);
		m.addListener(l);
		EasyMock.replay(l);
		c1.setName("xxx");
		c2.setName("yyy");
		EasyMock.verify(l);
	}
	
	@Test
	public void testCriterionNameChangeMaintainsMeasurements() {
		m.addAlternative(a1);
		m.addAlternative(a2);
		m.addCriterion(c1);
		m.addCriterion(c2);
		
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
	public void testAlternativeNameChangePropagates() {
		m.addCriterion(c1);
		m.addAlternative(a1);
		m.setMeasurement(c1, a1, new Interval(0.0, 1.0));
		a1.setName("aaaa");
		assertEquals(new Interval(0.0, 1.0), m.getMeasurement(c1, a1));
	}
	
	@Test
	public void testScalesChangeOnMeasurementChange() {
		m = new ImpactMatrix(alts, crit);
		Interval meas = new Interval(0.0, 2.0);
		m.setMeasurement(c1, a1, meas);
		meas.setEnd(10.0);
		assertEquals(new Interval(0.0, 10.0), c1.getScale());
	}
	
	@Test
	public void testMeasurementListenerFiresOnSet() {
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
	public void testEquals() {
		m = new ImpactMatrix(alts, crit);
		ImpactMatrix m2 = new ImpactMatrix(alts, crit);
		assertTrue(m.equals(m2));
		
		assertFalse(m.equals(null));
		assertFalse(m.equals("a"));
		List<Criterion> s = new ArrayList<Criterion>();
		s.add(c1);
		FullJointMeasurements m3 = new ImpactMatrix(alts, s);
		assertFalse(m.equals(m3));
		List<Alternative> s2 = new ArrayList<Alternative>();
		s2.add(a2);
		FullJointMeasurements m4 = new ImpactMatrix(s2, crit);
		assertFalse(m.equals(m4));
		
		m2.setMeasurement(c1, a1, new Interval(0.2, 0.3));
		assertFalse(m.equals(m2));
		
		m2 = new ImpactMatrix(alts, crit);
		m2.setBaseline(c1, new BaselineGaussianMeasurement(1.0, 3.0));
		assertFalse(m.equals(m2));
	}

	@Test
	public void testDeepCopy() {
		m = new ImpactMatrix(alts, crit);
		m.setMeasurement(c1, a1, new Interval(0.0, 1.0));
		OrdinalCriterion ordCrit = new OrdinalCriterion("ord");
		m.addCriterion(ordCrit);
		m.setMeasurement(ordCrit, a1, new Rank(2));
		BaselineGaussianMeasurement base = new BaselineGaussianMeasurement(1.0, 0.5);
		m.setBaseline(c1, base);
		
		ImpactMatrix m2 = (ImpactMatrix) m.deepCopy(m.getCriteria(), m.getAlternatives());
		
		assertTrue(m.getAlternatives().size() == m2.getAlternatives().size());
		assertTrue(m.getCriteria().size() == m2.getCriteria().size());
		
		Alternative na1 = m2.getAlternatives().get(0);
		Alternative na2 = m2.getAlternatives().get(1);
		Criterion nc1 = m2.getCriteria().get(0);
		Criterion nc2 = m2.getCriteria().get(1);		
		Criterion nc3 = m2.getCriteria().get(2);		
		
		assertEquals(a1.getName(), na1.getName());
		assertEquals(a2.getName(), na2.getName());
		assertEquals(c1.getName(), nc1.getName());
		assertEquals(c2.getName(), nc2.getName());
		assertEquals(ordCrit.getName(), nc3.getName());
		assertEquals(c1.getClass(), nc1.getClass());
		assertEquals(c2.getClass(), nc2.getClass());
		assertEquals(ordCrit.getClass(), nc3.getClass());
		
		Interval ival = (Interval) m2.getMeasurement(nc1, na1);
		assertEquals(new Interval(0.0, 1.0), ival);
		
		assertEquals(new Rank(2), m2.getMeasurement(nc3, na1));
		assertEquals(base, m2.getBaseline(nc1));
		assertEquals(new BaselineGaussianMeasurement(), m2.getBaseline(nc2));
		assertEquals(null, m2.getBaseline(nc3));
	}
	
	@Test
	public void testSetCriteriaKeepsMeasurements() throws Exception {
		crit = new ArrayList<Criterion>();
		crit.add(c1);
		m = new ImpactMatrix(alts, crit);
		LogNormalMeasurement meas = new LogNormalMeasurement(1.0, 0.2);
		m.setMeasurement(c1, a1, meas);
		m.addCriterion(c2);
		assertEquals(meas, m.getMeasurement(c1, a1));
	}

	@Test
	public void testAddOrdinalCriterionAddsCorrectRanks() {
		m.addAlternative(a1);
		m.addAlternative(a2);
		OrdinalCriterion o = new OrdinalCriterion("ord");
		m.addCriterion(o);
		assertEquals(new Rank(1), m.getMeasurement(o, a1));
		assertEquals(new Rank(2), m.getMeasurement(o, a2));
	}
	
	@Test
	public void testAddAlternativeAddOneOnOrdinalCriterion() {
		m.addAlternative(a1);
		m.addAlternative(a2);
		OrdinalCriterion o = new OrdinalCriterion("ord");
		m.addCriterion(o);
		Alternative a3 = new Alternative("a3");
		m.addAlternative(a3);
		assertEquals(new Rank(3), m.getMeasurement(o, a3));
	}	
	
	@Test
	public void testDeleteAlternativeRemovesRank() {
		m.addAlternative(a1);
		m.addAlternative(a2);
		OrdinalCriterion o = new OrdinalCriterion("ord");
		m.addCriterion(o);
		Rank r = (Rank) m.getMeasurement(o, a2);
		assertEquals(new Integer(2), r.getRank());		
		m.deleteAlternative(a1);
		assertEquals(new Integer(1), r.getRank());
	}
	
	@Test
	public void testSerializationConnectsRankListeners() throws Exception {
		m.addAlternative(a1);
		m.addAlternative(a2);
		OrdinalCriterion o = new OrdinalCriterion("ord");
		m.addCriterion(o);
		
		Rank oldR1 = (Rank) m.getMeasurement(o, a1);
		oldR1.setRank(1);
		ImpactMatrix nm = JUnitUtil.serializeObject(m);
		Rank r1 = (Rank) nm.getMeasurement(nm.getCriteria().get(0), nm.getAlternatives().get(0));
		Rank r2 = (Rank) nm.getMeasurement(nm.getCriteria().get(0), nm.getAlternatives().get(1));
		
		r1.setRank(2);
		assertEquals(new Integer(1), r2.getRank());
	}
}
