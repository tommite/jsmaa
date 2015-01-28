/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid 2012.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper, Daan Reid, Raymond Vermaas 2013-2015.

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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.random.RandomVectorGenerator;

import fi.smaa.common.MultivariateGaussianGenerator;
import fi.smaa.common.RandomUtil;

public class MultivariateGaussianCriterionMeasurement extends AbstractEntity implements CriterionMeasurement {
	private static final long serialVersionUID = 5141555384200634310L;
	public static final String PROPERTY_ALTERNATIVES = "alternatives";
	public static final String PROPERTY_MEAN_VECTOR = "meanVector";
	public static final String PROPERTY_COVARIANCE_MATRIX = "covarianceMatrix";
	
	private final List<Alternative> alternatives;
	private RealVector meanVector;
	private RealMatrix covarianceMatrix;
	
	/**
	 * Caches the most recently used RandomUtil to check whether the MultivariateGaussianGenerator must be rebuilt.
	 */
	private RandomUtil random;
	/**
	 * Caches the MultivariateGaussianGenerator.
	 */
	private RandomVectorGenerator mvgGenerator;
	
	public MultivariateGaussianCriterionMeasurement(List<Alternative> alternatives) {
		this.alternatives = new ArrayList<Alternative>(alternatives);
		this.meanVector = new ArrayRealVector(alternatives.size(), 0.0);
		this.covarianceMatrix = MatrixUtils.createRealIdentityMatrix(alternatives.size());
	}
	
	@Override
	public List<Alternative> getAlternatives() {
		return alternatives;
	}

	@Override
	public void addAlternative(Alternative alt) {
		alternatives.add(alt);
		RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(alternatives.size());
		final RealMatrix newCovarianceMatrix = matrix;
		newCovarianceMatrix.setSubMatrix(covarianceMatrix.getData(), 0, 0);
		final RealVector newMeanVector = meanVector.append(0);
		fireEvents(setMeanVectorInternal(newMeanVector), setCovarianceMatrixInternal(newCovarianceMatrix));
	}

	@Override
	public void deleteAlternative(Alternative alt) {
		final int n = alternatives.size();
		final int index = alternatives.indexOf(alt);
		alternatives.remove(alt);

		double select[][] = new double[n - 1][n];
		for (int i = 0; i < n; ++i) {
			if (i < index) {
				select[i][i] = 1;
			} else if (i > index) {
				select[i - 1][i] = 1;
			}
		}
		Array2DRowRealMatrix m = new Array2DRowRealMatrix(select);
		
		final RealVector newMeanVector = m.operate(meanVector);
		final RealMatrix newCovarianceMatrix = covarianceMatrix.multiply(m.transpose()).preMultiply(m);
		fireEvents(setMeanVectorInternal(newMeanVector), setCovarianceMatrixInternal(newCovarianceMatrix));
	}

	@Override
	public void reorderAlternatives(List<Alternative> alts) {
		final int n = alternatives.size();
		
		double permute[][] = new double[n][n];
		for (int i = 0; i < n; ++i) {
			int j = alternatives.indexOf(alts.get(i));
			permute[i][j] = 1;
		}
		Array2DRowRealMatrix m = new Array2DRowRealMatrix(permute);
		
		alternatives.clear();
		alternatives.addAll(alts);
		
		final RealVector newMeanVector = m.operate(meanVector);
		final RealMatrix newCovarianceMatrix = covarianceMatrix.multiply(m.transpose()).preMultiply(m);
		fireEvents(setMeanVectorInternal(newMeanVector), setCovarianceMatrixInternal(newCovarianceMatrix));
	}

	public void fireEvents(RealVector oldMeanVector, RealMatrix oldCovMatrix) {
		firePropertyChange(PROPERTY_ALTERNATIVES, null, alternatives);
		firePropertyChange(PROPERTY_MEAN_VECTOR, oldMeanVector, meanVector);
		firePropertyChange(PROPERTY_COVARIANCE_MATRIX, oldCovMatrix, covarianceMatrix);
	}
	
	public void setMeanVector(RealVector newValue) {
		RealVector oldValue = setMeanVectorInternal(newValue);
		firePropertyChange(PROPERTY_MEAN_VECTOR, oldValue, newValue);
	}

	private RealVector setMeanVectorInternal(RealVector newValue) {
		if (newValue.getDimension() != alternatives.size()) {
			throw new IllegalArgumentException("Incorrect vector size " + newValue.getDimension() + ", expected " + alternatives.size());
		}
		RealVector oldValue = meanVector;
		meanVector = newValue;
		return oldValue;
	}
	
	public RealVector getMeanVector() {
		return meanVector;
	}
	
	public void setCovarianceMatrix(RealMatrix newValue) {
		RealMatrix oldValue = setCovarianceMatrixInternal(newValue);
		firePropertyChange(PROPERTY_COVARIANCE_MATRIX, oldValue, newValue);		
	}

	private RealMatrix setCovarianceMatrixInternal(RealMatrix newValue) {
		if (newValue.getRowDimension() != alternatives.size() || newValue.getColumnDimension() != alternatives.size() ) {
			throw new IllegalArgumentException("Incorrect matrix size " + newValue.getRowDimension() 
					+ "x" + newValue.getColumnDimension() + ", expected " + alternatives.size() + "x" + alternatives.size());
		}
		try { 
			mvgGenerator = random == null ? 
				new MultivariateGaussianGenerator(meanVector, newValue, null) : 
				random.createMultivariateGaussian(meanVector, newValue);
		} catch (Exception e) {
			throw new IllegalArgumentException(e);
		}
		RealMatrix oldValue = covarianceMatrix;
		covarianceMatrix = newValue;
		return oldValue;
	}
	
	public RealMatrix getCovarianceMatrix() {
		return covarianceMatrix;
	}

	@Override
	public void sample(RandomUtil random, double[][] target, int criterionIndex) {
		if (this.random != random) {
			this.random = random;
			mvgGenerator = random.createMultivariateGaussian(meanVector, covarianceMatrix);
		}
		double[] sample = mvgGenerator.nextVector();
		for (int i = 0; i < alternatives.size(); ++i) {
			target[criterionIndex][i] = sample[i];
		}
	}
	
	/**
	 * Derives range of 95% confidence interval.
	 * To calculate a bounding box for the 95% confidence region of N(0, \Sigma), let z = 1.96... be the z-value 
	 * for the 95% confidence interval of the univariate normal distribution.
	 * Then, the interval on the i-th component is [x_i^-, x_i^+] = [-z * sqrt(\Sigma_{i,i}), +z * sqrt(\Sigma_{i,i}].
	 * The range for this criterion is then [min_i \mu_i + x_i^-, max_i \mu_i + x_i^+].
	 * Source: Ross Swaney and James B. Rawlings, "Estimation of Parameters from Data", http://cbe255.che.wisc.edu/paramest.pdf 
	 */
	@Override
	public Interval getRange() {
		final double z = 1.96;
		double min = calcComponent(0, -z);
		double max = calcComponent(0, z);
		for (int i = 1; i < alternatives.size(); ++i) {
			double cmin = calcComponent(i, -z);
			double cmax = calcComponent(i, z);
			min = cmin < min ? cmin : min;
			max = cmax > max ? cmax : max;
		}
		return new Interval(min, max);
	}

	private double calcComponent(int index, double z) {
		return meanVector.getEntry(index) + z * Math.sqrt(covarianceMatrix.getEntry(index, index));
	}

	@Override
	public MultivariateGaussianCriterionMeasurement deepCopy(List<Alternative> alts) {
		if (getAlternatives().size() != alts.size()) {
			throw new IllegalArgumentException("getAlternatives().size() != alts.size()");
		}
		
		MultivariateGaussianCriterionMeasurement m = new MultivariateGaussianCriterionMeasurement(alts);
		m.setCovarianceMatrix(covarianceMatrix.copy());
		m.setMeanVector(meanVector.copy());
		return m;
	}

}
