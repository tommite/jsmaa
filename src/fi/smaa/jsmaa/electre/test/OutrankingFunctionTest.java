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

package fi.smaa.jsmaa.electre.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.electre.OutrankingFunction;
import fi.smaa.jsmaa.model.OutrankingCriterion;

public class OutrankingFunctionTest {
	
	private OutrankingCriterion c;
	
	@Before
	public void setUp() {
		c = new OutrankingCriterion("crit", true, 1.0, 2.0);
	}
	

	@Test
	public void testIndifferenceThreshold() {
		assertEquals(1.0, OutrankingFunction.concordance(c, 1.0, 2.0), 0.00000001);
		assertEquals(1.0, OutrankingFunction.concordance(c, 2.0, 1.0), 0.00000001);
		// change to descending
		c.setAscending(false);
		assertEquals(1.0, OutrankingFunction.concordance(c, 1.0, 2.0), 0.00000001);		
		assertEquals(1.0, OutrankingFunction.concordance(c, 2.0, 1.0), 0.00000001);
		
		assertEquals(1.0, OutrankingFunction.concordance(c, 1.0, 1.0), 0.00000001);
	}
	
	@Test
	public void testPreferenceThreshold() {
		assertEquals(0.0, OutrankingFunction.concordance(c, 0.0, 2.0), 0.0000001);
		c.setAscending(false);
		assertEquals(0.0, OutrankingFunction.concordance(c, 2.0, 0.0), 0.0000001);		
	}
	
	@Test
	public void testHesitationArea() {
		assertEquals(0.5, OutrankingFunction.concordance(c, 0.0, 1.5), 0.0000001);
		c.setAscending(false);
		assertEquals(0.5, OutrankingFunction.concordance(c, 0.0, -1.5), 0.0000001);		
	}
}
