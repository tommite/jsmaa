/*
	This file is part of JSMAA.
	(c) Tommi Tervonen, 2009-2010	

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

package fi.smaa.common;

import java.util.Arrays;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.JDKRandomGenerator;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomDataImpl;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.RandomVectorGenerator;

public class RandomUtil {
	public static RandomUtil createWithFixedSeed() {
		JDKRandomGenerator engine = new JDKRandomGenerator();
		engine.setSeed(666);
		return new RandomUtil(engine);
	}
	
	public static RandomUtil createWithRandomSeed() {
		return new RandomUtil(new MersenneTwister());
	}

	private RandomDataImpl random;
	private RandomGenerator engine;
	
	private RandomUtil(RandomGenerator engine) {
		this.random = new RandomDataImpl(engine);
		this.engine = engine;
	}
	
	/**
	 * Generates a Gaussian distributed number.
	 * 
	 * @param mean mean of the number
	 * @param stdev standard deviation of the number
	 * @return a value sampled from the gaussian distribution
	 */
	public double createGaussian(double mean, double stddev) {
		return stddev == 0 ? mean : random.nextGaussian(mean, stddev);
	}
	
	/**
	 * Generates a sample from uniform distribution in interval [0.0, 1.0].
	 * 
	 * @return the sampled number
	 */
	public double createUnif01() {
		return random.nextUniform(0.0, 1.0);
	}
	
	/**
	 * Creates an array of random numbers that sum to a given amount.
	 * 
	 * @param dest the array to create the numbers to.
	 * @param sumTo The amount the numbers must sum to. Must be >= 0.0
	 * @throws NullPointerException if dest == null
	 */
	public void createSumToRand(double[] dest, double sumTo) throws NullPointerException {
		assert(sumTo >= 0.0);
		if (dest == null) {
			throw new NullPointerException("destination array null");
		}
				
		int len = dest.length;
		for (int i=0;i<len-1;i++) {
			dest[i] = createUnif01() * sumTo;
		}

		dest[len-1] = sumTo;
		
		Arrays.sort(dest);
			  
		double last = 0.0;
		for (int i=0;i<len;i++) {
			double t = dest[i];
			dest[i] = t - last;
			last = t;
	    }		
	}

	/**
	 * Creates random numbers that sum to 1.0.
	 * 
	 * @param dest the destination array to create the random numbers to
	 * @throws NullPointerException if dest == null
	 */
	public void createSumToOneRand(double[] dest) throws NullPointerException {
		createSumToRand(dest, 1.0);
	}

	/**
	 * Creates random numbers that sum to 1.0 and are sorted in ascending order.
	 * 
	 * @param dest the destination array to create the random numbers to
	 * @throws NullPointerException if dest == null
	 */
	public void createSumToOneSorted(double[] dest) throws NullPointerException {
		createSumToOneRand(dest);
		Arrays.sort(dest);
	}

	public double createBeta(Double min, Double max, Double alpha, Double beta) {
		double r = random.nextBeta(alpha, beta);
		return r * (max - min) + min;
	}
	
	public RandomVectorGenerator createMultivariateGaussian(RealVector mean, RealMatrix covariance) {
		return new MultivariateGaussianGenerator(mean, covariance, this.engine);
	}
}