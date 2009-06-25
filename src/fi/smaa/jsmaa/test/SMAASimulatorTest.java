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
import java.util.List;
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
import fi.smaa.jsmaa.model.LogNormalMeasurement;
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
	
	@Test
	public void testCorrectResultsWithBRModel() throws NoSuchAlternativeException, NoSuchCriterionException, InterruptedException {
		Alternative parox = new Alternative("Paroxetine");
		Alternative fluox = new Alternative("Fluoxetine");
		Alternative sert = new Alternative("Sertraline");
		Alternative ven = new Alternative("Venlafaxine");
		
		CardinalCriterion efficacy = new CardinalCriterion("Efficacy", true);
		CardinalCriterion diarrhea = new CardinalCriterion("Diarrhea", false);
		CardinalCriterion dizziness = new CardinalCriterion("Dizziness", false);
		CardinalCriterion headache = new CardinalCriterion("Headache", false);
		CardinalCriterion insomnia = new CardinalCriterion("Insomnia", false);
		CardinalCriterion nausea = new CardinalCriterion("Nausea", false);
		
		SMAAModel model = new SMAAModel("BRModel");
		model.addAlternative(parox);
		model.addAlternative(fluox);
		model.addAlternative(sert);
		model.addAlternative(ven);
		model.addCriterion(efficacy);
		model.addCriterion(diarrhea);
		model.addCriterion(dizziness);
		model.addCriterion(headache);
		model.addCriterion(insomnia);
		model.addCriterion(nausea);
		
		ImpactMatrix m = model.getImpactMatrix();
		
		m.setMeasurement(efficacy, fluox, new LogNormalMeasurement(0.0, 0.0));
		m.setMeasurement(efficacy, parox, new LogNormalMeasurement(0.086, 0.056));
		m.setMeasurement(efficacy, sert, new LogNormalMeasurement(0.095, 0.044));
		m.setMeasurement(efficacy, ven, new LogNormalMeasurement(0.113, 0.048));
		
		m.setMeasurement(diarrhea, fluox, new GaussianMeasurement(11.7, 2.5));
		m.setMeasurement(diarrhea, parox, new GaussianMeasurement(9.2, 1.86));
		m.setMeasurement(diarrhea, sert, new GaussianMeasurement(15.4, 2.65));
		m.setMeasurement(diarrhea, ven, new GaussianMeasurement(5.5, 2.32));

		m.setMeasurement(dizziness, fluox, new GaussianMeasurement(7.2, 1.45));
		m.setMeasurement(dizziness, parox, new GaussianMeasurement(10.6, 1.58));
		m.setMeasurement(dizziness, sert, new GaussianMeasurement(7.5, 1.48));
		m.setMeasurement(dizziness, ven, new GaussianMeasurement(15.7, 4.44));
		
		m.setMeasurement(headache, fluox, new GaussianMeasurement(16.6, 3.27));
		m.setMeasurement(headache, parox, new GaussianMeasurement(21.2, 5.15));
		m.setMeasurement(headache, sert, new GaussianMeasurement(20.2, 3.78));
		m.setMeasurement(headache, ven, new GaussianMeasurement(12.8, 2.45));
		
		m.setMeasurement(insomnia, fluox, new GaussianMeasurement(13.7, 1.89));
		m.setMeasurement(insomnia, parox, new GaussianMeasurement(14.3, 2.93));
		m.setMeasurement(insomnia, sert, new GaussianMeasurement(15.0, 3.21));
		m.setMeasurement(insomnia, ven, new GaussianMeasurement(11.2, 3.98));
		
		m.setMeasurement(nausea, fluox, new GaussianMeasurement(18.6, 1.79));
		m.setMeasurement(nausea, parox, new GaussianMeasurement(18.3, 3.7));
		m.setMeasurement(nausea, sert, new GaussianMeasurement(19.5, 2.6));
		m.setMeasurement(nausea, ven, new GaussianMeasurement(31.0, 1.68));
		
		assertEquals(0.98, efficacy.getScale().getStart(), 0.01);
		assertEquals(1.23, efficacy.getScale().getEnd(), 0.01);
		
		assertEquals(1.0, diarrhea.getScale().getStart(), 1.0);
		assertEquals(20.6, diarrhea.getScale().getEnd(), 1.0);
	
		assertEquals(4.4, dizziness.getScale().getStart(), 1.0);
		assertEquals(24.4, dizziness.getScale().getEnd(), 1.0);
	
		assertEquals(8.0, headache.getScale().getStart(), 1.0);
		assertEquals(31.3, headache.getScale().getEnd(), 1.0);
	
		assertEquals(3.4, insomnia.getScale().getStart(), 1.0);
		assertEquals(21.3, insomnia.getScale().getEnd(), 1.0);
	
		assertEquals(11.1, nausea.getScale().getStart(), 1.0);
		assertEquals(34.0, nausea.getScale().getEnd(), 1.0);
	
		SMAASimulator sim = new SMAASimulator(model, 10000);
		sim.restart();
		while (sim.isRunning()) {
			Thread.sleep(100);
		}
		
		SMAA2Results res = sim.getResults();
		Map<Alternative, Map<Criterion, Double>> cw = res.getCentralWeightVectors();
		Map<Alternative, List<Double>> ra = res.getRankAcceptabilities();
		Map<Alternative, Double> conf = res.getConfidenceFactors();
		
		assertEquals(0.20, ra.get(fluox).get(0), 0.02);
		assertEquals(0.28, ra.get(fluox).get(1), 0.02);
		assertEquals(0.30, ra.get(fluox).get(2), 0.02);
		assertEquals(0.22, ra.get(fluox).get(3), 0.02);
		
		assertEquals(0.25, ra.get(parox).get(0), 0.02);
		assertEquals(0.29, ra.get(parox).get(1), 0.02);
		assertEquals(0.27, ra.get(parox).get(2), 0.02);
		assertEquals(0.19, ra.get(parox).get(3), 0.02);

		assertEquals(0.17, ra.get(sert).get(0), 0.02);
		assertEquals(0.25, ra.get(sert).get(1), 0.02);
		assertEquals(0.29, ra.get(sert).get(2), 0.02);
		assertEquals(0.30, ra.get(sert).get(3), 0.02);

		assertEquals(0.39, ra.get(ven).get(0), 0.02);
		assertEquals(0.18, ra.get(ven).get(1), 0.02);
		assertEquals(0.15, ra.get(ven).get(2), 0.02);
		assertEquals(0.29, ra.get(ven).get(3), 0.02);
		
		assertEquals(0.08, cw.get(fluox).get(efficacy), 0.02);
		assertEquals(0.14, cw.get(fluox).get(diarrhea), 0.02);
		assertEquals(0.23, cw.get(fluox).get(dizziness), 0.02);
		assertEquals(0.18, cw.get(fluox).get(headache), 0.02);
		assertEquals(0.16, cw.get(fluox).get(insomnia), 0.02);
		assertEquals(0.22, cw.get(fluox).get(nausea), 0.02);
		
		assertEquals(0.18, cw.get(parox).get(efficacy), 0.02);
		assertEquals(0.17, cw.get(parox).get(diarrhea), 0.02);
		assertEquals(0.15, cw.get(parox).get(dizziness), 0.02);
		assertEquals(0.13, cw.get(parox).get(headache), 0.02);
		assertEquals(0.15, cw.get(parox).get(insomnia), 0.02);
		assertEquals(0.22, cw.get(parox).get(nausea), 0.02);
		
		assertEquals(0.21, cw.get(sert).get(efficacy), 0.02);
		assertEquals(0.10, cw.get(sert).get(diarrhea), 0.02);
		assertEquals(0.22, cw.get(sert).get(dizziness), 0.02);
		assertEquals(0.13, cw.get(sert).get(headache), 0.02);
		assertEquals(0.15, cw.get(sert).get(insomnia), 0.02);
		assertEquals(0.20, cw.get(sert).get(nausea), 0.02);
		
		assertEquals(0.18, cw.get(ven).get(efficacy), 0.02);
		assertEquals(0.21, cw.get(ven).get(diarrhea), 0.02);
		assertEquals(0.12, cw.get(ven).get(dizziness), 0.02);
		assertEquals(0.21, cw.get(ven).get(headache), 0.02);
		assertEquals(0.19, cw.get(ven).get(insomnia), 0.02);
		assertEquals(0.09, cw.get(ven).get(nausea), 0.02);
		
		assertEquals(0.48, conf.get(fluox), 0.02);
		assertEquals(0.45, conf.get(parox), 0.02);
		assertEquals(0.34, conf.get(sert), 0.02);
		assertEquals(0.74, conf.get(ven), 0.02);
		
		
	}
	
}