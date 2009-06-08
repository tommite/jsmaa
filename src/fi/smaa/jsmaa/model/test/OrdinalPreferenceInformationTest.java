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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fi.smaa.jsmaa.model.OrdinalPreferenceInformation;
import fi.smaa.jsmaa.model.Rank;

public class OrdinalPreferenceInformationTest {

	@Test
	public void testCorrectSampling() {
		List<Rank> ranks = new ArrayList<Rank>();
		ranks.add(new Rank(1));
		ranks.add(new Rank(3));
		ranks.add(new Rank(2));
		OrdinalPreferenceInformation pref = new OrdinalPreferenceInformation(ranks);		

		double[] w = pref.sampleWeights();
		assertEquals(3, w.length);
		assertTrue(w[0] > w[1]);
		assertTrue(w[0] > w[2]);
		assertTrue(w[2] > w[1]);
	}
}
