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

import fi.smaa.common.JUnitUtil;
import fi.smaa.jsmaa.model.OrdinalPreferenceInformation;
import fi.smaa.jsmaa.model.PreferenceListener;
import fi.smaa.jsmaa.model.Rank;

public class OrdinalPreferenceInformationTest {
	
	private OrdinalPreferenceInformation pref;

	@Before
	public void setUp() {
		List<Rank> ranks = new ArrayList<Rank>();
		ranks.add(new Rank(1));
		ranks.add(new Rank(3));
		ranks.add(new Rank(2));
		pref = new OrdinalPreferenceInformation(ranks);
	}
	
	@Test
	public void testCorrectSampling() {
		double[] w = pref.sampleWeights();
		assertEquals(3, w.length);
		assertTrue(w[0] > w[1]);
		assertTrue(w[0] > w[2]);
		assertTrue(w[2] > w[1]);
	}
	
	@Test
	public void testDeepCopy() {
		OrdinalPreferenceInformation pref2 = pref.deepCopy();
		assertEquals(3, pref2.getRanks().size());
		assertEquals(new Rank(1), pref2.getRanks().get(0));
		assertEquals(new Rank(3), pref2.getRanks().get(1));
		assertEquals(new Rank(2), pref2.getRanks().get(2));
		
		PreferenceListener mock = createMock(PreferenceListener.class);
		mock.preferencesChanged();
		mock.preferencesChanged();		
		pref2.addPreferenceListener(mock);		
		replay(mock);
		pref2.getRanks().get(0).setRank(2);
		verify(mock);
	}
	
	@Test
	public void testRankChangeUpdatesOtherRank() {
		PreferenceListener mock = createMock(PreferenceListener.class);
		mock.preferencesChanged();
		mock.preferencesChanged();		
		pref.addPreferenceListener(mock);		
		replay(mock);
		pref.getRanks().get(0).setRank(2);
		verify(mock);
		assertEquals(new Integer(1), pref.getRanks().get(2).getRank());
	}
	
	@Test
	public void testSerializationConnectsListeners() throws Exception {
		OrdinalPreferenceInformation p = JUnitUtil.serializeObject(pref);
		p.getRanks().get(0).setRank(2);
		assertEquals(new Integer(1), p.getRanks().get(2).getRank());		
	}	
}
