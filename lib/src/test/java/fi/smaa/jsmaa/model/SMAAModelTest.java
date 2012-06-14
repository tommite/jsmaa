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
package fi.smaa.jsmaa.model;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.drugis.common.JUnitUtil;
import org.junit.Before;
import org.junit.Test;

public class SMAAModelTest {
	
	private SMAAModel model;
	private Alternative a1;
	private Alternative a2;
	private ScaleCriterion c1;
	private ScaleCriterion c2;
	
	@Before
	public void setUp() {
		model = new SMAAModel("test");
		a1 = new Alternative("alt1");
		a2 = new Alternative("alt2");		
		c1 = new ScaleCriterion("c1");
		c2 = new ScaleCriterion("c2");		
	}

	@Test
	public void testSetName() {
		JUnitUtil.testSetter(model, SMAAModel.PROPERTY_NAME, "test", "modelName");
	}
	
	@Test
	public void testAddCriterion() {		
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);
		mock.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(model, ModelChangeEvent.CRITERIA)));
		replay(mock);
		model.addCriterion(c1);
		verify(mock);

		assertTrue(model.getCriteria().contains(c1));
	}
	
	@Test
	public void testSetPreferenceInformation() {
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);
		mock.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(model, ModelChangeEvent.PREFERENCES_TYPE)));		
		replay(mock);
		MissingPreferenceInformation pref = new MissingPreferenceInformation(model.getAlternatives().size());
		model.setPreferenceInformation(pref);
		verify(mock);
		assertEquals(pref, model.getPreferenceInformation());
	}
	
	@Test
	public void testSetPreferenceInformationConnectsListener() {
		setupModel();
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		OrdinalPreferenceInformation preferences = new OrdinalPreferenceInformation(model.getCriteria());
		model.setPreferenceInformation(preferences);		
		model.addModelListener(mock);
		mock.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(model, ModelChangeEvent.PREFERENCES)));				
		expectLastCall().anyTimes();
		replay(mock);
		preferences.getRanks().get(0).setRank(2);
		verify(mock);
	}
	
	@Test
	public void testAddAlternative() {
		model.addCriterion(c1);
		model.addAlternative(a1);
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);
		mock.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(model, ModelChangeEvent.ALTERNATIVES)));
		expectLastCall().anyTimes();
		replay(mock);
		
		List<Alternative> alts2 = new ArrayList<Alternative>();
		alts2.add(a1);
		Alternative alt2 = a2;
		alts2.add(alt2);		
		model.addAlternative(a2);
		verify(mock);
		
		assertEquals(alts2, model.getAlternatives());		
		assertNotNull(((IndependentMeasurements) model.getMeasurements()).getMeasurement(c1, alt2));
	}
	
	@Test
	public void testToString() {
		SMAAModel mod = new SMAAModel("testModel");
		assertEquals("testModel", mod.toString());
	}
	
	@Test
	public void testDeleteAlternative() throws Exception {
		model.addAlternative(a1);
		model.addAlternative(a2);
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);
		mock.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(model, ModelChangeEvent.ALTERNATIVES)));		
		replay(mock);
		
		List<Alternative> alts2 = new ArrayList<Alternative>();
		alts2.add(a1);

		model.deleteAlternative(a2);
		verify(mock);
		assertEquals(alts2, model.getAlternatives());		
	}
	
	@Test
	public void testDeleteCriterion() throws Exception {
		OrdinalCriterion oc = new OrdinalCriterion("ord");
		model.addCriterion(oc);
		model.addCriterion(c2);
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);
		mock.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(model, ModelChangeEvent.CRITERIA)));
		mock.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(model, ModelChangeEvent.PREFERENCES_TYPE)));
		replay(mock);
		
		List<Criterion> crit2 = new ArrayList<Criterion>();
		crit2.add(oc);
		model.deleteCriterion(c2);
		verify(mock);
		
		assertEquals(crit2, model.getCriteria());	
	}
	
	@Test
	public void testDeepCopy() {
		setupModel();
		
		SMAAModel model2 = model.deepCopy();
	
		assertEquals(model.getName(), model2.getName());
		assertEquals(model.getAlternatives().size(), model2.getAlternatives().size());
		assertEquals(model.getCriteria().size(), model2.getCriteria().size());
		
		assertFalse(model.getAlternatives() == model2.getAlternatives());
		assertFalse(model.getCriteria() == model2.getCriteria());
		assertFalse(model.getPreferenceInformation() == model2.getPreferenceInformation());
	}

	private void setupModel() {
		Alternative a1 = new Alternative("a1");
		Alternative a2 = new Alternative("a2");
		ScaleCriterion c1 = new ScaleCriterion("c1");
		ScaleCriterion c2 = new ScaleCriterion("c2");
		model.addAlternative(a1);
		model.addAlternative(a2);
		model.addCriterion(c1);
		model.addCriterion(c2);
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c1, a1, new Interval(0.0, 6.0));
	}
	
	@Test
	public void testEquals() {
		SMAAModel model2 = model.deepCopy();
		assertEquals(model, model2);
	}
	
	@Test
	public void testSerialization() throws Exception {
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
		model.setPreferenceInformation(new OrdinalPreferenceInformation(model.getCriteria()));
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oout = new ObjectOutputStream(bos);
		oout.writeObject(model);
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		SMAAModel newModel = (SMAAModel) in.readObject();
		
		SMAAModelListener l = createMock(SMAAModelListener.class);
		newModel.addModelListener(l);
		l.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(newModel, ModelChangeEvent.PREFERENCES)));
		expectLastCall().anyTimes();
		replay(l);
		OrdinalPreferenceInformation pref = (OrdinalPreferenceInformation) newModel.getPreferenceInformation();
		pref.getRanks().get(0).setRank(1);
		verify(l);
	}
	
	@Test
	public void testSerializationHooksOrdinalChange() throws Exception{
		setupModel();
		model.setPreferenceInformation(new OrdinalPreferenceInformation(model.getCriteria()));
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
		l.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(newModel, ModelChangeEvent.MEASUREMENT)));				
		replay(l);
		ScaleCriterion c = (ScaleCriterion) newModel.getCriteria().get(0);
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
	
	@Test
	public void testAddCriterionRetainsMeasurements() throws Exception {
		SMAAModel m = new SMAAModel("model");
		Alternative a1 = new Alternative("a");
		ScaleCriterion c1 = new ScaleCriterion("c1");
		ScaleCriterion c2 = new ScaleCriterion("c2");
		m.addAlternative(a1);
		m.addCriterion(c1);
		LogNormalMeasurement meas = new LogNormalMeasurement(0.0, 0.2);
		((IndependentMeasurements) m.getMeasurements()).setMeasurement(c1, a1, meas);
		m.addCriterion(c2);
		assertEquals(meas,
				((IndependentMeasurements) m.getMeasurements()).getMeasurement(c1, a1));
	}
	
	@Test
	public void testReorderAlternatives() {
		SMAAModel m = new SMAAModel("model");
		m.addAlternative(a1);
		m.addAlternative(a2);
		List<Alternative> newList = new ArrayList<Alternative>();
		newList.add(a2);
		newList.add(a1);
		SMAAModelListener l = createMock(SMAAModelListener.class);
		m.addModelListener(l);
		l.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(m, ModelChangeEvent.ALTERNATIVES)));					
		replay(l);
		m.reorderAlternatives(newList);
		verify(l);
		assertEquals(newList, m.getAlternatives());
	}
	
	@Test
	public void testReorderCriteria() {
		SMAAModel m = new SMAAModel("model");
		m.addCriterion(c1);
		m.addCriterion(c2);
		List<Criterion> newList = new ArrayList<Criterion>();
		newList.add(c2);
		newList.add(c1);
		SMAAModelListener l = createMock(SMAAModelListener.class);
		m.addModelListener(l);
		l.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(m, ModelChangeEvent.CRITERIA)));					
		replay(l);
		m.reorderCriteria(newList);
		verify(l);
		assertEquals(newList, m.getCriteria());
		assertEquals(newList, ((IndependentMeasurements) m.getMeasurements()).getCriteria());
	}
}
