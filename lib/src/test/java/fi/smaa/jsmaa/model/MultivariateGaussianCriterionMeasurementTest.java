/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.

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
package fi.smaa.jsmaa.model;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.drugis.common.JUnitUtil;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.RandomUtil;

public class MultivariateGaussianCriterionMeasurementTest {
	private List<Alternative> alternatives;
	private MultivariateGaussianCriterionMeasurement m;

	@Before
	public void setUp() {
		alternatives = new ArrayList<Alternative>(
				Arrays.asList(new Alternative("Ubuntu"), new Alternative("Debian"), new Alternative("Fedora")));
		m = new MultivariateGaussianCriterionMeasurement(alternatives);
		
	}
	
	@Test
	public void testInitialization() {
		// Test initial values with 3 alternatives
		assertEquals(alternatives, m.getAlternatives());
		RealVector mu = new ArrayRealVector(3, 0.0);
		assertEquals(mu, m.getMeanVector());
		RealMatrix sigma = new Array2DRowRealMatrix(new double[][] { {1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0} });
		assertEquals(sigma, m.getCovarianceMatrix());
		
		// Test defensive copying of alternatives list
		ArrayList<Alternative> expected = new ArrayList<Alternative>(alternatives);
		alternatives.remove(0);
		assertEquals(expected, m.getAlternatives());
		
		// Test initial values with 2 alternatives
		MultivariateGaussianCriterionMeasurement m2 = new MultivariateGaussianCriterionMeasurement(alternatives);
		assertEquals(alternatives, m2.getAlternatives());
		RealVector mu2 = new ArrayRealVector(2, 0.0);
		assertEquals(mu2, m2.getMeanVector());
		RealMatrix sigma2 = new Array2DRowRealMatrix(new double[][] { {1.0, 0.0}, {0.0, 1.0} });
		assertEquals(sigma2, m2.getCovarianceMatrix());
	}
	
	@Test
	public void testSetMeanVector() {
		RealVector mu1 = new ArrayRealVector(3, 0.0);
		RealVector mu2 = new ArrayRealVector(new double[] { 25.3, 2.1, -3 });
		JUnitUtil.testSetter(m, MultivariateGaussianCriterionMeasurement.PROPERTY_MEAN_VECTOR, mu1, mu2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSetWrongSizeMeanVector() {
		m.setMeanVector(new ArrayRealVector(4, 0.0));
	}
	
	@Test
	public void testSetCovarianceMatrix() {
		RealMatrix sigma1 = MatrixUtils.createRealIdentityMatrix(3);
		RealMatrix sigma2 = new Array2DRowRealMatrix(new double[][] { {1.0, 0.5, 0.0}, {0.5, 1.0, 0.0}, {0.0, 0.0, 1.0} });
		JUnitUtil.testSetter(m, MultivariateGaussianCriterionMeasurement.PROPERTY_COVARIANCE_MATRIX, sigma1, sigma2);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testWrongSizeCovarianceMatrix() {
		RealMatrix sigma = new Array2DRowRealMatrix(new double[][] { {1.5, 0.0}, {1.0, 0.5}, {1.0, 0.02} });
		m.setCovarianceMatrix(sigma);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testInvalidCovarianceMatrix() {
		RealMatrix sigma = new Array2DRowRealMatrix(new double[][] { {1.5, 0.0, 0.0}, {1.0, 0.5, 0.0}, {1.0, -3.0, 1.0} });
		m.setCovarianceMatrix(sigma);
	}
	
	@Test
	public void testModifyAlternatives() {
		m.setCovarianceMatrix(new Array2DRowRealMatrix(new double[][] { {2.0, 0.5, 0.0}, {0.5, 2.0, 0.0}, {0.0, 0.0, 2.0} }));
		m.setMeanVector(new ArrayRealVector(new double[] { 25.3, 2.1, -3 }));
		Alternative alt = new Alternative("Slackware");
		m.addAlternative(alt);
		
		RealMatrix sigma = new Array2DRowRealMatrix(new double[][] { {2.0, 0.5, 0.0, 0.0}, {0.5, 2.0, 0.0, 0.0}, {0.0, 0.0, 2.0, 0.0}, {0.0, 0.0, 0.0, 1.0} });
		RealVector mu = new ArrayRealVector(new double[] { 25.3, 2.1, -3, 0.0 });
		
		alternatives.add(alt);
		assertEquals(alternatives, m.getAlternatives());
		assertEquals(mu, m.getMeanVector());
		assertEquals(sigma, m.getCovarianceMatrix());
		
		Alternative del = alternatives.get(1);
		alternatives.remove(del);
		m.deleteAlternative(del);
		assertEquals(alternatives, m.getAlternatives());
		sigma = new Array2DRowRealMatrix(new double[][] { {2.0, 0.0, 0.0}, {0.0, 2.0, 0.0}, {0.0, 0.0, 1.0} });
		mu = new ArrayRealVector(new double[] { 25.3, -3, 0.0 });
		assertEquals(mu, m.getMeanVector());
		assertEquals(sigma, m.getCovarianceMatrix());
		
		List<Alternative> reordered = Arrays.asList(alternatives.get(1), alternatives.get(2), alternatives.get(0));
		m.setCovarianceMatrix(new Array2DRowRealMatrix(new double[][] { {2.0, 0.5, 0.0}, {0.5, 2.0, -0.2}, {0.0, -0.2, 1.0} }));
		m.reorderAlternatives(reordered);
		assertEquals(reordered, m.getAlternatives());
		sigma = new Array2DRowRealMatrix(new double[][] { {2.0, -0.2, 0.5}, {-0.2, 1.0, 0.0}, {0.5, 0.0, 2.0} });
		mu = new ArrayRealVector(new double[] { -3, 0.0, 25.3 });
		assertEquals(mu, m.getMeanVector());
		assertEquals(sigma, m.getCovarianceMatrix());
		
		// FIXME: define what events should be fired and when.
	}
	
	@Test
	public void testRandomSampling() {
		RandomUtil random = RandomUtil.createWithRandomSeed();
		m.setCovarianceMatrix(new Array2DRowRealMatrix(new double[][] { {2.0, 0.5, 0.0}, {0.5, 2.0, 0.0}, {0.0, 0.0, 2.0} }));
		double[] mean = new double[] { 25.3, 2.1, -3 };
		m.setMeanVector(new ArrayRealVector(mean));
		double[][] target = new double[5][3];
		int index = 1;
		m.sample(random, target, index);

		// The sampler should only write to the target row.
		assertArrayEquals(new double[3], target[0], 0.0);
		assertArrayEquals(new double[3], target[2], 0.0);
		assertArrayEquals(new double[3], target[3], 0.0);
		assertArrayEquals(new double[3], target[4], 0.0);

		// We expect non-zero values at least somewhere.
		assertFalse(new Array2DRowRealMatrix(target).getNorm() == 0);

		// We expect the generated values to be random, hence different.
		double[][] target2 = new double[5][3];
		m.sample(random, target2, index);
		assertFalse(target[index][0] == target2[index][0]);
		assertFalse(target[index][1] == target2[index][1]);
		assertFalse(target[index][2] == target2[index][2]);
		
		// With zero variance, we expect to get the mean.
		m.setCovarianceMatrix(new Array2DRowRealMatrix(new double[3][3]));
		m.sample(random, target, index);
		assertArrayEquals(mean, target[index], 0.0);
	}
	
	@Test
	public void testGetRange() {
		m.setCovarianceMatrix(new Array2DRowRealMatrix(new double[][] { {2.0, 0.5, 0.0}, {0.5, 2.0, 0.0}, {0.0, 0.0, 3.0} }));
		m.setMeanVector(new ArrayRealVector(new double[] { 25.3, 2.1, -3 }));
		
		Interval interval = m.getRange();
		assertEquals(25.3 + 1.96 * Math.sqrt(2), interval.getEnd(), 10e-7);
		assertEquals(-3 - 1.96 * Math.sqrt(3.0), interval.getStart(), 10e-7);
	}
	
	@Test
	public void testDeepCopy() {
		List<Alternative> newAlts = new ArrayList<Alternative>();
		for (Alternative a : alternatives) {
			newAlts.add(a.deepCopy());
		}
		m.setMeanVector(new ArrayRealVector(new double[] { 666, 8, 665 }));
		m.setCovarianceMatrix(MatrixUtils.createRealDiagonalMatrix(new double[] { 4, 3, 2 }));
		
		MultivariateGaussianCriterionMeasurement clone = m.deepCopy(newAlts);
		assertNotSame(m, clone);
		assertEquals(newAlts, clone.getAlternatives());
		assertSame(newAlts.get(0), clone.getAlternatives().get(0));
		assertEquals(m.getMeanVector(), clone.getMeanVector());
		assertNotSame(m.getMeanVector(), clone.getMeanVector());
		assertEquals(m.getCovarianceMatrix(), clone.getCovarianceMatrix());
		assertNotSame(m.getCovarianceMatrix(), clone.getCovarianceMatrix());
	}
}
