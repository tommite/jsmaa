package fi.smaa.common;

import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.optimization.linear.LinearConstraint;

public class LinearConstraintSatisfier {
	
	private LinearConstraint constraint;

	public LinearConstraintSatisfier(LinearConstraint constraint) {
		this.constraint = constraint;
	}
	
	public LinearConstraint getConstraint() {
		return constraint;
	}
	
	public boolean isSatisfied(RealVector point) {
		double dotprod = constraint.getCoefficients().dotProduct(point);
		switch (constraint.getRelationship()) {
		case EQ:
			return dotprod == constraint.getValue();
		case GEQ:
			return dotprod >= constraint.getValue();
		case LEQ:
			return dotprod <= constraint.getValue();			
		}
		throw new IllegalStateException("invalid compare type");
	}

}
