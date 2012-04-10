package fi.smaa.common;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

public class MultivariateGaussianGeneratorTest {
	@Test
	public void testSimpleWrap() {
		RealVector mean = new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 });
		RealMatrix covariance = new Array2DRowRealMatrix(new double[][] { {1.0, 0.0, 0.0}, {0.0, 0.5, 0.2}, {0.0, 0.2, 0.5} });
		RandomGenerator generator = new JDKRandomGenerator();
		new MultivariateGaussianGenerator(mean, covariance, generator).nextVector();
	}
	
	@Test
	public void testZeroVarianceComponents() {
		RealVector mean = new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 });
		RealMatrix covariance = new Array2DRowRealMatrix(new double[][] { {0.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 0.0} });
		RandomGenerator generator = new JDKRandomGenerator();
		double[] nextVector = new MultivariateGaussianGenerator(mean, covariance, generator).nextVector();
		assertEquals(1.0, nextVector[0], 0.0);
		assertEquals(3.0, nextVector[2], 0.0);
	}
	
	@Test
	public void testZeroVariance() {
		RealVector mean = new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 });
		RealMatrix covariance = new Array2DRowRealMatrix(new double[][] { {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0}, {0.0, 0.0, 0.0} });
		RandomGenerator generator = new JDKRandomGenerator();
		double[] nextVector = new MultivariateGaussianGenerator(mean, covariance, generator).nextVector();
		assertArrayEquals(new double[] {1.0, 2.0, 3.0}, nextVector, 0.0);
	}
	
	@Test
	public void testFullCovariance() {
		RealVector mean = new ArrayRealVector(new double[] { 1.0, 2.0, 3.0 });
		RealMatrix covariance = new Array2DRowRealMatrix(new double[][] { {1.0, 1.0, 0.0}, {1.0, 1.0, 0.0}, {0.0, 1.0, 0.0} });
		RandomGenerator generator = new JDKRandomGenerator();
		double[] nextVector = new MultivariateGaussianGenerator(mean, covariance, generator).nextVector();
		assertEquals(nextVector[0] - 1.0, nextVector[1] - 2.0, 10e-9);
	}
}
