package fi.smaa.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.optimization.linear.LinearConstraint;
import org.apache.commons.math.optimization.linear.Relationship;
import org.junit.Test;

public class LineaConstraintSatisfierTest {
	
	@Test
	public void testIsSatisfied() {
		RealVector p1 = new ArrayRealVector(new double[]{1.0});
		RealVector p2 = new ArrayRealVector(new double[]{2.0});
		RealVector p3 = new ArrayRealVector(new double[]{3.0});
		
		LinearConstraint lc1 = new LinearConstraint(new double[]{1.0}, Relationship.EQ, 2.0);
		LinearConstraint lc2 = new LinearConstraint(new double[]{1.0}, Relationship.GEQ, 2.0);
		LinearConstraint lc3 = new LinearConstraint(new double[]{1.0}, Relationship.LEQ, 2.0);
		
		LinearConstraintSatisfier c1 = new LinearConstraintSatisfier(lc1);
		LinearConstraintSatisfier c2 = new LinearConstraintSatisfier(lc2);
		LinearConstraintSatisfier c3 = new LinearConstraintSatisfier(lc3);
		
		
		assertTrue(c1.isSatisfied(p2));
		assertFalse(c1.isSatisfied(p1));
		assertFalse(c1.isSatisfied(p3));
		
		assertTrue(c2.isSatisfied(p3));
		assertTrue(c2.isSatisfied(p2));
		assertFalse(c2.isSatisfied(p1));
		
		assertTrue(c3.isSatisfied(p1));
		assertTrue(c3.isSatisfied(p2));
		assertFalse(c3.isSatisfied(p3));		
		
	}
}
