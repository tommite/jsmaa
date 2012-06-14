/*
    This file is part of JSMAA.
    JSMAA is distributed from http://smaa.fi/.

    (c) Tommi Tervonen, 2009-2010.
    (c) Tommi Tervonen, Gert van Valkenhoef 2011.
    (c) Tommi Tervonen, Gert van Valkenhoef, Joel Kuiper 2012.

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.junit.Before;
import org.junit.Test;

import fi.smaa.common.RandomUtil;

public class PerCriterionMeasurementsTest {

	private List<Alternative> alternatives;
	private List<Criterion> criteria;
	private PerCriterionMeasurements m;

	@Before
	public void setUp() { 
		alternatives = new ArrayList<Alternative>(Arrays.asList(new Alternative("ubuntu"), new Alternative("gentoo"), new Alternative("mint")));
		criteria = new ArrayList<Criterion>(Arrays.asList(new ScaleCriterion("size"), new ScaleCriterion("Boot time")));
		m = new PerCriterionMeasurements(criteria, alternatives);
	}
	
	@Test
	public void testInitialization() { 
		assertEquals(alternatives, m.getAlternatives());
		assertEquals(criteria, m.getCriteria());
		assertNotNull(m.getCriterionMeasurement(criteria.get(0)));
		assertEquals(alternatives, m.getCriterionMeasurement(criteria.get(0)).getAlternatives());
		assertSame(m.getCriterionMeasurement(criteria.get(0)), m.getCriterionMeasurement(criteria.get(0)));
		assertEquals(m.getCriterionMeasurement(criteria.get(0)).getRange(), ((ScaleCriterion)criteria.get(0)).getScale());
	}
	
	@Test 
	public void testSetCriterionMeasurement() { 
		MultivariateGaussianCriterionMeasurement m2 = new MultivariateGaussianCriterionMeasurement(alternatives);
		m2.setMeanVector(new ArrayRealVector(new double[] {1.0, 1.0, 1.0}));
		m.setCriterionMeasurement(criteria.get(1), m2);
		assertSame(m.getCriterionMeasurement(criteria.get(1)), m2);
		assertEquals(m.getCriterionMeasurement(criteria.get(0)).getRange(), ((ScaleCriterion)criteria.get(0)).getScale());
		assertEquals(m.getCriterionMeasurement(criteria.get(1)).getRange(), ((ScaleCriterion)criteria.get(1)).getScale());
	}
	
	@Test
	public void testChangeMeasurement() {
		((MultivariateGaussianCriterionMeasurement) m.getCriterionMeasurement(criteria.get(0))).setMeanVector(new ArrayRealVector(new double[] {1.0, 1.0, 1.0}));
		assertEquals(m.getCriterionMeasurement(criteria.get(0)).getRange(), ((ScaleCriterion)criteria.get(0)).getScale());
	}
	
	@Test
	public void testAddAlternatives() { 
		((MultivariateGaussianCriterionMeasurement) m.getCriterionMeasurement(criteria.get(0))).setMeanVector(new ArrayRealVector(new double[] {1.0, 1.0, 1.0}));
		Alternative newAlt = new Alternative("redhat");
		m.addAlternative(newAlt);
		alternatives.add(newAlt);
		assertEquals(alternatives, m.getAlternatives());
		assertEquals(alternatives, m.getCriterionMeasurement(criteria.get(0)).getAlternatives());
		assertEquals(m.getCriterionMeasurement(criteria.get(0)).getRange(), ((ScaleCriterion)criteria.get(0)).getScale());
	}
	
	@Test
	public void testDeleteAlternatives() {
		((MultivariateGaussianCriterionMeasurement) m.getCriterionMeasurement(criteria.get(0))).setMeanVector(new ArrayRealVector(new double[] {1.0, -1.0, -1.0}));
		m.deleteAlternative(alternatives.get(0));
		alternatives.remove(0);
		assertEquals(alternatives, m.getAlternatives());
		assertEquals(alternatives, m.getCriterionMeasurement(criteria.get(0)).getAlternatives());
		assertEquals(m.getCriterionMeasurement(criteria.get(0)).getRange(), ((ScaleCriterion)criteria.get(0)).getScale());
	}
	
	@Test 
	public void testReorderAlternatives() { 
		List<Alternative> newAlternatives = Arrays.asList(alternatives.get(2), alternatives.get(1), alternatives.get(0));
		m.reorderAlternatives(newAlternatives);
		assertEquals(newAlternatives, m.getAlternatives());
		assertEquals(newAlternatives, m.getCriterionMeasurement(criteria.get(0)).getAlternatives());

	}
	
	@Test
	public void testAddCriterion() {
		Criterion newCrit = new ScaleCriterion("LOC");
		criteria.add(newCrit);
		m.addCriterion(newCrit);
		assertEquals(criteria, m.getCriteria());
		assertNotNull(m.getCriterionMeasurement(newCrit));
	}
	
	@Test 
	public void testDeleteCriterion() { 
		Criterion c = m.getCriteria().get(0);
		
		m.deleteCriterion(criteria.get(0));
		criteria.remove(0);
		
		assertEquals(criteria, m.getCriteria());
		assertNull(m.getCriterionMeasurement(c));
	}
	
	@Test 
	public void testReorderCriteria() { 
		List<Criterion> newCrit = Arrays.asList(criteria.get(1), criteria.get(0));
		CriterionMeasurement c1 = m.getCriterionMeasurement(criteria.get(0));
		CriterionMeasurement c2 = m.getCriterionMeasurement(criteria.get(1));
		m.reorderCriteria(newCrit);
		assertEquals(newCrit, m.getCriteria());
		
		assertEquals(m.getCriterionMeasurement(criteria.get(0)), c1);
		assertEquals(m.getCriterionMeasurement(criteria.get(1)), c2);
	}
	
	@Test
	public void testDeepCopy() {
		// Created cloned criteria / alternatives
		List<Criterion> newCrit = new ArrayList<Criterion>();
		for (Criterion c : criteria) {
			newCrit.add(c.deepCopy());
		}
		List<Alternative> newAlts = new ArrayList<Alternative>();
		for (Alternative a : alternatives) {
			newAlts.add(a.deepCopy());
		}
		
		MultivariateGaussianCriterionMeasurement mvg = new MultivariateGaussianCriterionMeasurement(alternatives);
		mvg.setMeanVector(new ArrayRealVector(new double[] { 1.0, 1.5, -0.5 }));
		m.setCriterionMeasurement(criteria.get(1), mvg);
		
		PerCriterionMeasurements clone = m.deepCopy(newCrit, newAlts);
		assertNotSame(clone, m);
		assertEquals(newCrit, clone.getCriteria());
		assertSame(newCrit.get(0), clone.getCriteria().get(0));
		assertEquals(newAlts, clone.getAlternatives());
		assertSame(newAlts.get(0), clone.getAlternatives().get(0));
		assertEquals(newAlts, clone.getCriterionMeasurement(newCrit.get(0)).getAlternatives());
		assertEquals(newAlts, clone.getCriterionMeasurement(newCrit.get(1)).getAlternatives());
		
		assertEquals(mvg.getMeanVector(), ((MultivariateGaussianCriterionMeasurement)clone.getCriterionMeasurement(newCrit.get(1))).getMeanVector());
	}
	
	@Test
	public void testSample() {
		double[] d0 = new double[] { 8, 16, 32 };
		double[] d1 = new double[] { 1.0, 1.5, -0.5 };

		setFixedMeasurements(d0, d1);
		
		double[][] target = new double[2][3];
		m.sample(RandomUtil.createWithRandomSeed(), target);
		assertArrayEquals(d0, target[0], 0.0);
		assertArrayEquals(d1, target[1], 0.0);
	}

	private void setFixedMeasurements(double[] d0, double[] d1) {
		MultivariateGaussianCriterionMeasurement mvg0 = new MultivariateGaussianCriterionMeasurement(alternatives);
		mvg0.setMeanVector(new ArrayRealVector(d0));
		mvg0.setCovarianceMatrix(MatrixUtils.createRealMatrix(3, 3));
		m.setCriterionMeasurement(criteria.get(0), mvg0);
		MultivariateGaussianCriterionMeasurement mvg1 = new MultivariateGaussianCriterionMeasurement(alternatives);
		mvg1.setMeanVector(new ArrayRealVector(d1));
		mvg1.setCovarianceMatrix(MatrixUtils.createRealMatrix(3, 3));
		m.setCriterionMeasurement(criteria.get(1), mvg1);
	}
	
	@Test
	public void testRange() {
		double[] d0 = new double[] { 8, 16, 32 };
		double[] d1 = new double[] { 1.0, 1.5, -0.5 };

		setFixedMeasurements(d0, d1);
		
		assertEquals(-0.5, m.getRange(criteria.get(1)).getStart(), 0.0);
		assertEquals(1.5, m.getRange(criteria.get(1)).getEnd(), 0.0);
		assertEquals(8, m.getRange(criteria.get(0)).getStart(), 0.0);
		assertEquals(32, m.getRange(criteria.get(0)).getEnd(), 0.0);
	}
}
