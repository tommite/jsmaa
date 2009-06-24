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
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ModelChangeEvent;
import fi.smaa.jsmaa.model.SMAAModelListener;
import fi.smaa.jsmaa.model.SMAATRIModel;

public class SMAATRIModelTest {

	private SMAATRIModel model;
	private Alternative a1;
	private Alternative a2;
	private CardinalCriterion c2;
	private CardinalCriterion c1;
	private ArrayList<Alternative> alts;
	private ArrayList<Criterion> crit;
	private Alternative p1;
	private ArrayList<Alternative> prof;
	private Alternative p2;
	
	@Before
	public void setUp() {
		model = new SMAATRIModel("model");
		a1 = new Alternative("a1");
		a2 = new Alternative("a2");
		c1 = new CardinalCriterion("c1");
		c2 = new CardinalCriterion("c2");
		p1 = new Alternative("p1");
		p2 = new Alternative("p2");
		alts = new ArrayList<Alternative>();
		crit = new ArrayList<Criterion>();
		prof = new ArrayList<Alternative>();
		alts.add(a1);
		alts.add(a2);
		crit.add(c1);
		crit.add(c2);
		prof.add(p1);
		prof.add(p2);
		model.setAlternatives(alts);
	}
	
	@Test
	public void testSetProfiles() {
		SMAAModelListener mock = createMock(SMAAModelListener.class);
		model.addModelListener(mock);
		mock.modelChanged(ModelChangeEvent.PROFILES);
		mock.modelChanged(ModelChangeEvent.MEASUREMENT_TYPE);
		replay(mock);
		model.setProfiles(prof);
		verify(mock);
		assertEquals(prof, model.getCategories());
	}
	
	@Test
	public void testSerialization() throws Exception {
		fail();
		/*
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oout = new ObjectOutputStream(bos);
		oout.writeObject(model);
		ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
		SMAAModel newModel = (SMAAModel) in.readObject();
		assertEquals(model.getAlternatives().size(), newModel.getAlternatives().size());
		assertEquals(model.getCriteria().size(), newModel.getCriteria().size());
		*/
	}	
}
