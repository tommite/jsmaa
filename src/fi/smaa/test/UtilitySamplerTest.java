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

package fi.smaa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.Alternative;
import fi.smaa.GaussianCriterion;
import fi.smaa.GaussianMeasurement;
import fi.smaa.OrdinalCriterion;
import fi.smaa.Rank;
import fi.smaa.UniformCriterion;
import fi.smaa.UtilitySampler;
import fi.smaa.common.Interval;

public class UtilitySamplerTest {

	private GaussianCriterion gcrit;
	private List<Alternative> alts;
	
	@Before
	public void setUp() {
		gcrit = new GaussianCriterion("crit");
		alts = new ArrayList<Alternative>();
		alts.add(new Alternative("alt1"));
		alts.add(new Alternative("alt2"));	
	} 

	private HashMap<Alternative, GaussianMeasurement> generateGausMeas2Alts() {
		HashMap<Alternative, GaussianMeasurement> meas
			= new HashMap<Alternative, GaussianMeasurement>();
		meas.put(alts.get(0), new GaussianMeasurement(0.0, 1.0));
		meas.put(alts.get(1), new GaussianMeasurement(1.0, 0.0));
		return meas;
	}	
	
	@Test
	public void testGaussianSample() {
		UtilitySampler spl = new UtilitySampler(alts.size());
		gcrit.setAlternatives(alts);
		HashMap<Alternative, GaussianMeasurement> meas = generateGausMeas2Alts();		
		gcrit.setMeasurements(meas);
		double[] tgt = new double[2];
		spl.sample(gcrit, tgt);
		// something goes in there
		assertEquals(1.0, tgt[1], 0.001);
	}
	
	@Test
	public void testOrdinalSample() {
		OrdinalCriterion crit = new OrdinalCriterion("Crit");
		alts = new ArrayList<Alternative>();
		alts.add(new Alternative("alt1"));
		alts.add(new Alternative("alt2"));
		Map<Alternative, Rank> rankMap = new HashMap<Alternative, Rank>();
		rankMap.put(alts.get(0), new Rank(2));
		rankMap.put(alts.get(1), new Rank(1));
		
		crit.setAlternatives(alts);
		crit.setMeasurements(rankMap);		
		double[] tgt = new double[2];
		UtilitySampler spl = new UtilitySampler(alts.size());
		spl.sample(crit, tgt);
		assertTrue(tgt[0] < tgt[1]);
	}	
	
	@Test
	public void testUniformSample() {
		UniformCriterion criterion = new UniformCriterion("crit");
		List<Alternative> alts = new ArrayList<Alternative>();
		alts.add(new Alternative("alt1"));
		alts.add(new Alternative("alt2"));
		
		Map<Alternative, Interval> meas = new HashMap<Alternative, Interval>();
		meas.put(alts.get(0), new Interval(0.0, 0.5));
		meas.put(alts.get(1), new Interval(0.5, 1.0));		
		criterion.setAlternatives(alts);
		criterion.setMeasurements(meas);
		double[] tgt = new double[2];
		
		UtilitySampler spl = new UtilitySampler(alts.size());
		spl.sample(criterion, tgt);
		assertNotNull(tgt[0]);
		assertTrue(tgt[0] >= 0.0);
		assertTrue(tgt[0] <= 0.5);
		assertTrue(tgt[1] >= 0.5);
		assertTrue(tgt[1] <= 1.0);		
	}
	
}
