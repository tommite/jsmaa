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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.ExactMeasurement;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.OutrankingCriterion;
import fi.smaa.jsmaa.model.electre.ElectreTri;

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
	private Map<Alternative, Map<OutrankingCriterion, Double>> meas;
	private Map<Alternative, Map<OutrankingCriterion, Double>> ubounds;
	
	@Before
	public void setUp() {
		a1 = new Alternative("alt1");
		a2 = new Alternative("alt2");
		cat1 = new Alternative("cat1");
		cat2 = new Alternative("cat2");
		cat3 = new Alternative("cat3");
		c1 = new OutrankingCriterion("c1", true, new Interval(0.0, 0.0), new Interval(1.0, 1.0));
		c2 = new OutrankingCriterion("c2", true, new Interval(0.0, 0.0), new Interval(1.0, 1.0));
		c3 = new OutrankingCriterion("c3", true, new Interval(0.0, 0.0), new Interval(1.0, 1.0));
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
		crit.add(c3);
		weights = new double[] {0.4, 0.4, 0.2};
		meas = new HashMap<Alternative, Map<OutrankingCriterion, Double>>();
		Map<OutrankingCriterion, Double> a1meas = new HashMap<OutrankingCriterion, Double>();
		Map<OutrankingCriterion, Double> a2meas = new HashMap<OutrankingCriterion, Double>();
		
		a1meas.put(c1, 1.0);
		a1meas.put(c2, 2.0);
		a1meas.put(c3, 0.0);
		
		a2meas.put(c1, 1.0);
		a2meas.put(c2, 1.0);
		a2meas.put(c3, 0.0);
		meas.put(a1, a1meas);
		meas.put(a2, a2meas);
		
		ubounds = new HashMap<Alternative, Map<OutrankingCriterion, Double>>();
		Map<OutrankingCriterion, Double> cat1meas = new HashMap<OutrankingCriterion, Double>();
		Map<OutrankingCriterion, Double> cat2meas = new HashMap<OutrankingCriterion, Double>();		
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
	public void test1Category() {
		cats.clear();
		cats.add(cat1);
		ubounds.clear();
		tri = new ElectreTri(alts, crit, cats, meas, ubounds, weights, lambda, true);		
		Map<Alternative, Alternative> res = tri.compute();
		assertTrue(res.entrySet().size() == 2);
		assertEquals(cat1, res.get(a1));
		assertEquals(cat1, res.get(a2));
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
	
	@Test
	public void testMeradModel() {
		Alternative z1 = new Alternative("z1");
		Alternative z2 = new Alternative("z2");		
		Alternative z3 = new Alternative("z3");
		Alternative z4 = new Alternative("z4");
		Alternative z5 = new Alternative("z5");
		Alternative z6 = new Alternative("z6");		
		Alternative z7 = new Alternative("z7");
		Alternative z8 = new Alternative("z8");
		Alternative z10 = new Alternative("z10");
		
		OutrankingCriterion g11 = new OutrankingCriterion("g1.1", true, new ExactMeasurement(0.05), new ExactMeasurement(0.1));
		OutrankingCriterion g12 = new OutrankingCriterion("g1.2", true, new ExactMeasurement(0.0), new ExactMeasurement(0.0));
		OutrankingCriterion g13 = new OutrankingCriterion("g1.3", true, new ExactMeasurement(0.0), new ExactMeasurement(0.0));
		OutrankingCriterion g14 = new OutrankingCriterion("g1.4", true, new ExactMeasurement(0.0), new ExactMeasurement(0.0));
		OutrankingCriterion g15 = new OutrankingCriterion("g1.5", true, new ExactMeasurement(0.0), new ExactMeasurement(0.0));
		OutrankingCriterion g21 = new OutrankingCriterion("g2.1", false, new ExactMeasurement(10.0), new ExactMeasurement(20.0));
		OutrankingCriterion g22 = new OutrankingCriterion("g2.2", true, new ExactMeasurement(0.1), new ExactMeasurement(0.2));
		OutrankingCriterion g23 = new OutrankingCriterion("g2.3", true, new ExactMeasurement(0.05), new ExactMeasurement(0.09));
		OutrankingCriterion g24 = new OutrankingCriterion("g2.4", true, new ExactMeasurement(0.5), new ExactMeasurement(1.0));
		OutrankingCriterion g251 = new OutrankingCriterion("g2.5.1", true, new ExactMeasurement(0.0), new ExactMeasurement(0.0));
		Alternative cat4 = new Alternative("class 4");
		Alternative cat3 = new Alternative("class 3");
		Alternative cat2 = new Alternative("class 2");
		Alternative cat1 = new Alternative("class 1");
		
		List<Alternative> alts = new ArrayList<Alternative>();
		alts.add(z1);
		alts.add(z2);
		alts.add(z3);
		alts.add(z4);
		alts.add(z5);
		alts.add(z6);
		alts.add(z7);
		alts.add(z8);
		alts.add(z10);
		
		List<OutrankingCriterion> crit = new ArrayList<OutrankingCriterion>();
		crit.add(g11);
		crit.add(g12);
		crit.add(g13);
		crit.add(g14);
		crit.add(g15);
		crit.add(g21);
		crit.add(g22);
		crit.add(g23);
		crit.add(g24);
		crit.add(g251);
		
		List<Alternative> cats = new ArrayList<Alternative>();
		cats.add(cat4);
		cats.add(cat3);
		cats.add(cat2);
		cats.add(cat1);
		
		Map<Alternative, Map<OutrankingCriterion, Double>> mmeas = new HashMap<Alternative, Map<OutrankingCriterion, Double>>();
		Map<OutrankingCriterion, Double> z1meas = new HashMap<OutrankingCriterion, Double>();
		z1meas.put(g11, 5.8);
		z1meas.put(g12, 10.0);
		z1meas.put(g13, 0.0);
		z1meas.put(g14, 20.0);
		z1meas.put(g15, 0.0);
		z1meas.put(g21, 35.0);
		z1meas.put(g22, 2.37);
		z1meas.put(g23, 6.8);
		z1meas.put(g24, 3.6);
		z1meas.put(g251, 20.0);
		mmeas.put(z1, z1meas);
		
		Map<OutrankingCriterion, Double> z2meas = new HashMap<OutrankingCriterion, Double>();
		z2meas.put(g11, 4.8);
		z2meas.put(g12, 0.0);
		z2meas.put(g13, 40.0);
		z2meas.put(g14, 0.0);
		z2meas.put(g15, 0.0);
		z2meas.put(g21, 70.0);
		z2meas.put(g22, 1.28);
		z2meas.put(g23, 1.83);
		z2meas.put(g24, 0.2);
		z2meas.put(g251, 10.0);
		mmeas.put(z2, z2meas);

		Map<OutrankingCriterion, Double> z3meas = new HashMap<OutrankingCriterion, Double>();
		z3meas.put(g11, 9.7);
		z3meas.put(g12, 10.0);
		z3meas.put(g13, 10.0);
		z3meas.put(g14, 0.0);
		z3meas.put(g15, 30.0);
		z3meas.put(g21, 200.0);
		z3meas.put(g22, 1.67);
		z3meas.put(g23, 0.84);
		z3meas.put(g24, 7.4);
		z3meas.put(g251, 30.0);
		mmeas.put(z3, z3meas);
		
		Map<OutrankingCriterion, Double> z4meas = new HashMap<OutrankingCriterion, Double>();
		z4meas.put(g11, 10.4);
		z4meas.put(g12, 10.0);
		z4meas.put(g13, 10.0);
		z4meas.put(g14, 10.0);
		z4meas.put(g15, 30.0);
		z4meas.put(g21, 203.0);
		z4meas.put(g22, 1.68);
		z4meas.put(g23, 0.83);
		z4meas.put(g24, 9.0);
		z4meas.put(g251, 20.0);
		mmeas.put(z4, z4meas);
		
		Map<OutrankingCriterion, Double> z5meas = new HashMap<OutrankingCriterion, Double>();
		z5meas.put(g11, 9.7);
		z5meas.put(g12, 0.0);
		z5meas.put(g13, 10.0);
		z5meas.put(g14, 0.0);
		z5meas.put(g15, 10.0);
		z5meas.put(g21, 222.0);
		z5meas.put(g22, 1.2);
		z5meas.put(g23, 0.54);
		z5meas.put(g24, 1.8);
		z5meas.put(g251, 20.0);
		mmeas.put(z5, z5meas);
		
		Map<OutrankingCriterion, Double> z6meas = new HashMap<OutrankingCriterion, Double>();
		z6meas.put(g11, 9.8);
		z6meas.put(g12, 10.0);
		z6meas.put(g13, 0.0);
		z6meas.put(g14, 20.0);
		z6meas.put(g15, 0.0);
		z6meas.put(g21, 50.0);
		z6meas.put(g22, 1.27);
		z6meas.put(g23, 2.54);
		z6meas.put(g24, 6.7);
		z6meas.put(g251, 20.0);
		mmeas.put(z6, z6meas);
		
		Map<OutrankingCriterion, Double> z7meas = new HashMap<OutrankingCriterion, Double>();
		z7meas.put(g11, 12.3);
		z7meas.put(g12, 0.0);
		z7meas.put(g13, 0.0);
		z7meas.put(g14, 0.0);
		z7meas.put(g15, 30.0);
		z7meas.put(g21, 155.0);
		z7meas.put(g22, 0.96);
		z7meas.put(g23, 0.61);
		z7meas.put(g24, 14.1);
		z7meas.put(g251, 10.0);
		mmeas.put(z7, z7meas);		
		
		Map<OutrankingCriterion, Double> z8meas = new HashMap<OutrankingCriterion, Double>();
		z8meas.put(g11, 11.2);
		z8meas.put(g12, 10.0);
		z8meas.put(g13, 0.0);
		z8meas.put(g14, 0.0);
		z8meas.put(g15, 30.0);
		z8meas.put(g21, 180.0);
		z8meas.put(g22, 0.71);
		z8meas.put(g23, 0.39);
		z8meas.put(g24, 6.4);
		z8meas.put(g251, 20.0);
		mmeas.put(z8, z8meas);
		
		Map<OutrankingCriterion, Double> z10meas = new HashMap<OutrankingCriterion, Double>();
		z10meas.put(g11, 11.0);
		z10meas.put(g12, 10.0);
		z10meas.put(g13, 0.0);
		z10meas.put(g14, 10.0);
		z10meas.put(g15, 30.0);
		z10meas.put(g21, 170.0);
		z10meas.put(g22, 0.31);
		z10meas.put(g23, 0.18);
		z10meas.put(g24, 2.6);
		z10meas.put(g251, 20.0);
		mmeas.put(z10, z10meas);				
		
		Map<Alternative, Map<OutrankingCriterion, Double>> upbounds = new HashMap<Alternative, Map<OutrankingCriterion, Double>>();
		Map<OutrankingCriterion, Double> cat4meas = new HashMap<OutrankingCriterion, Double>();
		Map<OutrankingCriterion, Double> cat3meas = new HashMap<OutrankingCriterion, Double>();
		Map<OutrankingCriterion, Double> cat2meas = new HashMap<OutrankingCriterion, Double>();
		
		upbounds.put(cat4, cat4meas);
		upbounds.put(cat3, cat3meas);
		upbounds.put(cat2, cat2meas);
		
		cat4meas.put(g11, 8.0);
		cat4meas.put(g12, 0.0);
		cat4meas.put(g13, 10.0);
		cat4meas.put(g14, 10.0);
		cat4meas.put(g15, 10.0);
		cat4meas.put(g21, 190.0);
		cat4meas.put(g22, 1.0);
		cat4meas.put(g23, 0.63);
		cat4meas.put(g24, 6.0);
		cat4meas.put(g251, 20.0);
		
		cat3meas.put(g11, 10.0);
		cat3meas.put(g12, 10.0);
		cat3meas.put(g13, 10.0);
		cat3meas.put(g14, 10.0);
		cat3meas.put(g15, 10.0);
		cat3meas.put(g21, 150.0);
		cat3meas.put(g22, 1.4);
		cat3meas.put(g23, 0.82);
		cat3meas.put(g24, 20.0);
		cat3meas.put(g251, 20.0);

		cat2meas.put(g11, 14.0);
		cat2meas.put(g12, 10.0);
		cat2meas.put(g13, 40.0);
		cat2meas.put(g14, 20.0);
		cat2meas.put(g15, 20.0);
		cat2meas.put(g21, 110.0);
		cat2meas.put(g22, 1.9);
		cat2meas.put(g23, 1.0);
		cat2meas.put(g24, 35.0);
		cat2meas.put(g251, 30.0);
		
		double[] myweights = new double[] { 0.11, 0.02, 0.02, 0.02, 0.11, 0.02, 0.02, 0.44, 0.02, 0.22};

		ElectreTri model = new ElectreTri(alts, crit, cats, mmeas, upbounds, myweights, 0.65, true);
		
		Map<Alternative, Alternative> results = model.compute();
		
		assertEquals(0.5, model.concordance(z10meas, cat4meas), 0.01);

		assertEquals(cat1, results.get(z1));
		assertEquals(cat1, results.get(z2));
		assertEquals(cat2, results.get(z3));
		assertEquals(cat2, results.get(z4));	
		assertEquals(cat3, results.get(z5));
		assertEquals(cat1, results.get(z6));
		assertEquals(cat3, results.get(z7));
		assertEquals(cat4, results.get(z8));
		assertEquals(cat4, results.get(z10));
		
	}
}
