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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.electre.ElectreTri;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.OutrankingCriterion;

public class ElectreTriTest {

	private Alternative a1;
	private Alternative a2;
	private Alternative cat1;
	private Alternative cat2;
	private Alternative cat3;
	private OutrankingCriterion c1;
	private OutrankingCriterion c2;
	private OutrankingCriterion c3;	
	private List<Alternative> alts;
	private List<OutrankingCriterion> crit;
	private List<Alternative> cats;
	private double[] weights;
	private double lambda = 0.7;
	private ElectreTri tri;
	private Map<Alternative, Map<Criterion, Double>> meas;
	private Map<Alternative, Map<Criterion, Double>> ubounds;
	
	@Before
	public void setUp() {
		a1 = new Alternative("alt1");
		a2 = new Alternative("alt2");
		cat1 = new Alternative("cat1");
		cat2 = new Alternative("cat2");
		cat3 = new Alternative("cat3");
		c1 = new OutrankingCriterion("c1", true, 0.0, 1.0);
		c2 = new OutrankingCriterion("c2", true, 0.0, 1.0);
		c3 = new OutrankingCriterion("c3", true, 0.0, 1.0);
		alts = new ArrayList<Alternative>();
		cats = new ArrayList<Alternative>();
		crit = new ArrayList<OutrankingCriterion>();
		alts.add(a1);
		alts.add(a2);
		cats.add(cat1);
		cats.add(cat2);
		cats.add(cat3);
		crit.add(c1);
		crit.add(c2);
		weights = new double[] {0.4, 0.4, 0.2};
		meas = new HashMap<Alternative, Map<Criterion, Double>>();
		Map<Criterion, Double> a1meas = new HashMap<Criterion, Double>();
		Map<Criterion, Double> a2meas = new HashMap<Criterion, Double>();
		
		a1meas.put(c1, 1.0);
		a1meas.put(c2, 2.0);
		a1meas.put(c3, 0.0);
		
		a2meas.put(c1, 1.0);
		a2meas.put(c2, 1.0);
		a2meas.put(c3, 0.0);
		meas.put(a1, a1meas);
		meas.put(a2, a2meas);
		
		ubounds = new HashMap<Alternative, Map<Criterion, Double>>();
		Map<Criterion, Double> cat1meas = new HashMap<Criterion, Double>();
		Map<Criterion, Double> cat2meas = new HashMap<Criterion, Double>();		
		cat1meas.put(c1, 0.0);
		cat1meas.put(c2, 0.0);
		cat1meas.put(c3, 5.0);
		
		cat2meas.put(c1, 2.0);
		cat2meas.put(c2, 1.0);
		cat2meas.put(c3, 5.0);
		ubounds.put(cat1, cat1meas);
		ubounds.put(cat2, cat2meas);		
		
		tri = new ElectreTri(alts, crit, cats, meas, ubounds, weights, lambda, true);
	}
	
	@Test
	public void testOptimisticRule() {
		tri.setRule(true);
		Map<Alternative, Alternative> res = tri.compute();
		assertTrue(res.entrySet().size() == 2);
		assertEquals(cat3, res.get(a1));
		assertEquals(cat2, res.get(a2));
	}
	
	@Test
	public void testPessimisticRule() {
		tri.setRule(false);
		Map<Alternative, Alternative> res = tri.compute();
		assertTrue(res.entrySet().size() == 2);
		assertEquals(cat2, res.get(a1));
		assertEquals(cat2, res.get(a2));
	}
	
}
