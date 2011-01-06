package fi.smaa.jsmaa.mcmc;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.Relationship;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.Polytope;


public class HitAndRunSamplerTest {

	private HitAndRunSampler sampler;

	@Before
	public void setUp() throws OptimizationException {
		LinearConstraint lc1 = new LinearConstraint(new double[]{1.0, 1.0, 1.0}, Relationship.EQ, 1.0);
		Polytope p = new Polytope(Collections.singletonList(lc1));
		sampler = new HitAndRunSampler(p);
	}
	
	@Test
	public void testGetInitialPoint() {
		RealVector vec = sampler.getCurrentPoint();
		assertEquals(1.0, vec.getL1Norm(), 0.000001);
	}
	
	@Test(expected=OptimizationException.class)
	public void testInfeasibleInitialPoint() throws OptimizationException {
		LinearConstraint lc1 = new LinearConstraint(new double[]{1.0, 1.0, 1.0}, Relationship.EQ, 1.0);
		LinearConstraint lc2 = new LinearConstraint(new double[]{2.0, 2.0, 2.0}, Relationship.LEQ, 1.0);
		ArrayList<LinearConstraint> list = new ArrayList<LinearConstraint>();
		list.add(lc1);
		list.add(lc2);
		Polytope p = new Polytope(list);
		sampler = new HitAndRunSampler(p);
	}
}
