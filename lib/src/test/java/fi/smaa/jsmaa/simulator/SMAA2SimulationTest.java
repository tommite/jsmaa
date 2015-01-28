/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import org.drugis.common.threading.TaskUtil;
import org.drugis.common.threading.ThreadHandler;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.RandomUtil;
import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.Criterion;
import fi.smaa.jsmaa.model.GaussianMeasurement;
import fi.smaa.jsmaa.model.IndependentMeasurements;
import fi.smaa.jsmaa.model.Interval;
import fi.smaa.jsmaa.model.LogNormalMeasurement;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.Rank;
import fi.smaa.jsmaa.model.SMAAModel;
import fi.smaa.jsmaa.model.ScaleCriterion;


public class SMAA2SimulationTest {
	
	private SMAAModel model;
	private Alternative alt1 = new Alternative("alt1");
	private Alternative alt2 = new Alternative("alt2");
	private ScaleCriterion c1 = new ScaleCriterion("c1");
	private ScaleCriterion c2 = new ScaleCriterion("c2");
	private ScaleCriterion c3 = new ScaleCriterion("c3");
	
	@Before
	public void setUp() {
		model = new SMAAModel("model");
		model.addAlternative(alt1);
		model.addAlternative(alt2);
		model.addCriterion(c1);
		model.addCriterion(c2);
		model.addCriterion(c3);
	}
		
	@Test
	public void testEqualRanks() throws InterruptedException {
		// set intervals for cardinal criterion
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c1, alt1, new Interval(0.0, 0.0));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c1, alt2, new Interval(0.0, 0.0));
		
		// set measurements for gaussian criterion
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c2, alt1, new GaussianMeasurement(0.0, 0.0));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c2, alt2, new GaussianMeasurement(0.0, 0.0));
		
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c3, alt1, new Interval(0.0, 0.0));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c3, alt2, new Interval(0.0, 0.0));
				
		
		SMAA2Simulation simulation = new SMAA2Simulation(model, RandomUtil.createWithFixedSeed(), 10000);
		ThreadHandler hand = ThreadHandler.getInstance();
		hand.scheduleTask(simulation.getTask());
		do {
			Thread.sleep(1);
		} while (!simulation.getTask().isFinished());

		SMAA2Results results = (SMAA2Results) simulation.getResults();
				
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
	public void testCorrectResults() throws InterruptedException {
		setCriteriaMeasurements();
		
		SMAA2Simulation simulation = new SMAA2Simulation(model, RandomUtil.createWithFixedSeed(), 10000);
		TaskUtil.run(simulation.getTask());


		SMAA2Results results = (SMAA2Results) simulation.getResults();		
		Map<Criterion, Double> cw1 = results.getCentralWeightVectors().get(alt1);
		Map<Criterion, Double> cw2 = results.getCentralWeightVectors().get(alt2);

		assertTrue(cw1.get(c1) > cw2.get(c1));
		assertTrue(cw1.get(c2) < cw2.get(c2));
		assertTrue(cw1.get(c3) > cw2.get(c3));
		
	}

	private void setCriteriaMeasurements() {
		// set interval measurements
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c1, alt1, new Interval(1.0, 1.0));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c1, alt2, new Interval(0.0, 0.0));
		
		// set interval measurements
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c2, alt1, new Interval(0.0, 0.0));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c2, alt2, new Interval(1.0, 1.0));
		
		// set gaussian measurements
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c3, alt1, new GaussianMeasurement(1.0, 0.0));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(c3, alt2, new GaussianMeasurement(0.0, 0.0));
	}
	
	@Test
	public void testCorrectResultsWithBRModel() throws InterruptedException {
		Alternative parox = new Alternative("Paroxetine");
		Alternative fluox = new Alternative("Fluoxetine");
		Alternative sert = new Alternative("Sertraline");
		Alternative ven = new Alternative("Venlafaxine");
		
		ScaleCriterion efficacy = new ScaleCriterion("Efficacy", true);
		ScaleCriterion diarrhea = new ScaleCriterion("Diarrhea", false);
		ScaleCriterion dizziness = new ScaleCriterion("Dizziness", false);
		ScaleCriterion headache = new ScaleCriterion("Headache", false);
		ScaleCriterion insomnia = new ScaleCriterion("Insomnia", false);
		ScaleCriterion nausea = new ScaleCriterion("Nausea", false);
		
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
		
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(efficacy, fluox, new LogNormalMeasurement(0.0, 0.0));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(efficacy, parox, new LogNormalMeasurement(0.086, 0.056));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(efficacy, sert, new LogNormalMeasurement(0.095, 0.044));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(efficacy, ven, new LogNormalMeasurement(0.113, 0.048));
		
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(diarrhea, fluox, new GaussianMeasurement(11.7, 2.5));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(diarrhea, parox, new GaussianMeasurement(9.2, 1.86));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(diarrhea, sert, new GaussianMeasurement(15.4, 2.65));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(diarrhea, ven, new GaussianMeasurement(5.5, 2.32));

		((IndependentMeasurements) model.getMeasurements()).setMeasurement(dizziness, fluox, new GaussianMeasurement(7.2, 1.45));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(dizziness, parox, new GaussianMeasurement(10.6, 1.58));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(dizziness, sert, new GaussianMeasurement(7.5, 1.48));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(dizziness, ven, new GaussianMeasurement(15.7, 4.44));
		
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(headache, fluox, new GaussianMeasurement(16.6, 3.27));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(headache, parox, new GaussianMeasurement(21.2, 5.15));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(headache, sert, new GaussianMeasurement(20.2, 3.78));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(headache, ven, new GaussianMeasurement(12.8, 2.45));
		
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(insomnia, fluox, new GaussianMeasurement(13.7, 1.89));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(insomnia, parox, new GaussianMeasurement(14.3, 2.93));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(insomnia, sert, new GaussianMeasurement(15.0, 3.21));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(insomnia, ven, new GaussianMeasurement(11.2, 3.98));
		
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(nausea, fluox, new GaussianMeasurement(18.6, 1.79));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(nausea, parox, new GaussianMeasurement(18.3, 3.7));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(nausea, sert, new GaussianMeasurement(19.5, 2.6));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(nausea, ven, new GaussianMeasurement(31.0, 1.68));
		
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
	
		SMAA2Simulation simulation = new SMAA2Simulation(model, RandomUtil.createWithFixedSeed(), 10000);
		ThreadHandler hand = ThreadHandler.getInstance();
		hand.scheduleTask(simulation.getTask());
		do {
			Thread.sleep(1);
		} while (!simulation.getTask().isFinished());

		SMAA2Results res = (SMAA2Results) simulation.getResults();
		
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
		
		assertEquals(0.48, conf.get(fluox), 0.03);
		assertEquals(0.44, conf.get(parox), 0.03);
		assertEquals(0.34, conf.get(sert), 0.03);
		assertEquals(0.74, conf.get(ven), 0.03);		
	}
	
	@Test
	public void testResultsWithOrdinalCriteria() throws InterruptedException {
		OrdinalCriterion o = new OrdinalCriterion("ord");
		model = new SMAAModel("model");
		model.addAlternative(alt1);
		model.addAlternative(alt2);		
		model.addCriterion(o);
		
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(o, alt1, new Rank(2));
		((IndependentMeasurements) model.getMeasurements()).setMeasurement(o, alt2, new Rank(1));
		
		SMAA2Simulation simulation = new SMAA2Simulation(model, RandomUtil.createWithFixedSeed(), 10);
		ThreadHandler hand = ThreadHandler.getInstance();
		hand.scheduleTask(simulation.getTask());
		do {
			Thread.sleep(1);
		} while (!simulation.getTask().isFinished());

		SMAA2Results results = (SMAA2Results) simulation.getResults();
		
		List<Double> ra1 = results.getRankAcceptabilities().get(alt1);
		List<Double> ra2 = results.getRankAcceptabilities().get(alt2);
				
		assertTrue(ra1.get(0) < ra2.get(0));
		assertTrue(ra1.get(1) > ra2.get(1));
	}	
	
}
