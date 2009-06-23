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

package fi.smaa.jsmaa.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.SMAA2Results;
import fi.smaa.jsmaa.SMAASimulator;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.CardinalCriterion;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.ImpactMatrix;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.InvalidIntervalException;
import fi.smaa.jsmaa.model.NoSuchAlternativeException;
import fi.smaa.jsmaa.model.NoSuchCriterionException;
import fi.smaa.jsmaa.model.SMAAModel;


public class SMAASimulatorTest {
	
	private SMAAModel model;
	private Alternative alt1 = new Alternative("alt1");
	private Alternative alt2 = new Alternative("alt2");
	private CardinalCriterion c1 = new CardinalCriterion("c1");
	private CardinalCriterion c2 = new CardinalCriterion("c2");
	private CardinalCriterion c3 = new CardinalCriterion("c3");
	private Set<Alternative> alts;
	private Set<Criterion> crit;		
	
	@Before
	public void setUp() {
		alts = new HashSet<Alternative>();
		crit = new HashSet<Criterion>();
		alts.add(alt1);
		alts.add(alt2);
		crit.add(c1);
		crit.add(c2);
		crit.add(c3);
		model = new SMAAModel("model");
		model.setAlternatives(alts);
		model.setCriteria(crit);
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
	public void testEqualRanks() throws InterruptedException, NoSuchAlternativeException, NoSuchCriterionException, InvalidIntervalException {
		ImpactMatrix im = model.getImpactMatrix();
		// set intervals for cardinal criterion
		im.setMeasurement(c1, alt1, new Interval(0.0, 0.0));
		im.setMeasurement(c1, alt2, new Interval(0.0, 0.0));
		
		// set measurements for gaussian criterion
		im.setMeasurement(c2, alt1, new GaussianMeasurement(0.0, 0.0));
		im.setMeasurement(c2, alt2, new GaussianMeasurement(0.0, 0.0));
		
		im.setMeasurement(c3, alt1, new Interval(0.0, 0.0));
		im.setMeasurement(c3, alt2, new Interval(0.0, 0.0));
				
		SMAASimulator simulator = new SMAASimulator(model, 10000);
		simulator.restart();
		do {
			Thread.sleep(100);
		} while (simulator.isRunning());

		SMAA2Results results = simulator.getResults();
		
		Map<Alternative, Map<Criterion, Double>> cw = results.getCentralWeightVectors();
		Map<Criterion, Double> cw1 = cw.get(alt1);
		Map<Criterion, Double> cw2 = cw.get(alt2);

		for(Double d : cw1.values()) {
			assertEquals(0.333, d, 0.01);
		}
		for(Double d : cw2.values()) {
			assertEquals(0.333, d, 0.01);
		}
		
	}
	
	@Test
	public void testCorrectResults() throws InterruptedException, NoSuchAlternativeException, NoSuchCriterionException, InvalidIntervalException {
		setCriteriaMeasurements();
		
		SMAASimulator simulator = new SMAASimulator(model, 10000);
		simulator.restart();
		do {
			Thread.sleep(100);
		} while (simulator.isRunning());

		SMAA2Results results = simulator.getResults();
		
		Map<Criterion, Double> cw1 = results.getCentralWeightVectors().get(alt1);
		Map<Criterion, Double> cw2 = results.getCentralWeightVectors().get(alt2);

		assertTrue(cw1.get(c1) > cw2.get(c1));
		assertTrue(cw1.get(c2) < cw2.get(c2));
		assertTrue(cw1.get(c3) > cw2.get(c3));
		
	}

	private void setCriteriaMeasurements() throws NoSuchAlternativeException, NoSuchCriterionException, InvalidIntervalException {
		ImpactMatrix im = model.getImpactMatrix();
		// set interval measurements
		im.setMeasurement(c1, alt1, new Interval(1.0, 1.0));
		im.setMeasurement(c1, alt2, new Interval(0.0, 0.0));
		
		// set interval measurements
		im.setMeasurement(c2, alt1, new Interval(0.0, 0.0));
		im.setMeasurement(c2, alt2, new Interval(1.0, 1.0));
		
		// set gaussian measurements
		im.setMeasurement(c3, alt1, new GaussianMeasurement(1.0, 0.0));
		im.setMeasurement(c3, alt2, new GaussianMeasurement(0.0, 0.0));
	}
	
}
