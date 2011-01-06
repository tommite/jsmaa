package fi.smaa.jsmaa.mcmc;

import java.util.Arrays;

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.linear.LinearObjectiveFunction;
import org.apache.commons.math.optimization.linear.SimplexSolver;

import fi.smaa.common.Polytope;

public class HitAndRunSampler {

	private Polytope polytope;
	private RealVector currentPoint;
	
	public static final int DEFAULT_THINNING = 100;
	public static final int DEFAULT_BURNIN = 10000; 

	/**
	 * 
	 * @param p
	 * @throws OptimizationException in case the polytope has zero volume
	 */
	public HitAndRunSampler(Polytope p) throws OptimizationException {
		this.polytope = p;
		currentPoint = findStartingPoint();
	}
	
	public RealVector getCurrentPoint() {
		return currentPoint;
	}

	private RealVector findStartingPoint() throws OptimizationException {
		double[] coeff = new double[polytope.getDimension()];
		Arrays.fill(coeff, 1.0);
		LinearObjectiveFunction f = new LinearObjectiveFunction(coeff, 0.0);
		SimplexSolver solver = new SimplexSolver(Double.MIN_VALUE);
		solver.setMaxIterations(1);
		
		return new ArrayRealVector(solver.optimize(f, polytope.getEdges(), GoalType.MAXIMIZE, false).getPoint());
	}

	public Polytope getPolytope() {
		return polytope;
	}
}
