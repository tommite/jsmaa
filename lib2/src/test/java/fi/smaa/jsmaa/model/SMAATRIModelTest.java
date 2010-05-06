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

package fi.smaa.jsmaa.model;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.JUnitUtil;

public class SMAATRIModelTest {

	private SMAATRIModel model;
	private Alternative a1;
	private Alternative a2;
	private OutrankingCriterion c2;
	private OutrankingCriterion c1;
	private ArrayList<Alternative> alts;
	private ArrayList<Criterion> crit;
	private Alternative cat1;
	private ArrayList<Alternative> cats;
	private Alternative cat2;
	
	@Before
	public void setUp() {
		model = new SMAATRIModel("model");
		a1 = new Alternative("a1");
		a2 = new Alternative("a2");
		c1 = new OutrankingCriterion("c1", true, new Interval(0.0, 0.0), new Interval(1.0, 1.0));
		c2 = new OutrankingCriterion("c2", true, new Interval(0.0, 0.0), new Interval(1.0, 1.0));
		cat1 = new Alternative("cat1");
		cat2 = new Alternative("cat2");
		alts = new ArrayList<Alternative>();
		crit = new ArrayList<Criterion>();
		cats = new ArrayList<Alternative>();
		alts.add(a1);
		alts.add(a2);
		crit.add(c1);
		crit.add(c2);
		cats.add(cat1);
		cats.add(cat2);
		model.addAlternative(a1);
		model.addAlternative(a2);
	}
	
	@Test
	public void testAddCategory() {
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);
		mock.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(model, ModelChangeEvent.CATEGORIES)));				
		replay(mock);
		model.addCategory(cat1);
		verify(mock);
		assertEquals(Collections.singletonList(cat1), model.getCategories());
	}
		
	@Test
	public void testSerialization() throws Exception {
		model.addCriterion(c1);
		model.addCriterion(c2);
		model.addCategory(cat1);
		model.addCategory(cat2);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oout = new ObjectOutputStream(bos);
		oout.writeObject(model);
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		SMAATRIModel newModel = (SMAATRIModel) in.readObject();
		assertEquals(model.getAlternatives().size(), newModel.getAlternatives().size());
		assertEquals(model.getCriteria().size(), newModel.getCriteria().size());

		SMAAModelListener l = createMock(SMAAModelListener.class);
		newModel.addModelListener(l);
		l.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(newModel, ModelChangeEvent.MEASUREMENT_TYPE)));				
		replay(l);
		newModel.setCategoryUpperBound((OutrankingCriterion)newModel.getCriteria().get(0),
				newModel.getCategories().iterator().next(),
				new Interval(0.0, 1.0));
		verify(l);
		
		newModel.removeModelListener(l);
		SMAAModelListener l2 = createMock(SMAAModelListener.class);
		newModel.addModelListener(l2);
		l2.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(newModel, ModelChangeEvent.PARAMETER)));				
		replay(l2);
		newModel.getLambda().setStart(0.55);
		verify(l2);				
	}	
	
	@Test
	public void testSetRule() {
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);
		mock.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(model, ModelChangeEvent.PARAMETER)));				
		replay(mock);
		model.setRule(false);
		verify(mock);
		assertFalse(model.getRule());
	}
	
	@Test
	public void testLambdaFires() {
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);
		mock.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(model, ModelChangeEvent.PARAMETER)));				
		replay(mock);
		model.getLambda().setEnd(0.9);
		verify(mock);
		assertEquals(0.9, model.getLambda().getEnd(), 0.000001);
	}	
	
	@Test
	public void testConstructorCorrectLambda() {
		assertEquals(new Interval(0.65, 0.85), model.getLambda());
	}
	
	@Test
	public void testDeepCopy() {
		model.setRule(false);
		model.getLambda().setStart(0.55);
		model.getLambda().setEnd(0.56);
		SMAATRIModel model2 = model.deepCopy();
	
		assertEquals(model.getName(), model2.getName());
		assertEquals(model.getAlternatives().size(), model2.getAlternatives().size());
		assertEquals(model.getCriteria().size(), model2.getCriteria().size());
		assertEquals(model.getCategories().size(), model2.getCategories().size());
		assertFalse(model2.getRule());
		assertEquals(model.getLambda(), model2.getLambda());
		
		assertFalse(model.getAlternatives() == model2.getAlternatives());
		assertFalse(model.getCriteria() == model2.getCriteria());
		assertFalse(model.getPreferenceInformation() == model2.getPreferenceInformation());
		
		for (int i=0;i<model2.getCategories().size()-1;i++) {
			for (Criterion c : model2.getCriteria()) {
				assertNotNull(model2.getCategoryUpperBound((OutrankingCriterion) c,
						model2.getCategories().get(i)));
			}
		}
	}	
	
	@Test
	public void testDeleteCategory() {
		model.addCategory(cat1);
		model.addCategory(cat2);
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);
		mock.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(model, ModelChangeEvent.CATEGORIES)));				
		replay(mock);
		
		List<Alternative> cats2 = new ArrayList<Alternative>();
		cats2.add(cat1);

		model.deleteCategory(cat2);
		verify(mock);
		
		assertEquals(cats2, model.getCategories());				
	}
	
	@Test
	public void testReorderCategories() {
		SMAATRIModel m = new SMAATRIModel("model");
		m.addCategory(a1);
		m.addCategory(a2);
		assertEquals(Collections.singletonList(a1), m.getProfileImpactMatrix().getAlternatives());
		
		List<Alternative> newList = new ArrayList<Alternative>();
		newList.add(a2);
		newList.add(a1);
		SMAAModelListener l = createMock(SMAAModelListener.class);
		m.addModelListener(l);
		l.modelChanged((ModelChangeEvent) JUnitUtil.eqEventObject(new ModelChangeEvent(m, ModelChangeEvent.CATEGORIES)));					
		replay(l);
		m.reorderCategories(newList);
		verify(l);
		assertEquals(newList, m.getCategories());
		assertEquals(Collections.singletonList(a2), m.getProfileImpactMatrix().getAlternatives());
	}
	
	@Test
	public void testReorderCriteria() {
		SMAATRIModel m = new SMAATRIModel("model");
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
		assertEquals(newList, m.getProfileImpactMatrix().getCriteria());
	}	
}
