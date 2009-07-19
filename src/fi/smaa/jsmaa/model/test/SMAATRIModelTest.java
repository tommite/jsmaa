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
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.ModelChangeEvent;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.SMAAModelListener;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class SMAATRIModelTest {

	private SMAATRIModel model;
	private Alternative a1;
	private Alternative a2;
	private OutrankingCriterion c2;
	private OutrankingCriterion c1;
	private ArrayList<Alternative> alts;
	private ArrayList<Criterion> crit;
	private Alternative p1;
	private ArrayList<Alternative> cats;
	private Alternative p2;
	
	@Before
	public void setUp() {
		model = new SMAATRIModel("model");
		a1 = new Alternative("a1");
		a2 = new Alternative("a2");
		c1 = new OutrankingCriterion("c1", true, 0.0, 1.0);
		c2 = new OutrankingCriterion("c2", true, 0.0, 1.0);
		p1 = new Alternative("p1");
		p2 = new Alternative("p2");
		alts = new ArrayList<Alternative>();
		crit = new ArrayList<Criterion>();
		cats = new ArrayList<Alternative>();
		alts.add(a1);
		alts.add(a2);
		crit.add(c1);
		crit.add(c2);
		cats.add(p1);
		cats.add(p2);
		model.setAlternatives(alts);
	}
	
	@Test
	public void testSetProfiles() {
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);
		mock.modelChanged(ModelChangeEvent.CATEGORIES);
		mock.modelChanged(ModelChangeEvent.MEASUREMENT_TYPE);
		replay(mock);
		model.setCategories(cats);
		verify(mock);
		assertEquals(cats, model.getCategories());
	}
	
	@Test
	public void testSerialization() throws Exception {
		model.setCriteria(crit);
		model.setCategories(cats);
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oout = new ObjectOutputStream(bos);
		oout.writeObject(model);
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		SMAATRIModel newModel = (SMAATRIModel) in.readObject();
		assertEquals(model.getAlternatives().size(), newModel.getAlternatives().size());
		assertEquals(model.getCriteria().size(), newModel.getCriteria().size());
		
		SMAAModelListener l = createMock(SMAAModelListener.class);
		newModel.addModelListener(l);
		l.modelChanged(ModelChangeEvent.MEASUREMENT_TYPE);
		replay(l);
		newModel.setCategoryUpperBound((OutrankingCriterion)newModel.getCriteria().get(0),
				newModel.getCategories().iterator().next(),
				new Interval(0.0, 1.0));
		verify(l);		
	}	
	
	@Test
	public void testSetRule() {
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);
		mock.modelChanged(ModelChangeEvent.PARAMETER);
		replay(mock);
		model.setRule(false);
		verify(mock);
		assertFalse(model.getRule());
	}
	
	@Test
	public void testSetLambda() {
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);
		mock.modelChanged(ModelChangeEvent.PARAMETER);
		replay(mock);
		model.setLambda(0.9);
		verify(mock);
		assertEquals(0.9, model.getLambda(), 0.000001);
	}	
	
	@Test
	public void testConstructorCorrectLambda() {
		assertEquals(SMAATRIModel.DEFAULT_LAMBDA_VALUE, model.getLambda(), 0.00001);
	}
	
	@Test
	public void testDeepCopy() {
		model.setRule(false);
		SMAATRIModel model2 = model.deepCopy();
	
		assertEquals(model.getName(), model2.getName());
		assertEquals(model.getAlternatives().size(), model2.getAlternatives().size());
		assertEquals(model.getCriteria().size(), model2.getCriteria().size());
		assertEquals(model.getCategories().size(), model2.getCategories().size());
		assertFalse(model2.getRule());
		assertEquals(model.getLambda(), model2.getLambda(), 0.00001);
		
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
		List<Alternative> alts = new ArrayList<Alternative>();
		Alternative c1 = new Alternative("cat1");
		alts.add(c1);
		Alternative c2 = new Alternative("cat2");
		alts.add(c2);				
		model.setCategories(alts);
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);
		mock.modelChanged(ModelChangeEvent.CATEGORIES);
		mock.modelChanged(ModelChangeEvent.MEASUREMENT_TYPE);
		replay(mock);
		
		List<Alternative> cats2 = new ArrayList<Alternative>();
		cats2.add(c1);

		model.deleteCategory(c2);
		verify(mock);
		
		assertEquals(cats2, model.getCategories());				
	}
}
