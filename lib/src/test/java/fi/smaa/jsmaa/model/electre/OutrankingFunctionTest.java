/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.

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
package fi.smaa.jsmaa.model.electre;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.RandomUtil;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.simulator.IterationException;

public class OutrankingFunctionTest {
	
	private OutrankingCriterion c;
	
	@Before
	public void setUp() throws IterationException {
		c = new OutrankingCriterion("crit", true, new Interval(1.0, 1.0), 
				new Interval(2.0, 2.0));
		c.sampleThresholds(RandomUtil.createWithFixedSeed());
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
	
	@Test
	public void testOutranksEqual() {
		c = new OutrankingCriterion("crit", true, new ExactMeasurement(0.0), new ExactMeasurement(0.0));
		assertEquals(1.0, OutrankingFunction.concordance(c, 10.0, 10.0), 0.000001);
	}
	
	@Test
	public void testGreater() {
		c = new OutrankingCriterion("crit", true, new ExactMeasurement(0.05), new ExactMeasurement(0.1));
		assertEquals(1.0, OutrankingFunction.concordance(c, 9.7, 8.0), 0.000001);		
	}
		
}
