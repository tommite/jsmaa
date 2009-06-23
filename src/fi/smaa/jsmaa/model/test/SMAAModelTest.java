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
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import nl.rug.escher.common.JUnitUtil;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.AlternativeExistsException;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.MissingPreferenceInformation;
import fi.smaa.jsmaa.model.NoSuchAlternativeException;
import fi.smaa.jsmaa.model.NoSuchCriterionException;
import fi.smaa.jsmaa.model.NoSuchValueException;
import fi.smaa.jsmaa.model.OrdinalPreferenceInformation;
import fi.smaa.jsmaa.model.Rank;
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
		List<Alternative> alts = new ArrayList<Alternative>();
		alts.add(new Alternative("alt1"));
		alts.add(new Alternative("alt2"));
		
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);
		mock.alternativesChanged();
		mock.measurementsChanged();
		replay(mock);
		model.setAlternatives(alts);
		verify(mock);
		
		assertEquals(alts, model.getAlternatives());
		
		assertEquals(alts, model.getImpactMatrix().getAlternatives());
	}
	
	@Test
	public void testSetCriteria() {
		List<Criterion> crit = new ArrayList<Criterion>();
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
	public void testSetPreferenceInformationConnectsListener() {
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		List<Rank> ranks = new ArrayList<Rank>();
		Rank r1 = new Rank(1);
		ranks.add(r1);
		ranks.add(new Rank(2));
		model.setPreferenceInformation(new OrdinalPreferenceInformation(ranks));		
		model.addModelListener(mock);
		mock.preferencesChanged();
		expectLastCall().anyTimes();
		replay(mock);
		r1.setRank(2);
		verify(mock);
	}
	
	@Test
	public void testAddAlternative() throws AlternativeExistsException, NoSuchAlternativeException, NoSuchCriterionException {
		List<Alternative> alts = new ArrayList<Alternative>();
		Alternative a1 = new Alternative("alt1");
		alts.add(a1);
		
		CardinalCriterion c = new CardinalCriterion("crit");
		List<Criterion> crit = new ArrayList<Criterion>();
		crit.add(c);
		model.setCriteria(crit);
		model.setAlternatives(alts);
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);		
		mock.alternativesChanged();
		mock.measurementTypeChanged();
		mock.measurementsChanged();
		expectLastCall().anyTimes();
		replay(mock);
		
		List<Alternative> alts2 = new ArrayList<Alternative>();
		alts2.add(a1);
		Alternative a2 = new Alternative("alt2");
		Alternative alt2 = a2;
		alts2.add(alt2);		
		model.addAlternative(a2);
		verify(mock);
		
		assertEquals(alts2, model.getAlternatives());
		
		assertNotNull(model.getImpactMatrix().getMeasurement(c, alt2));
	}
	
	@Test
	public void testToString() {
		SMAAModel mod = new SMAAModel("testModel");
		assertEquals("testModel", mod.toString());
	}
	
	@Test
	public void testAddCriterion() {
		Set<Criterion> crit = new HashSet<Criterion>();
		CardinalCriterion c1 = new CardinalCriterion("c1");
		crit.add(c1);
		
		model.setCriteria(crit);
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);		
		mock.criteriaChanged();
		mock.preferencesChanged();
		replay(mock);
		
		List<Criterion> crit2 = new ArrayList<Criterion>();
		crit2.add(c1);
		CardinalCriterion c2 = new CardinalCriterion("c2");
		crit2.add(c2);		
		model.addCriterion(c2);
		verify(mock);
		
		assertEquals(crit2, model.getCriteria());			
	}
	
	@Test
	public void testDeleteAlternative() throws Exception {
		Set<Alternative> alts = new HashSet<Alternative>();
		Alternative a1 = new Alternative("alt1");
		alts.add(a1);
		Alternative a2 = new Alternative("alt2");
		alts.add(a2);				
		model.setAlternatives(alts);
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);		
		mock.alternativesChanged();
		mock.measurementsChanged();
		replay(mock);
		
		List<Alternative> alts2 = new ArrayList<Alternative>();
		alts2.add(a1);

		model.deleteAlternative(a2);
		verify(mock);
		
		assertEquals(alts2, model.getAlternatives());		
	}
	
	@Test
	public void testDeleteCriterion() throws Exception {
		Set<Criterion> crit = new HashSet<Criterion>();
		CardinalCriterion c1 = new CardinalCriterion("c1");
		crit.add(c1);
		CardinalCriterion c2 = new CardinalCriterion("c2");
		crit.add(c2);		
		
		model.setCriteria(crit);
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);		
		mock.criteriaChanged();
		mock.preferencesChanged();
		replay(mock);
		
		List<Criterion> crit2 = new ArrayList<Criterion>();
		crit2.add(c1);
		model.deleteCriterion(c2);
		verify(mock);
		
		assertEquals(crit2, model.getCriteria());	
	}
	
	@Test
	public void testDeepCopy() throws AlternativeExistsException, NoSuchValueException {
		setupModel();
		
		SMAAModel model2 = model.deepCopy();
	
		assertEquals(model.getName(), model2.getName());
		assertEquals(model.getAlternatives().size(), model2.getAlternatives().size());
		assertEquals(model.getCriteria().size(), model2.getCriteria().size());
		
		assertFalse(model.getAlternatives() == model2.getAlternatives());
		assertFalse(model.getCriteria() == model2.getCriteria());
		assertFalse(model.getPreferenceInformation() == model2.getPreferenceInformation());
		assertFalse(model.getImpactMatrix() == model2.getImpactMatrix());
	}

	private void setupModel() throws AlternativeExistsException,
			NoSuchAlternativeException, NoSuchCriterionException {
		Alternative a1 = new Alternative("a1");
		Alternative a2 = new Alternative("a2");
		CardinalCriterion c1 = new CardinalCriterion("c1");
		CardinalCriterion c2 = new CardinalCriterion("c2");
		model.addAlternative(a1);
		model.addAlternative(a2);
		model.addCriterion(c1);
		model.addCriterion(c2);
		model.getImpactMatrix().setMeasurement(c1, a1, new Interval(0.0, 6.0));
	}
	
	@Test
	public void testEquals() {
		SMAAModel model2 = model.deepCopy();
		assertEquals(model, model2);
	}
	
	@Test
	public void testSerialization() throws NoSuchAlternativeException, NoSuchCriterionException, AlternativeExistsException, IOException, ClassNotFoundException {
		setupModel();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oout = new ObjectOutputStream(bos);
		oout.writeObject(model);
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		SMAAModel newModel = (SMAAModel) in.readObject();
		assertEquals(model.getAlternatives().size(), newModel.getAlternatives().size());
		assertEquals(model.getCriteria().size(), newModel.getCriteria().size());
	}
	
	@Test
	public void testSerializationHooksPreferenceListeners() throws Exception{
		setupModel();
		List<Rank> ranks = new ArrayList<Rank>();
		ranks.add(new Rank(2));
		ranks.add(new Rank(1));
		model.setPreferenceInformation(new OrdinalPreferenceInformation(ranks));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oout = new ObjectOutputStream(bos);
		oout.writeObject(model);
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		SMAAModel newModel = (SMAAModel) in.readObject();
		
		SMAAModelListener l = createMock(SMAAModelListener.class);
		newModel.addModelListener(l);		
		l.preferencesChanged();
		expectLastCall().anyTimes();
		replay(l);
		OrdinalPreferenceInformation pref = (OrdinalPreferenceInformation) newModel.getPreferenceInformation();
		pref.getRanks().get(0).setRank(1);
		verify(l);
	}
	
	@Test
	public void testSerializationHooksOrdinalChange() throws Exception{
		setupModel();
		List<Rank> ranks = new ArrayList<Rank>();
		ranks.add(new Rank(2));
		ranks.add(new Rank(1));
		model.setPreferenceInformation(new OrdinalPreferenceInformation(ranks));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oout = new ObjectOutputStream(bos);
		oout.writeObject(model);
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		SMAAModel newModel = (SMAAModel) in.readObject();
		
		OrdinalPreferenceInformation pref = (OrdinalPreferenceInformation) newModel.getPreferenceInformation();
		pref.getRanks().get(0).setRank(1);
		assertEquals(new Rank(1), pref.getRanks().get(0));
		assertEquals(new Rank(2), pref.getRanks().get(1));		
	}	
	
	@Test
	public void testSerializationHooksCriteriaListeners() throws Exception{
		setupModel();
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oout = new ObjectOutputStream(bos);
		oout.writeObject(model);
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		SMAAModel newModel = (SMAAModel) in.readObject();
		
		SMAAModelListener l = createMock(SMAAModelListener.class);
		newModel.addModelListener(l);		
		l.measurementsChanged();
		replay(l);
		CardinalCriterion c = (CardinalCriterion) newModel.getCriteria().get(0);
		c.setAscending(false);
		verify(l);
	}	
	
	@Test
	public void testChangeCritNameDoesntFire() throws Exception {
		setupModel();
		SMAAModelListener l = createMock(SMAAModelListener.class);
		model.addModelListener(l);
		replay(l);
		Criterion c = model.getCriteria().get(0);
		c.setName("ccc");
		verify(l);
	}
}
