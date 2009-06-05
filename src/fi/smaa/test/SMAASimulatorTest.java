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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.Alternative;
import fi.smaa.AbstractCriterion;
import fi.smaa.Criterion;
import fi.smaa.GaussianCriterion;
import fi.smaa.GaussianMeasurement;
import fi.smaa.Rank;
import fi.smaa.SMAAModel;
import fi.smaa.SMAAResults;
import fi.smaa.SMAASimulator;
import fi.smaa.UniformCriterion;
import fi.smaa.common.Interval;


public class SMAASimulatorTest {
	
	private SMAAModel model;
	private TestData data;
	
	@Before
	public void setUp() {
		data = new TestData();
		model = data.model;
	}
	
	@Test
	public void testConstructor() {
		SMAASimulator simulator = new SMAASimulator(model, 100);		
		assertEquals(100, simulator.getTotalIterations().intValue());
	}
	
	@Test
	public void testIsSimulatorRunning() throws InterruptedException {
		SMAASimulator simulator = new SMAASimulator(model, Integer.MAX_VALUE);
		assertFalse(simulator.isRunning());
		simulator.restart();
		Thread.sleep(1);
		assertTrue(simulator.isRunning());
		simulator.stop();
	}
	
	@Test
	public void testStopSimulator() throws InterruptedException {
		SMAASimulator simulator = new SMAASimulator(model, Integer.MAX_VALUE);
		simulator.restart();
		Thread.sleep(1);
		simulator.stop();
		assertFalse(simulator.isRunning());
	}	
	
	@Test
	public void testEqualRanks() throws InterruptedException {
		Alternative alt1 = new Alternative("a1");
		Alternative alt2 = new Alternative("a2");
		UniformCriterion c1 = new UniformCriterion("c1");
		AbstractCriterion<GaussianMeasurement> c2 = new GaussianCriterion("c2");
		
		ArrayList<Alternative> alts2 = new ArrayList<Alternative>();
		alts2.add(alt1);
		alts2.add(alt2);
		SMAAModel model2 = new SMAAModel("model");
		model2.setAlternatives(alts2);
		
		// set intervals for cardinal criterion
		Map<Alternative, Interval> unifMeas = new HashMap<Alternative, Interval>();
		unifMeas.put(alt1, new Interval(0.0, 0.0));
		unifMeas.put(alt2, new Interval(0.0, 0.0));
		c1.setMeasurements(unifMeas);
		
		// set measurements for gaussian criterion
		Map<Alternative, GaussianMeasurement> gausMeas = new HashMap<Alternative, GaussianMeasurement>();		
		gausMeas.put(alt1, new GaussianMeasurement(0.0, 0.0));
		gausMeas.put(alt2, new GaussianMeasurement(0.0, 0.0));
		c2.setMeasurements(gausMeas);
		
		ArrayList<Criterion> crit2 = new ArrayList<Criterion>();
		crit2.add(c1);
		crit2.add(c2);
		model2.setCriteria(crit2);
		
		SMAASimulator simulator = new SMAASimulator(model2, 10000);
		simulator.restart();
		do {
			Thread.sleep(100);
		} while (simulator.isRunning());

		SMAAResults results = simulator.getResults();
		
		Map<Alternative, List<Double>> cw = results.getCentralWeightVectors();
		List<Double> cw1 = cw.get(alt1);
		List<Double> cw2 = cw.get(alt2);

		for(int i=0;i<cw1.size();i++) {
			assertEquals(0.5, cw1.get(i), 0.02);
			assertEquals(0.5, cw2.get(i), 0.02);			
		}
	}
	
	@Test
	public void testCorrectResults() throws InterruptedException {
		setCriteriaMeasurements();
		
		SMAASimulator simulator = new SMAASimulator(model, 10000);
		simulator.restart();
		do {
			Thread.sleep(100);
		} while (simulator.isRunning());

		SMAAResults results = simulator.getResults();
		
		List<Double> cw1 = results.getCentralWeightVectors().get(model.getAlternatives().get(0));
		List<Double> cw2 = results.getCentralWeightVectors().get(model.getAlternatives().get(1));

		assertTrue(cw1.get(0) > cw2.get(0));
		assertTrue(cw1.get(1) < cw2.get(1));
		assertTrue(cw1.get(2) > cw2.get(2));
		
	}

	private void setCriteriaMeasurements() {
		// set ranks for ordinal criterion. alt1 = rank1, alt2 = rank2
		Map<Alternative, Rank> ranks = new HashMap<Alternative, Rank>();
		ranks.put(data.alt1, new Rank(1));
		ranks.put(data.alt2, new Rank(2));
		data.crit1.setMeasurements(ranks);
		
		// set intervals for cardinal criterion
		Map<Alternative, Interval> unifMeas = new HashMap<Alternative, Interval>();
		unifMeas.put(data.alt1, new Interval(0.0, 0.0));
		unifMeas.put(data.alt2, new Interval(1.0, 1.0));
		data.crit2.setMeasurements(unifMeas);
		
		// set measurements for gaussian criterion
		Map<Alternative, GaussianMeasurement> gausMeas = new HashMap<Alternative, GaussianMeasurement>();
		gausMeas.put(data.alt1, new GaussianMeasurement(1.0, 0.0));
		gausMeas.put(data.alt2, new GaussianMeasurement(0.0, 0.0));
		data.crit3.setMeasurements(gausMeas);
	}
	
}
