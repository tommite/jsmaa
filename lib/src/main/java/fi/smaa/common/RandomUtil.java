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

import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.RealVector;

import cern.jet.random.Beta;
import cern.jet.random.Normal;
import cern.jet.random.engine.DRand;
import cern.jet.random.engine.RandomEngine;

public class RandomUtil {
	public static RandomEngine random = new DRand(666);
	private static Normal normal = new Normal(0, 1, random);
	private static Beta betaGen = new Beta(1, 1, random);
	
	/**
	 * Generates a gaussian distributed number.
	 * 
	 * @param mean mean of the number
	 * @param stdev standard deviation of the number
	 * @return a value sampled from the gaussian distribution
	 */
	public static double createGaussian(double mean, double stdev) {
		return normal.nextDouble(mean, stdev);
	}
	
	/**
	 * Generates a sample from uniform distribution in interval [0.0, 1.0].
	 * 
	 * @return the sampled number
	 */
	public static double createUnif01() {
		return random.nextDouble();
	}
	
	/**
	 * Generates a uniformly distributed point from a dim-dimensional unit sphere
	 * @param dim dimension of the sphere
	 * @return the random point
	 */
	public static RealVector createUnifOnsphere(int dim) {
		RealVector n = new ArrayRealVector(dim);
		for (int i=0;i<dim;i++) {
			n.setEntry(i, createGaussian(0.0, 1.0));
		}
		n.unitize();
		return n;
	}
	
	/**
	 * Creates a point uniformly distributed from a line segment
	 * 
	 * @param p1 First endpoint of the segment
	 * @param p2 Second endpoint of the segment
	 * @return the random point from the segment
	 */
	public static RealVector createUnifFromSegment(RealVector p1, RealVector p2) {
		RealVector diff = p2.subtract(p1);
		diff.mapMultiplyToSelf(createUnif01());
		RealVector retP = p1.add(diff);
		return retP;
	}
	
	/**
	 * Creates an array of random numbers that sum to a given amount.
	 * 
	 * @param dest the array to create the numbers to.
	 * @param sumTo The amount the numbers must sum to. Must be >= 0.0
	 * @throws NullPointerException if dest == null
	 */
	public static void createSumToRand(double[] dest, double sumTo) throws NullPointerException {
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
	public static void createSumToOneRand(double[] dest) throws NullPointerException {
		createSumToRand(dest, 1.0);
	}

	/**
	 * Creates random numbers that sum to 1.0 and are sorted in ascending order.
	 * 
	 * @param dest the destination array to create the random numbers to
	 * @throws NullPointerException if dest == null
	 */
	public static void createSumToOneSorted(double[] dest) throws NullPointerException {
		createSumToOneRand(dest);
		Arrays.sort(dest);
	}

	public static double createBeta(Double min, Double max, Double alpha, Double beta) {
		double r = betaGen.nextDouble(alpha, beta);
		return r * (max - min) + min;
	}
	
}