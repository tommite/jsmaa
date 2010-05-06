package fi.smaa.jsmaa.simulator;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import fi.smaa.jsmaa.model.Alternative;
import fi.smaa.jsmaa.model.OrdinalCriterion;
import fi.smaa.jsmaa.model.Rank;
import fi.smaa.jsmaa.model.SMAAModel;

public class SamplerTest {

	private SMAAModel model;
	private Sampler sampler;
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
		
		model.setMeasurement(c, a1, r1);
		model.setMeasurement(c, a2, r2);
		
		sampler = new Sampler(model);
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
