package fi.smaa.common;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import junit.framework.AssertionFailedError;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.correlation.StorelessCovariance;
import org.junit.Test;

public class MultivariateGaussianGeneratorTest {
	private static final double EPSILON = 0.001;
	private static final int N_SAMPLES = 10000;

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
		RealMatrix covariance = new Array2DRowRealMatrix(new double[][] { {1.0, 1.0, 0.0}, {1.0, 1.0, 0.0}, {0.0, 0.0, 1.0} });
		RandomGenerator generator = new JDKRandomGenerator();
		double[] nextVector = new MultivariateGaussianGenerator(mean, covariance, generator).nextVector();
		assertEquals(nextVector[0] - 1.0, nextVector[1] - 2.0, 10e-9);
	}
		
	/**
	 * There is a bug in apache commons-math3 - it does not always decompose the covariance matrix correctly.
	 * @see <a href="https://issues.apache.org/jira/browse/MATH-789">bug MATH-789</a>
	 */
	@Test
	public void testWorkAroundForCommonsBug() {
		final double[][] covMatrix1 = new double[][]{
				{0.013445532, 0.010394690, 0.009881156, 0.010499559},
				{0.010394690, 0.023006616, 0.008196856, 0.010732709},
				{0.009881156, 0.008196856, 0.019023866, 0.009210099},
				{0.010499559, 0.010732709, 0.009210099, 0.019107243}
		};
		
		final double[][] covMatrix2 = new double[][]{
				{0.0, 0.0, 0.0, 0.0, 0.0},
				{0.0, 0.013445532, 0.010394690, 0.009881156, 0.010499559},
				{0.0, 0.010394690, 0.023006616, 0.008196856, 0.010732709},
				{0.0, 0.009881156, 0.008196856, 0.019023866, 0.009210099},
				{0.0, 0.010499559, 0.010732709, 0.009210099, 0.019107243}
		};
		
		final double[][] covMatrix3 = new double[][]{
				{0.013445532, 0.010394690, 0.0, 0.009881156, 0.010499559},
				{0.010394690, 0.023006616, 0.0, 0.008196856, 0.010732709},
				{0.0, 0.0, 0.0, 0.0, 0.0},
				{0.009881156, 0.008196856, 0.0, 0.019023866, 0.009210099},
				{0.010499559, 0.010732709, 0.0, 0.009210099, 0.019107243}
		};
		
		testSampler(covMatrix1);
		testSampler(covMatrix2);
		testSampler(covMatrix3);
	}

	private void testSampler(final double[][] covMatrix) {
		MultivariateGaussianGenerator sampler = new MultivariateGaussianGenerator(
				new ArrayRealVector(covMatrix.length),
				new Array2DRowRealMatrix(covMatrix),
				new JDKRandomGenerator());
		
		StorelessCovariance cov = new StorelessCovariance(covMatrix.length);
		for (int i = 0; i < N_SAMPLES; ++i) {
			double[] sample = sampler.nextVector();
			cov.increment(sample);
		}

		double[][] sampleCov = cov.getData();
		for (int r = 0; r < covMatrix.length; ++r) {
			try {
				assertArrayEquals(covMatrix[r], sampleCov[r], EPSILON);
			} catch (Error e) {
				throw new AssertionFailedError("Matrices differ at row " + r + ": " + e.getMessage());
			}
		}
	}
}
