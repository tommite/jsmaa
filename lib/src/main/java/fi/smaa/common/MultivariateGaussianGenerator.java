package fi.smaa.common;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.CorrelatedRandomVectorGenerator;
import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomVectorGenerator;

public class MultivariateGaussianGenerator implements RandomVectorGenerator {
	private CorrelatedRandomVectorGenerator generator;
	private double[] mean;
	
	public MultivariateGaussianGenerator(RealVector mean, RealMatrix covariance, RandomGenerator generator) {
		this.mean = mean.toArray();
		if (covariance.getNorm() != 0) {
			this.generator = new CorrelatedRandomVectorGenerator(mean.toArray(), covariance,
					1.0e-12 * covariance.getNorm(), new GaussianRandomGenerator(generator));
		}
	}

	@Override
	public double[] nextVector() {
		if (this.generator == null) {
			return this.mean;
		}
		return this.generator.nextVector();
	}
}
