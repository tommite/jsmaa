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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.model.CardinalPreferenceInformation;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.PreferenceListener;
import fi.smaa.jsmaa.model.ScaleCriterion;

public class CardinalPreferenceInformationTest {
	
	private CardinalPreferenceInformation info;
	private ScaleCriterion crit;

	@Before
	public void setUp() {
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
		PreferenceListener list = createMock(PreferenceListener.class);
		info.addPreferenceListener(list);
		list.preferencesChanged();
		replay(list);
		info.setMeasurement(crit, new ExactMeasurement(1.0));
		assertEquals(new ExactMeasurement(1.0), info.getMeasurement(crit));
		verify(list);
	}
	
	@Test
	public void testMeasurementChangeFires() {
		PreferenceListener list = createMock(PreferenceListener.class);
		ExactMeasurement m = new ExactMeasurement(2.0);
		info.setMeasurement(crit, m);
		info.addPreferenceListener(list);		
		list.preferencesChanged();
		replay(list);
		m.setValue(3.0);
		verify(list);
	}
	
	@Test
	public void testSampleWeights() {
		info.setMeasurement(crit, new ExactMeasurement(2.0));
		double[] w = info.sampleWeights();
		assertEquals(1, w.length);
		assertEquals(2.0, w[0], 0.0000001);
	}
	
	@Test
	public void testDeepCopy() {
		info.setMeasurement(crit, new ExactMeasurement(1.0));
		CardinalPreferenceInformation info2 = info.deepCopy();
		
		assertEquals(1, info2.getCriteria().size());
		assertTrue(info2.getCriteria().get(0)instanceof ScaleCriterion);
		assertEquals("c", info2.getCriteria().get(0).getName());
		assertEquals(new ExactMeasurement(1.0), info2.getMeasurement(info.getCriteria().get(0)));
		
		PreferenceListener list = createMock(PreferenceListener.class);
		info2.addPreferenceListener(list);		
		list.preferencesChanged();
		replay(list);
		((ExactMeasurement) info2.getMeasurement(info2.getCriteria().get(0))).setValue(3.0);
		verify(list);		
		
	}	
}
