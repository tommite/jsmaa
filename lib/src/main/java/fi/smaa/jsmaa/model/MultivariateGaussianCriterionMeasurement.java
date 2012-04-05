package fi.smaa.jsmaa.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.MathException;
import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.ArrayRealVector;
import org.apache.commons.math.linear.CholeskyDecompositionImpl;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.RealVector;

import fi.smaa.common.RandomUtil;

public class MultivariateGaussianCriterionMeasurement extends AbstractEntity implements CriterionMeasurement {
	private static final long serialVersionUID = 5141555384200634310L;
	public static final String PROPERTY_ALTERNATIVES = "alternatives";
	public static final String PROPERTY_MEAN_VECTOR = "meanVector";
	public static final String PROPERTY_COVARIANCE_MATRIX = "covarianceMatrix";
	
	private final List<Alternative> alternatives;
	private RealVector meanVector;
	private RealMatrix covarianceMatrix; 

	
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
		setMeanVector(meanVector.append(0));
		RealMatrix matrix = MatrixUtils.createRealIdentityMatrix(alternatives.size());
		matrix.setSubMatrix(covarianceMatrix.getData(), 0, 0);
		setCovarianceMatrix(matrix);
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
		
		setMeanVector(m.operate(meanVector));
		setCovarianceMatrix(covarianceMatrix.multiply(m.transpose()).preMultiply(m));
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
		
		setMeanVector(m.operate(meanVector));
		setCovarianceMatrix(covarianceMatrix.multiply(m.transpose()).preMultiply(m));
	}
	
	public void setMeanVector(RealVector newValue) {
		if (newValue.getDimension() != alternatives.size()) {
			throw new IllegalArgumentException("Incorrect vector size " + newValue.getDimension() + ", expected " + alternatives.size());
		}
		RealVector oldValue = meanVector;
		meanVector = newValue;
		firePropertyChange(PROPERTY_MEAN_VECTOR, oldValue, newValue);
	}
	
	public RealVector getMeanVector() {
		return meanVector;
	}
	
	public void setCovarianceMatrix(RealMatrix newValue) {
		if (newValue.getRowDimension() != alternatives.size() || newValue.getColumnDimension() != alternatives.size() ) {
			throw new IllegalArgumentException("Incorrect matrix size " + newValue.getRowDimension() 
					+ "x" + newValue.getColumnDimension() + ", expected " + alternatives.size() + "x" + alternatives.size());
		}
		try { 
			double small = 10e-12 * newValue.getNorm();
			new CholeskyDecompositionImpl(newValue, small, small);
		} catch(MathException e) {
			throw new IllegalArgumentException(e);
		}
		RealMatrix oldValue = covarianceMatrix;
		covarianceMatrix = newValue;
		firePropertyChange(PROPERTY_COVARIANCE_MATRIX, oldValue, newValue);		
	}
	
	public RealMatrix getCovarianceMatrix() {
		return covarianceMatrix;
	}

	@Override
	public void sample(RandomUtil random, double[][] target, int criterionIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Interval getRange() {
		// TODO Auto-generated method stub
		return null;
	}

}
