/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper 2012.

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
package fi.smaa.jsmaa.simulator;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.RandomUtil;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.IndependentMeasurements;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.Rank;
import fi.smaa.jsmaa.model.SMAAModel;

public class SamplerTest {

	private SMAAModel model;
	private Sampler sampler;
	private RandomUtil random;
	private OrdinalCriterion c;
	private Rank r1;
	private Rank r2;

	@Before
	public void setUp() {
		model = new SMAAModel("model");
		Alternative a1 = new Alternative("a1");
		Alternative a2 = new Alternative("a2");
		
		c = new OrdinalCriterion("c1");
		model.addAlternative(a1);
		model.addAlternative(a2);
		
		model.addCriterion(c);
		
		r1 = new Rank(2);
		r2 = new Rank(1);
		
		random = RandomUtil.createWithFixedSeed();
		
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c, a1, r1);
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c, a2, r2);
		
		sampler = new Sampler((IndependentMeasurements) model.getMeasurements(), random);
	}
	
	@Test
	public void testSampleOrdinal() {
		double[] arr = new double[2];
		sampler.sample(c, arr);
		assertTrue(arr[0] < arr[1]);
		
		r1.setRank(1);
		r2.setRank(2);
		sampler.sample(c, arr);
		assertTrue(arr[0] > arr[1]);
	}
}
