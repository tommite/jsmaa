package fi.smaa.common;

import java.util.Arrays;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.CorrelatedRandomVectorGenerator;
import org.apache.commons.math3.random.GaussianRandomGenerator;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomVectorGenerator;

public class MultivariateGaussianGenerator implements RandomVectorGenerator {
	private CorrelatedRandomVectorGenerator generator;
	private double[] mean;
	private int[] rowMap;
	
	public MultivariateGaussianGenerator(RealVector mean, RealMatrix covariance, RandomGenerator generator) {
		this.mean = mean.toArray();
		
		if (covariance.getNorm() != 0) {
			final int n = covariance.getColumnDimension();
			final double small = 1.0e-12 * covariance.getNorm();
			rowMap = new int[n];
			int[] select = new int[n]; 
					
			int j = 0;
			for (int i = 0; i < n; ++i) {
				if (covariance.getEntry(i, i) < small) {
					rowMap[i] = -1;
				} else {
					rowMap[i] = j;
					select[j] = i;
					++j;
				}
			}
			final int m = j;
			int[] selectTruncated = Arrays.copyOf(select, m);
			covariance = covariance.getSubMatrix(selectTruncated, selectTruncated);
			
			this.generator = new CorrelatedRandomVectorGenerator(new double[m], covariance,
					small, new GaussianRandomGenerator(generator));
		}
	}

	@Override
	public double[] nextVector() {
		double sample[] = new double[this.mean.length];
		System.arraycopy(this.mean, 0, sample, 0, sample.length);
		if (this.generator != null) {
			double[] x = this.generator.nextVector();
			for (int i = 0; i < mean.length; ++i) {
				sample[i] += rowMap[i] < 0 ? 0.0 : x[rowMap[i]];
			}
		}
		return sample;
	}
}
