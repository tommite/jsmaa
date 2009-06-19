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

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;
import java.util.Set;

import nl.rug.escher.common.JUnitUtil;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.common.Interval;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.AlternativeExistsException;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.MissingPreferenceInformation;
import fi.smaa.jsmaa.model.NoSuchValueException;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.SMAAModelListener;

public class SMAAModelTest {
	
	private SMAAModel model;
	
	@Before
	public void setUp() {
		model = new SMAAModel("test");
	}

	@Test
	public void testSetName() {
		JUnitUtil.testSetter(model, SMAAModel.PROPERTY_NAME, "test", "modelName");
	}
	
	@Test
	public void testSetAlternatives() {
		Set<Alternative> alts = new HashSet<Alternative>();
		alts.add(new Alternative("alt1"));
		alts.add(new Alternative("alt2"));
		
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);
		mock.alternativesChanged();

		replay(mock);
		
		model.setAlternatives(alts);
		verify(mock);
		
		assertEquals(alts, model.getAlternatives());
		
		assertEquals(alts, model.getImpactMatrix().getAlternatives());
	}
	
	@Test
	public void testSetCriteria() {
		Set<Criterion> crit = new HashSet<Criterion>();
		crit.add(new CardinalCriterion("c1"));
		crit.add(new CardinalCriterion("c2"));
		
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);		
		mock.preferencesChanged();
		mock.criteriaChanged();
		replay(mock);
		
		model.setCriteria(crit);
		verify(mock);

		assertEquals(crit, model.getCriteria());
		assertEquals(crit, model.getImpactMatrix().getCriteria());		
	}
	
	@Test
	public void testSetPreferenceInformation() {
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);		
		mock.preferencesChanged();
		replay(mock);
		MissingPreferenceInformation pref = new MissingPreferenceInformation(model.getAlternatives().size());
		model.setPreferenceInformation(pref);
		verify(mock);
		assertEquals(pref, model.getPreferenceInformation());
	}
	
	@Test
	public void testAddAlternative() throws AlternativeExistsException {
		Set<Alternative> alts = new HashSet<Alternative>();
		alts.add(new Alternative("alt1"));
		
		model.setAlternatives(alts);
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);		
		mock.alternativesChanged();
		replay(mock);
		
		Set<Alternative> alts2 = new HashSet<Alternative>();
		alts2.add(new Alternative("alt1"));
		alts2.add(new Alternative("alt2"));		
		model.addAlternative(new Alternative("alt2"));
		verify(mock);
		
		assertEquals(alts2, model.getAlternatives());		
	}
	
	@Test
	public void testToString() {
		SMAAModel mod = new SMAAModel("testModel");
		assertEquals("testModel", mod.toString());
	}
	
	@Test
	public void testAddCriterion() {
		Set<Criterion> crit = new HashSet<Criterion>();
		crit.add(new CardinalCriterion("c1"));
		
		model.setCriteria(crit);
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);		
		mock.criteriaChanged();
		mock.preferencesChanged();
		replay(mock);
		
		Set<Criterion> crit2 = new HashSet<Criterion>();
		crit2.add(new CardinalCriterion("c1"));
		crit2.add(new CardinalCriterion("c2"));		
		model.addCriterion(new CardinalCriterion("c2"));
		verify(mock);
		
		assertEquals(crit2, model.getCriteria());			
	}
	
	@Test
	public void testDeleteAlternative() throws Exception {
		Set<Alternative> alts = new HashSet<Alternative>();
		alts.add(new Alternative("alt1"));
		alts.add(new Alternative("alt2"));				
		model.setAlternatives(alts);
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);		
		mock.alternativesChanged();
		replay(mock);
		
		Set<Alternative> alts2 = new HashSet<Alternative>();
		alts2.add(new Alternative("alt1"));

		model.deleteAlternative(new Alternative("alt2"));
		verify(mock);
		
		assertEquals(alts2, model.getAlternatives());		
	}
	
	@Test
	public void testDeleteCriterion() throws Exception {
		Set<Criterion> crit = new HashSet<Criterion>();
		crit.add(new CardinalCriterion("c1"));
		crit.add(new CardinalCriterion("c2"));		
		
		model.setCriteria(crit);
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);		
		mock.criteriaChanged();
		mock.preferencesChanged();
		replay(mock);
		
		Set<Criterion> crit2 = new HashSet<Criterion>();
		crit2.add(new CardinalCriterion("c1"));
		model.deleteCriterion(new CardinalCriterion("c2"));
		verify(mock);
		
		assertEquals(crit2, model.getCriteria());	
	}
	
	@Test
	public void testDeepCopy() throws AlternativeExistsException, NoSuchValueException {
		Alternative a1 = new Alternative("a1");
		Alternative a2 = new Alternative("a2");
		CardinalCriterion c1 = new CardinalCriterion("c1");
		CardinalCriterion c2 = new CardinalCriterion("c2");
		model.addAlternative(a1);
		model.addAlternative(a2);
		model.addCriterion(c1);
		model.addCriterion(c2);
		model.getImpactMatrix().setMeasurement(c1, a1, new Interval(0.0, 6.0));
		
		SMAAModel model2 = model.deepCopy();
	
		assertEquals(model.getName(), model2.getName());
		assertEquals(model.getAlternatives(), model2.getAlternatives());
		assertEquals(model.getCriteria(), model2.getCriteria());
		assertEquals(model.getPreferenceInformation(), model2.getPreferenceInformation());
		assertEquals(model.getImpactMatrix(), model2.getImpactMatrix());
		
		assertFalse(model.getAlternatives() == model2.getAlternatives());
		assertFalse(model.getCriteria() == model2.getCriteria());
		assertFalse(model.getPreferenceInformation() == model2.getPreferenceInformation());
		assertFalse(model.getImpactMatrix() == model2.getImpactMatrix());
	}
}
