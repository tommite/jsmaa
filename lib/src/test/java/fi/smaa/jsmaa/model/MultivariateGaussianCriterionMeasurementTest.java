package fi.smaa.jsmaa.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;
import org.drugis.common.JUnitUtil;
import org.junit.Before;
import org.junit.Test;

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
}
